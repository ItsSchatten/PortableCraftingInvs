package com.itsschatten.portablecrafting;

import com.itsschatten.libs.UpdateNotifications;
import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.commands.*;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.MySql;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.configs.SignsConfig;
import com.itsschatten.portablecrafting.listeners.*;
import com.shanebeestudios.api.VirtualFurnaceAPI;
import com.shanebeestudios.api.machine.Furnace;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;

public class PortableCraftingInvsPlugin extends JavaPlugin {

    @Getter(AccessLevel.PUBLIC)
    private static FakeContainers fakeContainers;

    @Getter
    @Setter(value = AccessLevel.PRIVATE)
    private static PortableCraftingInvsPlugin instance; // The instance stuffs.

    @Getter
    private static String serverVersion;

    @Getter
    @Setter
    private static MySql database;

    @Override
    public void onEnable() { // We all know what this does right? Right!?
        Utils.setInstance(this);
        setInstance(this);
        serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        // Register configs.
        Settings.init();
        Messages.init();

        final PluginDescriptionFile pdf = this.getDescription();
        if (!Settings.SILENT_START_UP)
            Utils.log("",
                    "&9+---------------------------------------------------+ ",
                    "",
                    "&9 _____   _____ _____ ",
                    "&9|  __ \\ / ____|_   _|",
                    "&3| |__) | |      | |  ",
                    "&3|  ___/| |      | |  ",
                    "&b| |    | |____ _| |_ ",
                    "&b|_|     \\_____|_____|",
                    "",
                    "&7Developed by " + String.join(",", pdf.getAuthors()),
                    "&7Version " + pdf.getVersion(),
                    "&7Using Minecraft version " + serverVersion);


        if (Settings.USE_MYSQL) {
            database = new MySql(Settings.MYSQL_HOST, Settings.MYSQL_PORT, Settings.MYSQL_DATABASE, Settings.MYSQL_USER, Settings.MYSQL_PASS);
        } else {
            database = null;
        }

        if (Settings.ATTEMPT_MIGRATION_AT_START) {
            try {
                final Properties properties = new Properties();
                final File file = new File(getDataFolder(), "PCI.properties");
                if (!file.exists()) {
                    Utils.debugLog(Settings.DEBUG, "Unable to find the properties file in the data folder! Creating a new one.");
                    properties.load(getResource("PCI.properties"));
                } else {
                    Utils.debugLog(Settings.DEBUG, "Found properties file in the data folder!");
                    FileInputStream in = new FileInputStream(file);
                    properties.load(in);
                }

                int version = Integer.parseInt(properties.getProperty("furnace-version", "0"));
                if (version != 2) {
                    FileOutputStream out = new FileOutputStream(getDataFolder() + File.separator + "PCI.properties");
                    properties.setProperty("furnace-version", "2");
                    properties.store(out, null);
                    out.close();
                    long timeStart = System.currentTimeMillis();

                    try {
                        Utils.debugLog(Settings.DEBUG, "Attempting to update API references to new package...");
                        Utils.debugLog(Settings.DEBUG, "Attempting to migrate furnaces.yml...");
                        Path furnacePath = Paths.get(getDataFolder() + File.separator + "furnaces.yml");

                        String content = Files.readString(furnacePath, StandardCharsets.UTF_8);
                        content = content.replaceAll("com\\.shanebeestudios\\.api", "com.itsschatten.portablecrafting.libs");
                        Files.writeString(furnacePath, content);

                        Utils.debugLog(Settings.DEBUG, "Attempting to migrate brewing-stands.yml");
                        Path brewingPath = Paths.get(getDataFolder() + File.separator + "brewing-stands.yml");
                        String brewingContent = Files.readString(brewingPath, StandardCharsets.UTF_8);
                        brewingContent = brewingContent.replaceAll("com\\.shanebeestudios\\.api", "com.itsschatten.portablecrafting.libs");
                        Files.writeString(brewingPath, brewingContent);
                        Utils.debugLog(Settings.DEBUG, "API package migration completed...");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    if (registerFakeContainers()) return;

                    FileConfiguration configuration = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "furnaces.yml"));

                    ConfigurationSection section = configuration.getConfigurationSection("furnaces");
                    if (section != null) {
                        for (String string : section.getKeys(true)) {
                            final Furnace furnace = VirtualFurnaceAPI.getInstance().getFurnaceManager().getByID(UUID.fromString(string));

                            if (!furnace.getName().equalsIgnoreCase("furnace")) {
                                if (furnace.getProperties().getFuelMultiplier() != 2) {
                                    furnace.getProperties().fuelMultiplier(2);
                                    Utils.debugLog(Settings.DEBUG, "Found a furnace that I can migrate, attempting to set cookX to 2");
                                }
                            }
                        }
                    }
                    VirtualFurnaceAPI.getInstance().getFurnaceManager().saveConfig();

                    Utils.debugLog(Settings.DEBUG, "Completed furnace migration in " + (System.currentTimeMillis() - timeStart) + "ms");
                } else {
                    if (registerFakeContainers()) return;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (registerFakeContainers()) return;
        }
        fakeContainers.setUsingMysql(Settings.USE_MYSQL);
        fakeContainers.setDebug(Settings.DEBUG);

        if (Settings.USE_METRICS) {
            Utils.log("&7Metrics are enabled! You can see the information collect at the following link: &chttps://bstats.org/plugin/bukkit/PortableCraftingInvss&7",
                    "If you don't wish for this information to be collected you can disable it in the settings.yml.");
            new Metrics(this, 5752);
        }

        if (!Settings.USE_FURNACE && !Settings.USE_BLAST_FURNACE && !Settings.USE_SMOKER && !Settings.USE_BREWING) {
            VirtualFurnaceAPI.getInstance().disableAPI();
            Utils.debugLog(Settings.DEBUG, "&cFurnaces and Brewing stands are not enabled, disabled the API so we won't take up loads of resources.");
        }

        if (Settings.USE_UPDATER) {
            new UpdateNotifications(61045) {
                @Override
                public void onUpdateAvailable() {
                    Utils.log("There is an update available for the plugin! Current Version " + pdf.getVersion() + " New Version " + getLatestVersion() + " {https://spigotmc.org/resources/" + getProjectId() + ")");
                }
            }.runTaskAsynchronously(this);

            new CheckForUpdate().runTaskTimerAsynchronously(this, Settings.UPDATE_CHECK_INTERVAL * (60 * 20), Settings.UPDATE_CHECK_INTERVAL * (60 * 20)); // Wait for the time set in the settings.yml before checking for an update.
            Utils.debugLog(Settings.DEBUG, "Checked for update, and set timer running, checking for update again in " + Settings.UPDATE_CHECK_INTERVAL + " minutes.");
        }

        if (Settings.USE_ENDERCHEST_RESTRICTION) {
            this.getServer().getPluginManager().registerEvents(new EnderchestListener(), this);
            Utils.debugLog(Settings.DEBUG, "USE_ENDERCHEST_RESTRICTIONS is true; EnderchestListener has been initialized.");
        }

        if (Settings.USE_SIGNS) {
            SignsConfig.init();
            this.getServer().getPluginManager().registerEvents(new SignListener(), this);
            Utils.debugLog(Settings.DEBUG, "Signs have been enabled.");
        }

        // Register commands, and JoinListener.
        registerCommands(new EnchanttableCommand(), new PortableCraftingInvsCommand(), new FurnaceCommand(), new SmokerCommand(), new BlastFurnaceCommand(), new BrewingStandCommand());

        if (Settings.ALLOW_ESSENTIALS && Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            if (!Settings.USE_CRAFTING) {
                Utils.debugLog(Settings.DEBUG, "Crafting features have been disabled, and Essentials has been installed. To avoid causing issues we are not going to register the command.");
            } else {
                registerCommand(new CraftCommand());
            }

            if (!Settings.USE_ENDERCHEST) {
                Utils.debugLog(Settings.DEBUG, "Enderchest features have been disabled, and Essentials has been installed. To avoid causing issues we are not going to register the command.");
            } else {
                registerCommand(new EnderChestCommand());
            }

            if (!Settings.USE_ANVIL) {
                Utils.debugLog(Settings.DEBUG, "Anvil features have been disabled, and Essentials has been installed. To avoid causing issues we are not going to register the command.");
            } else {
                registerCommand(new AnvilCommand());
            }

            if (!Settings.USE_GRINDSTONE) {
                Utils.debugLog(Settings.DEBUG, "Grind stone features have been disabled, and Essentials has been installed. To avoid causing issues we are not going to register the command.");
            } else {
                registerCommand(new GrindStoneCommand());
            }

            if (!Settings.USE_LOOM) {
                Utils.debugLog(Settings.DEBUG, "Loom features have been disabled, and Essentials has been installed. To avoid causing issues we are not going to register the command.");
            } else {
                registerCommand(new LoomCommand());
            }

            if (!Settings.USE_STONE_CUTTER) {
                Utils.debugLog(Settings.DEBUG, "Stonecutter features have been disabled, and Essentials has been installed. To avoid causing issues we are not going to register the command.");
            } else {
                registerCommand(new StoneCutterCommand());
            }

            if (!Settings.USE_CARTOGRAPHY) {
                Utils.debugLog(Settings.DEBUG, "Cartography table features have been disabled, and Essentials has been installed. To avoid causing issues we are not going to register the command.");
            } else {
                registerCommand(new CartographyCommand());
            }

            if (!Settings.USE_SMITHING_SIGN) {
                Utils.debugLog(Settings.DEBUG, "Smithing table features have been disabled, and Essentials has been installed. To avoid causing issues we are not going to register the command.");
            } else {
                registerCommand(new SmithingCommand());
            }

        } else {
            registerCommands(new AnvilCommand(), new GrindStoneCommand(), new EnderChestCommand(), new CraftCommand(),
                    new LoomCommand(), new StoneCutterCommand(), new CartographyCommand(), new SmithingCommand());

            if (Settings.USE_ANVIL)
                this.getServer().getPluginManager().registerEvents(new AnvilListeners(), this);

        }

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new EnchantmentListener(), this);
        Utils.debugLog(Settings.DEBUG, "Loaded event listeners.");

        if (!Settings.SILENT_START_UP)
            Utils.log("&9+---------------------------------------------------+ ",
                    "");
    }

    private boolean registerFakeContainers() {
        switch (serverVersion) {
            case "v1_18_R2" -> fakeContainers = new FakeContainers_v1_18_R2(this, database);
            case "v1_18_R1" -> fakeContainers = new FakeContainers_v1_18_R1(this, database);
            case "v1_17_R1" -> fakeContainers = new FakeContainers_v1_17_R1(this, database);
            default -> {
                Utils.log("&4&l! Attention ! &cVersion " + serverVersion + " of Spigot is not supported by this plugin, to avoid issues the plugin will be disabled.");
                Bukkit.getPluginManager().disablePlugin(this);
                return true;
            }
        }
        return false;
    }

    // Remove the instance of the plugin, to help prevent memory leaks, and set it to null in the Utils so we can get a new instance of it when it's reloaded.
    // Also disable the VirtualFurnace API if it is enabled and the one of the furnaces is enabled.
    @Override
    public void onDisable() {
        if (VirtualFurnaceAPI.getInstance() != null && VirtualFurnaceAPI.getInstance().isEnabled() &&
                (Settings.USE_FURNACE | Settings.USE_BLAST_FURNACE | Settings.USE_SMOKER | Settings.USE_BREWING))
            VirtualFurnaceAPI.getInstance().disableAPI();

        CraftCommand.setInstance(null);
        EnderChestCommand.setInstance(null);
        setInstance(null);
        Utils.setInstance(null);
    }

    // A utility method so I can register commands on one line.
    private void registerCommand(Command command) {
        try {
            Utils.registerCommand(command);
            Utils.debugLog(Settings.DEBUG, "&7Command " + command.getName() + " has been registered.");
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.log("Some error occurred, please report this immediately to ItsSchatten on Spigot or Github. \n(https://github.com/ItsSchatten/PortableCraftingInvs/issues)");
        }
    }

    private void registerCommands(Command... commands) {
        try {
            for (Command command : commands) {
                Utils.registerCommand(command);
                Utils.debugLog(Settings.DEBUG, "&7Command " + command.getName() + " has been registered.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.log("Some error occurred, please report this immediately to ItsSchatten on Spigot or Github. \n(https://github.com/ItsSchatten/PortableCraftingInvs/issues)");
        }
    }

    // Uses some reflection to access the server's command map and unregister out commands.
    public void unregisterCommand(Command command) {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            command.unregister(commandMap);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
