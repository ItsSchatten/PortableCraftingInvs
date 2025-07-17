package com.itsschatten.portablecrafting;

import com.itsschatten.libs.UpdateNotifications;
import com.itsschatten.libs.Utils;
import com.itsschatten.libs.configutils.PlayerConfigManager;
import com.itsschatten.portablecrafting.commands.*;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.configs.SignsConfig;
import com.itsschatten.portablecrafting.listeners.EnderchestListener;
import com.itsschatten.portablecrafting.listeners.PlayerJoinListener;
import com.itsschatten.portablecrafting.listeners.SignListener;
import com.itsschatten.portablecrafting.storage.HikariConnection;
import com.itsschatten.portablecrafting.storage.StorageCredentials;
import com.itsschatten.portablecrafting.storage.StorageMedium;
import com.itsschatten.portablecrafting.storage.implementations.YamlStorage;
import com.itsschatten.portablecrafting.storage.implementations.sql.MariaDbStorage;
import com.itsschatten.portablecrafting.storage.implementations.sql.MySQLStorage;
import com.itsschatten.portablecrafting.virtual.Storage;
import com.itsschatten.portablecrafting.virtual.VirtualManager;
import com.itsschatten.portablecrafting.virtual.machine.BrewingStand;
import com.itsschatten.portablecrafting.virtual.machine.Furnace;
import com.itsschatten.portablecrafting.virtual.machine.properties.BrewingProperties;
import com.itsschatten.portablecrafting.virtual.utils.FurnaceType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class PortableCraftingInvsPlugin extends JavaPlugin {

    @Getter(AccessLevel.PUBLIC)
    private static PCIAPI fakeContainers;

    @Getter
    @Setter(value = AccessLevel.PRIVATE)
    private static PortableCraftingInvsPlugin instance; // The instance stuffs.

    @Getter
    private static String serverVersion;

    @Getter
    private VirtualManager manager;

    public static boolean isPaperServer() {
        return serverVersion.equals("PAPER");
    }

    @Override
    public void onEnable() { // We all know what this does right? Right!?
        Utils.setInstance(this);
        setInstance(this);

        try {
            // Check for a paper-specific class, if it doesn't exist, an error is thrown that we ignore.
            // If it does exist, we instead just set the version to PAPER.
            Class.forName("io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader");

            serverVersion = "PAPER";
        } catch (Throwable ignored) {
            // We can assume this is probably a normal spigot implementation, so we'll split the version.
            serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        }

        // Register configuration files.
        Settings.init();
        Messages.init();

        final PluginDescriptionFile pdf = this.getDescription();
        if (!Settings.SILENT_START_UP)
            Utils.log("+---------------------------------------------------+ ",
                    " ",
                    " _____   _____ _____ ",
                    "|  __ \\ / ____|_   _|",
                    "| |__) | |      | |  ",
                    "|  ___/| |      | |  ",
                    "| |    | |____ _| |_ ",
                    "|_|     \\_____|_____|",
                    " ",
                    "Developed by " + String.join(",", pdf.getAuthors()),
                    "Version " + pdf.getVersion(),
                    "Using Minecraft version " + serverVersion);


        // Check if we are unsupported. There is no point in continuing if we're unsupported.
        if (!supported()) {
            Utils.log("You are running an unsupported version of Minecraft!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Register one tick after all plugins are loaded.
        // This should effectively allow the registration of "custom" furnace recipes.
        // This will not work for "dynamic" (unloaded during run time) recipes, however.
        // I don't think we'll ever support that behavior anyway.
        // If it's even a thing :)
        Bukkit.getScheduler().runTaskLater(this, this::registerFakeContainers, 1L);

        if (Settings.ATTEMPT_MIGRATION_AT_START) {
            Utils.log("Attempting to migrate data from an older version of PCI...");
            try {
                final Properties properties = new Properties();
                final File file = new File(getDataFolder(), "PCI.properties");

                // Check if we have the file already if we don't, generate it.
                if (!file.exists()) {
                    Utils.debugLog("Unable to find the properties file in the data folder! Creating a new one.");
                    properties.load(getResource("PCI.properties"));
                } else {
                    // We do have a file, load it.
                    Utils.debugLog("Found properties file in the data folder!");
                    try (final FileInputStream in = new FileInputStream(file)) {
                        properties.load(in);
                    }
                }

                // The iteration of this properties file.
                // @since 2.0.0-SNAPSHOT
                final int iteration = Integer.parseInt(properties.getProperty("iteration", "0"));
                final long start = System.currentTimeMillis();

                // Check if we are at the iteration.
                if (iteration != 1) {
                    //<editor-fold desc="Migration" default-state="collapsed">

                    // The current date, used to show the backups.
                    // This is built to work on Windows,
                    // this may need additional tweaks if file creation fails on another OS.
                    final String date = LocalDateTime.now().toString().replace("T", "_").replaceAll("\\..*", "").replace(":", ".");

                    try {
                        // Our backup files.
                        final File brew = new File(getDataFolder().getPath() + File.separator + "backups", "brewing-stands-backup-" + date + ".yml");
                        final File furn = new File(getDataFolder().getPath() + File.separator + "backups", "furnace-backup-" + date + ".yml");

                        // Check if the files exist, if they don't attempt to make any directories and then create the file.
                        if (!brew.exists()) {
                            new File(brew.getParent()).mkdirs();
                            brew.createNewFile();
                        }
                        if (!furn.exists()) {
                            new File(furn.getParent()).mkdirs();
                            furn.createNewFile();
                        }

                        // Copy the current brew and furnace files to their backup.
                        FileUtil.copy(new File(getDataFolder(), "brewing-stands.yml"), brew);
                        FileUtil.copy(new File(getDataFolder(), "furnaces.yml"), furn);

                        // Furnace and Brewing stand conversion.
                        // Virtual Tile UUID → Player
                        final Map<UUID, UUID> virtualToPlayer = new HashMap<>();

                        // Furnaces, we remove the types, so we can then convert it to a new type.
                        final Path furnacePath = Paths.get(getDataFolder() + File.separator + "furnaces.yml");
                        String content = Files.readString(furnacePath, StandardCharsets.UTF_8);
                        content = content.replaceAll("==: com.itsschatten.*", "");
                        Files.writeString(furnacePath, content);

                        // Brewing stands.
                        final Path brewingPath = Paths.get(getDataFolder() + File.separator + "brewing-stands.yml");
                        String brewingContent = Files.readString(brewingPath, StandardCharsets.UTF_8);
                        brewingContent = brewingContent.replaceAll("==: com.itsschatten.*", "");
                        Files.writeString(brewingPath, brewingContent);

                        // Load the furnace storage file.
                        final File furnacesFile = new File(getDataFolder(), "furnaces.yml");
                        final FileConfiguration furnaces = YamlConfiguration.loadConfiguration(furnacesFile);

                        // Load the brewing stand storage file.
                        final File brewingFile = new File(getDataFolder(), "brewing-stands.yml");
                        final FileConfiguration brewingStands = YamlConfiguration.loadConfiguration(brewingFile);

                        // Check if we have used SQL in the past.
                        final Settings settings = Settings.getInstance();
                        if (settings.getBoolean("use-sql")) {
                            final String user = settings.getString("sql-user");
                            final String pass = settings.getString("sql-pass");
                            final String host = settings.getString("sql-host");
                            final String db = settings.getString("sql-database");
                            final String port = String.valueOf(settings.getInt("sql-port"));

                            Utils.log("Attempting to connect to your old configured SQL database...");

                            //<editor-fold desc="Connect to SQL and convert." defaultstate="collapsed">
                            HikariConfig config;
                            try {
                                config = new HikariConfig();
                            } catch (LinkageError er) {
                                Utils.logError(er);
                                Utils.logError("Failed to create a hikari config on startup!", "https://github.com/ItsSchatten/PortableCraftingInvs/issues/new?template=bug_report.yml&labels=labels=type%3A+bug%2Cpriority%3A+normal%2Cstatus%3A+awaiting+confirmation");
                                return;
                            }

                            config.setPoolName("portable-crafting-invs-hikari-migrate-startup");

                            config.setJdbcUrl(String.format("jdbc:%s://%s:%s/%s?autoReconnect=true&useSSL=false", "mysql", host, port, db));
                            config.setUsername(user);
                            config.setPassword(pass);

                            Utils.log("Starting sql extraction...");
                            long sqlStart = System.currentTimeMillis();
                            // Connect and get a connection.
                            try (final HikariDataSource source = new HikariDataSource(config);
                                 final Connection connection = source.getConnection()) {

                                // Get all furnaces.
                                try (final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `pci_furnaces`");
                                     final ResultSet set = statement.executeQuery()) {

                                    // Loop stored.
                                    while (set.next()) {
                                        virtualToPlayer.put(UUID.fromString(Objects.requireNonNull(set.getString("furnace_uuid"))), UUID.fromString(set.getString("owner_uuid")));
                                    }
                                }

                                // Get all brewing stands.
                                try (final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `pci_brewing_stands`");
                                     final ResultSet set = statement.executeQuery()) {

                                    // Loop stored.
                                    while (set.next()) {
                                        virtualToPlayer.put(UUID.fromString(Objects.requireNonNull(set.getString("stand_uuid"))), UUID.fromString(set.getString("owner_uuid")));
                                    }
                                }
                            } catch (SQLException e) {
                                Utils.logError(e);
                            }
                            Utils.log("SQL extraction finished in " + (System.currentTimeMillis() - sqlStart) + "ms");
                            //</editor-fold>
                        } else {
                            // We haven't used SQL in the past, attempt to migrate from old YAML.
                            final File dataFolder = new File(Utils.getInstance().getDataFolder(), "data");

                            // Check if we have player data.
                            if (dataFolder.exists() && dataFolder.isDirectory() && dataFolder.listFiles() != null) {
                                Arrays.stream(Objects.requireNonNullElse(dataFolder.listFiles(), new File[0]))
                                        // Filter the files.
                                        .filter((playerFile) -> playerFile.getName().endsWith(".yml"))
                                        // Convert each player file.
                                        .forEach((playerFile) -> {
                                            // The UUID (or name) of the file.
                                            final UUID uuid = UUID.fromString(playerFile.getName().replace(".yml", ""));

                                            // Load a player's config file.
                                            final PlayerConfigManager pcm = PlayerConfigManager.getConfig(uuid);
                                            final FileConfiguration configuration = pcm.getConfig();

                                            // Check if we have furnaces.
                                            if (configuration.contains("furnaces")) {
                                                // If we do; go ahead and loop all furnaces.
                                                for (final String key : Objects.requireNonNull(configuration.getConfigurationSection("furnaces")).getKeys(false)) {
                                                    virtualToPlayer.put(UUID.fromString(Objects.requireNonNull(configuration.getString("furnaces." + key))), uuid);
                                                }
                                            }

                                            // Check if we have a brewing stand if we do add it to the map.
                                            if (configuration.contains("brewing")) {
                                                virtualToPlayer.put(UUID.fromString(Objects.requireNonNull(configuration.getString("brewing"))), uuid);
                                            }
                                        });
                            }
                        }

                        // Loop all found machines mapped to the player.
                        virtualToPlayer.forEach((machine, player) -> {
                            // Get the player config.
                            final PlayerConfigManager pcm = PlayerConfigManager.getConfig(player);
                            final FileConfiguration configuration = pcm.getConfig();

                            // Check if the furnaces config contain our machine.
                            if (furnaces.contains("furnaces." + machine)) {
                                // We do, get the values.
                                final Map<String, Object> vals = furnaces.getConfigurationSection("furnaces." + machine).getValues(true);

                                // Get the properties key, so we can determine what the furnace type is.
                                final String propertiesKey = furnaces.getString("furnaces." + machine + ".properties.key", "furnace");
                                final FurnaceType type = propertiesKey.contains("blast") ? FurnaceType.BLASTING : propertiesKey.contains("smoker") ? FurnaceType.SMOKER : FurnaceType.FURNACE;

                                // Create/Load our furnace.
                                final Furnace furnace = new Furnace(
                                        (String) vals.get("name"),
                                        machine,
                                        type.getProperties(),
                                        type,
                                        (ItemStack) vals.get("fuel"),
                                        (ItemStack) vals.get("input"),
                                        (ItemStack) vals.get("output"),
                                        (Integer) vals.get("cookTime"),
                                        (Integer) vals.get("fuelTime"),
                                        (Integer) vals.get("fuelTime"),
                                        ((Double) vals.get("xp")).floatValue(),
                                        LocalDateTime.now()
                                );

                                // Save the furnace for the player.
                                configuration.set("furnaces." + machine, furnace);
                                Utils.debugLog("Migrated the machine " + machine + " as a furnace!");
                            } else if (brewingStands.contains("brewing." + machine)) {
                                // We check for a brewing stand instead.
                                // We have one get the values.
                                final Map<String, Object> vals = brewingStands.getConfigurationSection("brewing." + machine).getValues(true);

                                // Convert the individual bottles into an array.
                                final ItemStack[] bottles = new ItemStack[3];
                                bottles[0] = (ItemStack) vals.get("bottle-1");
                                bottles[1] = (ItemStack) vals.get("bottle-2");
                                bottles[2] = (ItemStack) vals.get("bottle-3");

                                // Create/Load our brewing stand.
                                final BrewingStand stand = new BrewingStand(
                                        (String) vals.get("name"),
                                        machine,
                                        bottles,
                                        BrewingProperties.NORMAL,
                                        (ItemStack) vals.get("fuel"),
                                        (ItemStack) vals.get("ingredient"),
                                        (Integer) vals.get("fuelTime"),
                                        (Integer) vals.get("maxBrews"),
                                        (Integer) vals.get("brewTime"),
                                        0,
                                        LocalDateTime.now()
                                );

                                // Save the configuration.
                                configuration.set("brewing-stands." + machine, stand);
                                Utils.debugLog("Migrated the machine " + machine + " as a brewing stand!");
                            } else {
                                Utils.logWarning("Failed to migrate " + machine + " to a player...");
                            }

                            // Save our config.
                            pcm.saveConfig();
                        });

                        // Clean up PlayerConfigManager configs.
                        PlayerConfigManager.getConfigs().clear();

                        // Update the property.
                        properties.setProperty("iteration", "1");
                        // Update the file.
                        try (final FileOutputStream out = new FileOutputStream(file)) {
                            properties.store(out, "Updated in version " + pdf.getVersion() + " of PCI.");
                        }
                    } catch (NoSuchFileException ex) {
                        Utils.logWarning("Couldn't find a storage file to migrate.");
                        Utils.logWarning("If you do have just a 'furnace.yml' or a 'brewing-stands.yml' please create the other file and the migration should complete.");
                    } catch (Exception e) {
                        Utils.logError(e);
                        Utils.logError("Failed to migrate data!");
                    }
                    //</editor-fold>
                } else {
                    Utils.log("You're up to date! You can safely disable `attempt-migration-at-start` if you wish!");
                    Utils.debugLog("Completed migration check (already up to date) in " + (System.currentTimeMillis() - start) + "ms.");
                }
            } catch (IOException e) {
                Utils.logError(e);
            }
        }

        if (Settings.USE_METRICS) {
            Utils.log("Metrics are enabled! You can see the information collect at the following link: https://bstats.org/plugin/bukkit/PortableCraftingInvs",
                    "If you don't wish for this information to be collected you can disable it in the 'settings.yml'.");
            new Metrics(this, 5_752);
        }

        UpdateNotifications notifications = null;
        if (Settings.USE_UPDATER) {
            notifications = new UpdateNotifications(61_045) {
                boolean available = false;

                @Override
                public void onUpdateAvailable() {
                    Utils.log("There is an update available for the plugin! Current Version " + pdf.getVersion() + " New Version " + getLatestVersion() + " (https://spigotmc.org/resources/" + getProjectId() + ")");

                    if (!available) {
                        Bukkit.getOnlinePlayers().stream().filter((player) -> player.hasPermission(Permissions.UPDATE_NOTIFICATIONS.getPermission())).forEach((admin) -> {
                            Utils.tell(admin, getUpdateMessage().replace("{currentVer}", PortableCraftingInvsPlugin.getInstance().getDescription().getVersion())
                                    .replace("{newVer}", UpdateNotifications.getLatestVersion())
                                    .replace("{link}", "https://spigotmc.org/resources/" + UpdateNotifications.getProjectId()));
                        });

                        available = true;
                    }
                }
            };

            notifications.runTaskTimerAsynchronously(this, 0, Settings.UPDATE_CHECK_INTERVAL * (60 * 20));

            Utils.debugLog("Checked for update, and set timer running, checking for an update again in " + Settings.UPDATE_CHECK_INTERVAL + " minutes.");
        }

        if (Settings.USE_ENDERCHEST_RESTRICTION) {
            this.getServer().getPluginManager().registerEvents(new EnderchestListener(), this);
            Utils.debugLog("USE_ENDERCHEST_RESTRICTIONS is true; EnderchestListener has been initialized.");
        }

        if (Settings.USE_SIGNS) {
            SignsConfig.init();
            this.getServer().getPluginManager().registerEvents(new SignListener(), this);
            Utils.debugLog("Signs have been enabled.");
        }

        // Register commands.
        registerCommands(new PortableCraftingInvsCommand());

        if (Settings.USE_ANVIL) registerCommands(new AnvilCommand());
        if (Settings.USE_CARTOGRAPHY) registerCommands(new CartographyCommand());
        if (Settings.USE_CRAFTING) registerCommands(new CraftCommand());
        if (Settings.USE_ENCHANT_TABLE) registerCommands(new EnchantmentTableCommand());
        if (Settings.USE_ENDERCHEST) registerCommands(new EnderChestCommand());
        if (Settings.USE_GRINDSTONE) registerCommands(new GrindStoneCommand());
        if (Settings.USE_SMITHING_TABLE) registerCommands(new SmithingCommand());
        if (Settings.USE_LOOM) registerCommands(new LoomCommand());
        if (Settings.USE_STONE_CUTTER) registerCommands(new StoneCutterCommand());

        // Virtual.
        if (Settings.USE_VIRTUAL_TILES) {
            if (Settings.USE_FURNACE) registerCommands(new FurnaceCommand());
            if (Settings.USE_BLAST_FURNACE) registerCommands(new BlastFurnaceCommand());
            if (Settings.USE_SMOKER) registerCommands(new SmokerCommand());
            if (Settings.USE_BREWING) registerCommands(new BrewingStandCommand());
        }

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(notifications), this);
        Utils.debugLog("Loaded event listeners.");

        if (!Settings.SILENT_START_UP)
            Utils.log("+---------------------------------------------------+ ",
                    "");
    }

    // Method to quickly determine the versions we support.
    private boolean supported() {
        return switch (serverVersion) {
            case "v1_21_R5",
                 "v1_21_R4",
                 "v1_21_R3",
                 "v1_21_R2",
                 "v1_21_R1",
                 "v1_20_R4",
                 "v1_20_R3",
                 "v1_20_R2",
                 "v1_20_R1",
                 "PAPER" -> true;
            default -> false;
        };
    }

    private void registerFakeContainers() {
        final long then = System.currentTimeMillis();
        Utils.debugLog("Begin loading fake containers.");

        // Switch the server version.
        switch (serverVersion) {
            case "v1_21_R5" -> fakeContainers = new FakeContainers_v1_21_R5();
            case "v1_21_R4" -> fakeContainers = new FakeContainers_v1_21_R4();
            case "v1_21_R3" -> fakeContainers = new FakeContainers_v1_21_R3();
            case "v1_21_R2" -> fakeContainers = new FakeContainers_v1_21_R2();
            case "v1_21_R1" -> fakeContainers = new FakeContainers_v1_21_R1();
            case "v1_20_R4" -> fakeContainers = new FakeContainers_v1_20_R4();
            case "v1_20_R3" -> fakeContainers = new FakeContainers_v1_20_R3();
            case "v1_20_R2" -> fakeContainers = new FakeContainers_v1_20_R2();
            case "v1_20_R1" -> fakeContainers = new FakeContainers_v1_20_R1();
            // Check whether Paper's plugin loader is available.
            // If it is, we'll go ahead and use a paper specific FakeContainers instance.
            // This also bypasses Paper's craft bukkit relocation.
            // This does also pose an issue that lesser paper versions may not function appropriately.
            case "PAPER" -> fakeContainers = new FakeContainersPaper();
            default -> {
                Utils.logError("Version " + serverVersion + " of Spigot is not supported by this plugin, to avoid issues the plugin will now disable.");
                Utils.debugLog("Failed to register fake containers in " + (System.currentTimeMillis() - then) + "ms.");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }

        if (Settings.USE_VIRTUAL_TILES) {
            if (!Settings.USE_FURNACE && !Settings.USE_BLAST_FURNACE && !Settings.USE_SMOKER && !Settings.USE_BREWING) {
                Utils.logWarning("[ATTENTION] Hey! You seem to have to disabled all virtual tile features, because of this we will not be registering it's API so we don't take up any additional resources.");
            } else {
                final Storage medium = switch (StorageMedium.valueOf(Settings.STORAGE_MEDIUM.toUpperCase())) {
                    case MYSQL -> new MySQLStorage(new StorageCredentials());
                    case MARIADB -> new MariaDbStorage(new StorageCredentials());

                    /* TODO: H2 and SQLite
                    LATER...
                    case H2, SQLITE -> {
                        Utils.logWarning("H2 and SQLite have yet to be setup properly!");
                        yield null;
                    }*/
                    case YAML -> new YamlStorage();
                };

                Utils.debugLog("Using " + medium.implementationName() + " storage medium!");

                // If we are an HikariConnection, go ahead and init the connection.
                if (medium instanceof HikariConnection connection) {
                    Utils.debugLog("Calling HikariConnection#init()!");
                    connection.init();
                }

                // Calling this will register the manager
                // and make a static instance for it to be accessed (Singleton).
                // This may change in the future to use dependency injection instead.
                this.manager = new VirtualManager(medium, Settings.getInstance());
                Utils.log("Loaded the virtual tile API.");
            }
        } else {
            Utils.debugLog("Not loading virtual tile API.");
        }

        // Register PCIFakeContainers for API usages.
        Bukkit.getServicesManager().register(PCIAPI.class, fakeContainers, this, ServicePriority.Normal);
        Utils.debugLog("Registered fake containers in " + (System.currentTimeMillis() - then) + "ms.");
    }

    // Remove the instance of the plugin, to help prevent memory leaks,
    // and set it to null in the Utils, so we can get a new instance of it when it's reloaded.
    // Also disable the VirtualFurnace API if it is enabled.
    @Override
    public void onDisable() {
        if (VirtualManager.getInstance() != null && VirtualManager.getInstance().isLoaded())
            VirtualManager.getInstance().shutdown();

        CraftCommand.setInstance(null);
        EnderChestCommand.setInstance(null);
        setInstance(null);
        Utils.setInstance(null);
    }

    // Util method to register commands quickly.
    private void registerCommands(Command... commands) {
        try {
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            for (Command command : commands) {
                command.register(commandMap);
                commandMap.register(this.getName(), command);
                Utils.debugLog("Command " + command.getName() + " has been registered.");
            }
        } catch (Exception ex) {
            Utils.logError(ex);
            Utils.logError("Some error occurred, please report this immediately to ItsSchatten on Spigot or Github.", "(https://github.com/ItsSchatten/PortableCraftingInvs/issues/new/choose)");
        }
    }

    // Uses some reflection to access the server's command map and unregisters commands.
    public final void unregisterCommand(Command... commands) {
        if (commands == null) return;
        try {
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            for (final Command command : commands) {
                if (command == null) continue;
                command.unregister(commandMap);
                Utils.debugLog("Command " + command.getName() + " has been unregistered.");
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Utils.logError(e);
            Utils.logError("Some error occurred, please report this immediately to ItsSchatten on Spigot or Github.", "(https://github.com/ItsSchatten/PortableCraftingInvs/issues/new/choose)");
        }
    }
}
