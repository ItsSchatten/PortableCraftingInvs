package com.itsschatten.portablecrafting.configs;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.configutils.SimpleConfig;
import com.itsschatten.portablecrafting.PortableCraftingInvsPlugin;
import com.itsschatten.portablecrafting.storage.StorageMedium;
import com.itsschatten.portablecrafting.virtual.ISettings;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import javax.annotation.Untainted;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Sets values to be used throughout the plugin.
 */
public class Settings extends SimpleConfig implements ISettings {

    public static StorageMedium CURRENT_MEDIUM = null;

    public static boolean
            SILENT_START_UP,
            ATTEMPT_MIGRATION_AT_START,
            DEBUG,
            USE_PERMISSIONS,
            USE_UPDATER,
            USE_METRICS,
            USE_MESSAGES,
            USE_TOO_MANY_ARGS,
            USE_HELP_IF_WRONG_ARGS,
            USE_RANDOM_SOUND_PITCH,
            USE_SIGNS,
            USE_VIRTUAL_TILES,
            REQUIRE_SNEAK_CLICK_BREAK_SIGN,
            OLD_LIMITATION_CHECKS,

    // Feature booleans.
    USE_ENDERCHEST,
            USE_ENDERCHEST_RESTRICTION,
            USE_OLD_ENDERCHEST,
            USE_ENCHANT_TABLE,
            USE_ENCHANT_MAX_LEVEL_ARGUMENT,
            USE_CRAFTING,
            USE_ANVIL,
            USE_GRINDSTONE,
            USE_LOOM,
            USE_CARTOGRAPHY,
            USE_STONE_CUTTER,
            USE_SMITHING_TABLE,
            USE_FURNACE,
            USE_BLAST_FURNACE,
            USE_SMOKER,
            USE_BREWING,

    // Sound booleans.
    USE_ANVIL_SOUNDS,
            USE_CRAFTING_SOUNDS,
            USE_ENCHANT_TABLE_SOUNDS,
            USE_ENDERCHEST_SOUNDS,
            USE_GRINDSTONE_SOUNDS,
            USE_LOOM_SOUNDS,
            USE_CARTOGRAPHY_SOUNDS,
            USE_STONE_CUTTER_SOUNDS,
            USE_SMITHING_TABLE_SOUNDS,

    // Sign booleans.
    USE_ANVIL_SIGN,
            USE_BLAST_FURNACE_SIGN,
            USE_BREWING_STAND_SIGN,
            USE_CARTOGRAPHY_SIGN,
            USE_CRAFTING_SIGN,
            USE_ENDERCHEST_SIGN,
            USE_ENCHANT_TABLE_SIGN,
            USE_FURNACE_SIGN,
            USE_GRINDSTONE_SIGN,
            USE_LOOM_SIGN,
            USE_STONE_CUTTER_SIGN,
            USE_SMITHING_SIGN,
            USE_SMOKER_SIGN;

    // The interval in which we should check for an update for the plugin.
    public static int UPDATE_CHECK_INTERVAL,
    // The max level of enchantment that should be allowed in Enchantment tables.
    ENCHANT_MAX_LEVEL;

    public static String
            // The sounds that are played when opening a specific inventory.
            ENDER_CHEST_OPEN_SOUND,
            ENCHANT_TABLE_OPEN_SOUND,
            CRAFTING_OPEN_SOUND,
            ANVIL_OPEN_SOUND,
            GRINDSTONE_OPEN_SOUND,
            LOOM_OPEN_SOUND,
            CARTOGRAPHY_OPEN_SOUND,
            STONE_CUTTER_OPEN_SOUND,
            SMITHING_TABLE_OPEN_SOUND;

    public static String STORAGE_MEDIUM, DATABASE_ADDRESS, DATABASE_USER, DATABASE_PASSWORD, DATABASE_DATABASE;
    @Untainted
    public static String DATABASE_PREFIX;
    public static int POOL_MAX_SIZE, POOL_MIN_CONNECTIONS, POOL_MAX_LIFE, POOL_KEEP_ALIVE, POOL_CONNECTION_TIMEOUT;
    public static Map<String, Object> PROPERTIES;

    @Getter
    @Setter(value = AccessLevel.PRIVATE)
    private static Settings instance;

    public Settings(String fileName, final StorageMedium current) {
        super(fileName);

        CURRENT_MEDIUM = current;

        setInstance(this);
    }

    public static void init() {
        new Settings("settings.yml", CURRENT_MEDIUM).onLoad();
        Utils.debugLog("Loaded the settings.yml file.");
    }

    private void onLoad() {
        PROPERTIES = new HashMap<>();

        /* Features */
        SILENT_START_UP = getBoolean("silent-start");
        ATTEMPT_MIGRATION_AT_START = getBoolean("attempt-migration-at-start");
        DEBUG = getBoolean("debug");
        UPDATE_CHECK_INTERVAL = getInt("update-check-interval");
        USE_PERMISSIONS = getBoolean("use-permissions");
        USE_MESSAGES = getBoolean("use-messages");

        STORAGE_MEDIUM = getString("method");

        if (CURRENT_MEDIUM == null) {
            CURRENT_MEDIUM = StorageMedium.valueOf(Objects.requireNonNullElse(STORAGE_MEDIUM, "YAML").toUpperCase());
        }

        DATABASE_ADDRESS = getString("database.address");
        DATABASE_USER = getString("database.username");
        DATABASE_PASSWORD = getString("database.password");
        DATABASE_DATABASE = getString("database.database");

        DATABASE_PREFIX = getString("database.table-prefix");

        POOL_MAX_SIZE = getInt("database.pool-config.maximum-pool-size");
        POOL_MIN_CONNECTIONS = getInt("database.pool-config.minimum-idle-connections");
        POOL_MAX_LIFE = getInt("database.pool-config.maximum-lifetime");
        POOL_KEEP_ALIVE = getInt("database.pool-config.keep-alive-timeout");
        POOL_CONNECTION_TIMEOUT = getInt("database.pool-config.connection-timeout");

        PROPERTIES = getConfigurationSection("database.pool-config.properties").getValues(true);

        USE_VIRTUAL_TILES = getBoolean("use-virtual-tiles");
        OLD_LIMITATION_CHECKS = getBoolean("old-limitation-checks");
        USE_FURNACE = getBoolean("use-furnace");
        USE_BLAST_FURNACE = getBoolean("use-blast-furnace");
        USE_SMOKER = getBoolean("use-smoker");
        USE_BREWING = getBoolean("use-brewing");

        USE_TOO_MANY_ARGS = getBoolean("use-too-many-args");
        USE_HELP_IF_WRONG_ARGS = getBoolean("use-help-if-no-args");
        USE_UPDATER = getBoolean("use-updater");
        USE_METRICS = getBoolean("metrics");
        USE_CRAFTING = getBoolean("use-crafting");
        USE_ENDERCHEST = getBoolean("use-enderchest");
        USE_OLD_ENDERCHEST = getBoolean("old-enderchest");
        USE_ENCHANT_TABLE = getBoolean("use-enchanttable");
        USE_ENCHANT_MAX_LEVEL_ARGUMENT = getBoolean("allow-max-level-argument");
        ENCHANT_MAX_LEVEL = getInt("maximum-enchant-level");
        USE_ANVIL = getBoolean("use-anvil");
        USE_ENDERCHEST_RESTRICTION = getBoolean("ender-chest-restrictions");
        USE_GRINDSTONE = getBoolean("use-grindstone");
        USE_LOOM = getBoolean("use-loom");
        USE_CARTOGRAPHY = getBoolean("use-cartography");
        USE_STONE_CUTTER = getBoolean("use-stonecutter");
        USE_SMITHING_TABLE = getBoolean("use-smithing-table");

        /* Sound Booleans */
        USE_ANVIL_SOUNDS = getBoolean("use-anvil-sounds");
        USE_CRAFTING_SOUNDS = getBoolean("use-crafting-sounds");
        USE_ENCHANT_TABLE_SOUNDS = getBoolean("use-enchanttable-sounds");
        USE_ENDERCHEST_SOUNDS = getBoolean("use-enderchest-sounds");
        USE_GRINDSTONE_SOUNDS = getBoolean("use-grindstone-sounds");
        USE_LOOM_SOUNDS = getBoolean("use-loom-sounds");
        USE_CARTOGRAPHY_SOUNDS = getBoolean("use-cartography-sounds");
        USE_STONE_CUTTER_SOUNDS = getBoolean("use-stonecutter-sounds");
        USE_SMITHING_TABLE_SOUNDS = getBoolean("use-smithing-table-sounds");

        /* Sounds */
        ENDER_CHEST_OPEN_SOUND = getString("enderchest-open-sound");
        ENCHANT_TABLE_OPEN_SOUND = getString("enchanttable-open-sound");
        CRAFTING_OPEN_SOUND = getString("crafting-open-sound");
        ANVIL_OPEN_SOUND = getString("anvil-open-sound");
        GRINDSTONE_OPEN_SOUND = getString("grindstone-open-sound");
        LOOM_OPEN_SOUND = getString("loom-open-sound");
        CARTOGRAPHY_OPEN_SOUND = getString("cartography-open-sound");
        STONE_CUTTER_OPEN_SOUND = getString("stonecutter-open-sound");
        SMITHING_TABLE_OPEN_SOUND = getString("smithing-open-sound");

        USE_RANDOM_SOUND_PITCH = getBoolean("use-random-sound-pitch");

        /* Sign Stuff */
        USE_SIGNS = getBoolean("use-signs");
        REQUIRE_SNEAK_CLICK_BREAK_SIGN = getBoolean("require-shift-click-to-break-signs");

        USE_ANVIL_SIGN = getBoolean("use-anvil-sign");
        USE_BLAST_FURNACE_SIGN = getBoolean("use-blast-furnace-sign");
        USE_BREWING_STAND_SIGN = getBoolean("use-brewing-sign");
        USE_CARTOGRAPHY_SIGN = getBoolean("use-cartography-sign");
        USE_CRAFTING_SIGN = getBoolean("use-crafting-sign");
        USE_ENDERCHEST_SIGN = getBoolean("use-enderchest-sign");
        USE_ENCHANT_TABLE_SIGN = getBoolean("use-enchanttable-sign");
        USE_FURNACE_SIGN = getBoolean("use-furnace-sign");
        USE_GRINDSTONE_SIGN = getBoolean("use-grindstone-sign");
        USE_LOOM_SIGN = getBoolean("use-loom-sign");
        USE_STONE_CUTTER_SIGN = getBoolean("use-stonecutter-sign");
        USE_SMITHING_SIGN = getBoolean("use-smithingtable-sign");
        USE_SMOKER_SIGN = getBoolean("use-smoker-sign");

        Utils.setDebugMode(DEBUG);
    }

    public void reload() {
        setInstance(null);
        init();

        // Update the settings instance for the manager.
        if (PortableCraftingInvsPlugin.getInstance().getManager() != null) {
            PortableCraftingInvsPlugin.getInstance().getManager().setSettings(this);
        }

        if (StorageMedium.valueOf(STORAGE_MEDIUM.toUpperCase()) != CURRENT_MEDIUM) {
            Utils.logError("Hey! Just so you're aware you cannot change the storage medium during runtime, please restart the server for that change to take effect.");
            Utils.logError("After switching over you may migrate your data to your new medium by running '/pci migrate'.");
        }

        Utils.debugLog("Reloaded the settings.yml file.");
    }

    @Override
    public int maximumFurnaces() {
        return getInt("maximum-furnaces", 3);
    }

    @Override
    public int maximumBrewingStands() {
        return getInt("maximum-brewing-stands", 3);
    }

    @Override
    public boolean useFurnaces() {
        return USE_VIRTUAL_TILES && USE_FURNACE;
    }

    @Override
    public boolean useBrewingStands() {
        if (PortableCraftingInvsPlugin.isPaperServer()) {
            try {
                final Method minecraftVersion = Server.class.getDeclaredMethod("getMinecraftVersion");
                final String ver = (String) minecraftVersion.invoke(Bukkit.getServer());

                if (!ver.contains("1.21") && !ver.equalsIgnoreCase("1.20.6")) {
                    Utils.logWarning("!!! ATTENTION !!! Due to breaking changes (and the goal to only actively support the latest Minecraft) the brewing stands cannot be used. Please update to a Minecraft 1.21.1 to continue using them.");
                    return false;
                }
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                Utils.logError(e);
                Utils.logError("Failed to parse minecraft version!");
                Utils.logWarning("!!! ATTENTION !!! Due to breaking changes (and the goal to only actively support the latest Minecraft) the brewing stands cannot be used. Please update to a Minecraft 1.21.1 to continue using them.");
                return false;
            }
        } else {
            if (!PortableCraftingInvsPlugin.getServerVersion().contains("v1_21") && !PortableCraftingInvsPlugin.getServerVersion().equalsIgnoreCase("v1_20_R4")) {
                Utils.logWarning("!!! ATTENTION !!! Due to breaking changes (and the goal to only actively support the latest Minecraft) the brewing stands cannot be used. Please update to a Minecraft 1.21.1 to continue using them.");
                return false;
            }
        }



        return USE_VIRTUAL_TILES && USE_BREWING;
    }

    @Override
    public boolean oldLimitationChecks() {
        return OLD_LIMITATION_CHECKS;
    }


}
