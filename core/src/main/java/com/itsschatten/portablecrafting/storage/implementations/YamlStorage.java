package com.itsschatten.portablecrafting.storage.implementations;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.configutils.PlayerConfigManager;
import com.itsschatten.portablecrafting.virtual.Storage;
import com.itsschatten.portablecrafting.virtual.machine.BrewingStand;
import com.itsschatten.portablecrafting.virtual.machine.Furnace;
import com.itsschatten.portablecrafting.virtual.utils.FurnaceType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class YamlStorage implements Storage {

    @Override
    public String implementationName() {
        return "Per-player YAML";
    }

    @Override
    public void saveFurnace(UUID player, Furnace furnace) {
        final PlayerConfigManager pcm = PlayerConfigManager.getConfig(player);
        final FileConfiguration config = pcm.getConfig();

        config.set("furnaces." + furnace.getUniqueId(), furnace);
        pcm.saveConfig();
    }

    @Override
    public Furnace loadFurnace(UUID player, UUID uuid) {
        final PlayerConfigManager pcm = PlayerConfigManager.getConfig(player);
        final FileConfiguration config = pcm.getConfig();

        return (Furnace) config.get("furnaces." + uuid);
    }

    @Override
    public void saveBrewingStand(UUID player, BrewingStand brewingStand) {
        final PlayerConfigManager pcm = PlayerConfigManager.getConfig(player);
        final FileConfiguration config = pcm.getConfig();

        config.set("brewing-stands." + brewingStand.getUniqueId(), brewingStand);
        pcm.saveConfig();
    }

    @Override
    public BrewingStand loadBrewingStand(UUID player, UUID uuid) {
        final PlayerConfigManager pcm = PlayerConfigManager.getConfig(player);
        final FileConfiguration config = pcm.getConfig();

        return (BrewingStand) config.get("brewing-stands." + uuid);
    }

    @Override
    public UUID getFurnaceUUIDFromID(UUID player, Integer id, FurnaceType type) {
        final PlayerConfigManager pcm = PlayerConfigManager.getConfig(player);
        final FileConfiguration config = pcm.getConfig();

        final List<UUID> uuids = new ArrayList<>();
        if (config.getConfigurationSection("furnaces") != null) {
            Objects.requireNonNull(config.getConfigurationSection("furnaces")).getKeys(false).stream()
                    .map((key) -> {
                        try {
                            return (Furnace) config.get("furnaces." + key);
                        } catch (ClassCastException ex) {
                            return null;
                        }
                    })
                    .filter((furn) -> furn != null && furn.getType() == type)
                    .forEach(furnace -> uuids.add(furnace.getUniqueId()));
        }

        try {
            return uuids.isEmpty() ? null : uuids.get(id);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    @Override
    public UUID getBrewingStandUUIDFromID(UUID player, Integer id) {
        final PlayerConfigManager pcm = PlayerConfigManager.getConfig(player);
        final FileConfiguration config = pcm.getConfig();

        final List<UUID> uuids = new ArrayList<>();

        if (config.getConfigurationSection("brewing-stands") != null) {
            Objects.requireNonNull(config.getConfigurationSection("brewing-stands")).getKeys(false).forEach((key) -> uuids.add(UUID.fromString(key)));
        }

        try {
            return uuids.isEmpty() ? null : uuids.get(id);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    @Override
    public int getPlayerFurnaceCount(final Player player, final FurnaceType type) {
        final PlayerConfigManager pcm = PlayerConfigManager.getConfig(player);
        final FileConfiguration config = pcm.getConfig();

        if (config.getConfigurationSection("furnaces") != null) {
            return Math.toIntExact(Objects.requireNonNull(config.getConfigurationSection("furnaces")).getKeys(false).stream()
                    .map((key) -> {
                        try {
                            return (Furnace) config.get("furnaces." + key);
                        } catch (ClassCastException ex) {
                            return null;
                        }
                    })
                    .filter((furn) -> furn != null && furn.getType() == type).count());
        }

        return 0;
    }

    @Override
    public int getPlayerBrewingStandCount(final Player player) {
        final PlayerConfigManager pcm = PlayerConfigManager.getConfig(player);
        final FileConfiguration config = pcm.getConfig();

        if (config.getConfigurationSection("brewing-stands") != null) {
            return Objects.requireNonNull(config.getConfigurationSection("brewing-stands")).getKeys(false).size();
        }

        return 0;
    }

    @Override
    public void shutdown() {
        // Nothing to worry about.
    }

    @Override
    public int getUniquePlayers() {
        final File folder = new File(Utils.getInstance().getDataFolder(), "data");
        if (folder.exists() && folder.isDirectory() && folder.listFiles() != null) {
            return Math.toIntExact(Arrays.stream(Objects.requireNonNullElse(folder.listFiles(), new File[0])).filter((file) -> file.getName().endsWith(".yml")).count());

        }

        return 0;
    }
}
