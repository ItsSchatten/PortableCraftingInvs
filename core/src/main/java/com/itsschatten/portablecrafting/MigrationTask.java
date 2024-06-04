package com.itsschatten.portablecrafting;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.configutils.PlayerConfigManager;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.storage.HikariConnection;
import com.itsschatten.portablecrafting.storage.StorageCredentials;
import com.itsschatten.portablecrafting.storage.StorageMedium;
import com.itsschatten.portablecrafting.storage.implementations.YamlStorage;
import com.itsschatten.portablecrafting.storage.implementations.sql.MariaDbStorage;
import com.itsschatten.portablecrafting.virtual.VirtualManager;
import com.itsschatten.portablecrafting.virtual.machine.BrewingStand;
import com.itsschatten.portablecrafting.virtual.machine.Furnace;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * Class responsible for starting a migration task.
 */
public class MigrationTask implements Runnable {

    /**
     * The medium to transfer to.
     */
    final StorageMedium toMedium;

    public MigrationTask(StorageMedium toMedium) {
        this.toMedium = toMedium;
    }

    @Override
    public void run() {
        final long start = System.currentTimeMillis();

        Utils.log("Starting migration...");
        switch (toMedium) {
            case MYSQL -> {
                if (Settings.CURRENT_MEDIUM == StorageMedium.MYSQL || Settings.CURRENT_MEDIUM == StorageMedium.MARIADB) {
                    Bukkit.getOnlinePlayers().stream().filter((player) -> player.hasPermission("pci.migrate")).forEach(player -> Utils.tell(player, "&cFailed to migrate data! You cannot migrate between MySQL and MariaDB! Please manually transfer your data."));
                    throw new UnsupportedOperationException("PCI doesn't support migrating between MySQL and MariaDB, please manually transfer your data.");
                }

                final MariaDbStorage storage = new MariaDbStorage(new StorageCredentials());
                storage.init();

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
                                        try {
                                            storage.saveFurnace(uuid, (Furnace) configuration.get("furnaces." + key));
                                        } catch (ClassCastException ignored) {
                                        }
                                    }
                                }

                                // Check if we have a brewing stand if we do add it to the map.
                                if (configuration.contains("brewing-stands")) {
                                    // If we do; go ahead and loop all furnaces.
                                    for (final String key : Objects.requireNonNull(configuration.getConfigurationSection("brewing-stands")).getKeys(false)) {
                                        try {
                                            storage.saveBrewingStand(uuid, (BrewingStand) configuration.get("brewing-stands." + key));
                                        } catch (ClassCastException ignored) {
                                        }
                                    }
                                }
                            });
                }
            }

            case MARIADB -> {
                if (Settings.CURRENT_MEDIUM == StorageMedium.MYSQL || Settings.CURRENT_MEDIUM == StorageMedium.MARIADB) {
                    Bukkit.getOnlinePlayers().stream().filter((player) -> player.hasPermission("pci.migrate")).forEach(player -> Utils.tell(player, "&cFailed to migrate data! You cannot migrate between MariaDB and MySQL! Please manually transfer your data."));
                    throw new UnsupportedOperationException("PCI doesn't support migrating between MariaDB and MySQL, please manually transfer your data.");
                }

                final MariaDbStorage storage = new MariaDbStorage(new StorageCredentials());
                storage.init();

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
                                        try {
                                            storage.saveFurnace(uuid, (Furnace) configuration.get("furnaces." + key));
                                        } catch (ClassCastException ignored) {
                                        }
                                    }
                                }

                                // Check if we have a brewing stand if we do add it to the map.
                                if (configuration.contains("brewing-stands")) {
                                    // If we do; go ahead and loop all furnaces.
                                    for (final String key : Objects.requireNonNull(configuration.getConfigurationSection("brewing-stands")).getKeys(false)) {
                                        try {
                                            storage.saveBrewingStand(uuid, (BrewingStand) configuration.get("brewing-stands." + key));
                                        } catch (ClassCastException ignored) {
                                        }
                                    }
                                }
                            });
                }
            }

            case YAML -> {
                if (Settings.CURRENT_MEDIUM == StorageMedium.YAML) {
                    Bukkit.getOnlinePlayers().stream().filter((player) -> player.hasPermission("pci.migrate")).forEach(player -> Utils.tell(player, "&cFailed to migrate data! You cannot migrate between YAML and YAML!"));
                    throw new UnsupportedOperationException("You cannot migrate from YAML to YAML!");
                }

                final YamlStorage storage = new YamlStorage();

                // Make sure we have a hikari storage.
                if (VirtualManager.getInstance().getStorage() instanceof HikariConnection hikariConnection) {
                    // Get the connection.
                    try (final Connection connection = hikariConnection.getConnection()) {
                        // Get the uuids for brewing stands.
                        try (final PreparedStatement statement = connection.prepareStatement("SELECT `uuid`,`owner` FROM `{prefix}brewing_stand_storage`".replace("{prefix}", Settings.DATABASE_PREFIX));
                             final ResultSet set = statement.executeQuery()) {
                            while (set.next()) {
                                final UUID owner = UUID.fromString(set.getString("owner"));
                                storage.saveBrewingStand(owner, VirtualManager.getInstance().getBrewingStand(owner, UUID.fromString("uuid")));
                            }
                        }

                        // Get the uuids for furnaces.
                        try (final PreparedStatement statement = connection.prepareStatement("SELECT `uuid`,`owner` FROM `{prefix}furnace_storage`".replace("{prefix}", Settings.DATABASE_PREFIX));
                             final ResultSet set = statement.executeQuery()) {
                            final UUID owner = UUID.fromString(set.getString("owner"));
                            storage.saveFurnace(owner, VirtualManager.getInstance().getFurnace(owner, UUID.fromString("uuid")));
                        }

                    } catch (SQLException e) {
                        Utils.logError(e);
                        Bukkit.getOnlinePlayers().stream().filter((player) -> player.hasPermission("pci.migrate")).forEach(player -> Utils.tell(player, "&cFailed to migrate data! An issue occurred during SQL. Check console for details."));
                    }
                }
            }
        }

        Utils.log("Finished migration in " + (System.currentTimeMillis() - start) + "ms.");
    }
}
