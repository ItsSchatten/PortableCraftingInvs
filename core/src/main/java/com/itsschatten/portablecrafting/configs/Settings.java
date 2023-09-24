package com.itsschatten.portablecrafting.configs;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.configutils.SimpleConfig;
import com.itsschatten.portablecrafting.PortableCraftingInvsPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Sets values to be used throughout the plugin.
 */
public class Settings extends SimpleConfig {

    public static boolean
            SILENT_START_UP,
            ATTEMPT_MIGRATION_AT_START,
            DEBUG,
            USE_MYSQL,
            USE_PERMISSIONS,
            USE_UPDATER,
            USE_METRICS,
            USE_MESSAGES,
            USE_TOO_MANY_ARGS,
            ALLOW_ESSENTIALS,
            USE_HELP_IF_WRONG_ARGS,
            USE_RANDOM_SOUND_PITCH,
            USE_SIGNS,
            REQUIRE_SIGHT_CLICK_BREAK_SIGN,

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
            // MYSQL Stuff.
            MYSQL_USER,
            MYSQL_HOST,
            MYSQL_PASS,
            MYSQL_DATABASE,
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

    public static int MYSQL_PORT;

    @Getter
    @Setter(value = AccessLevel.PRIVATE)
    private static Settings instance;

    public Settings(String fileName) {
        super(fileName);

        setHeader(new String[]{
                "--------------------------------------------------------",
                " This configuration file has been automatically updated!",
                "",
                " Unfortunately, due to the way Bukkit saves .yml files,",
                " all comments in your file where lost. To read them,",
                " please open " + fileName + " directly to browse the default values.",
                " Don't know how to do this? You can also check our github",
                " page for the default file.",
                "(https://github.com/itsschatten/portablecraftinginvs/)",
                "--------------------------------------------------------"});

        setInstance(this);
    }

    public static void init() {
        new Settings("settings.yml").onLoad();
        Utils.debugLog("Loaded the settings.yml file.");
    }

    private void onLoad() {
        /* Features */
        SILENT_START_UP = getBoolean("silent-start");
        ATTEMPT_MIGRATION_AT_START = getBoolean("attempt-migration-at-start");
        DEBUG = getBoolean("debug");
        UPDATE_CHECK_INTERVAL = getInt("update-check-interval");
        USE_PERMISSIONS = getBoolean("use-permissions");
        ALLOW_ESSENTIALS = getBoolean("allow-essentials");
        USE_MESSAGES = getBoolean("use-messages");

        USE_MYSQL = getBoolean("use-sql");

        MYSQL_HOST = getString("sql-host");
        MYSQL_DATABASE = getString("sql-database");
        MYSQL_USER = getString("sql-user");
        MYSQL_PASS = getString("sql-pass");
        MYSQL_PORT = getInt("sql-port");

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
        REQUIRE_SIGHT_CLICK_BREAK_SIGN = getBoolean("require-shift-click-to-break-signs");

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
        PortableCraftingInvsPlugin.getFakeContainers().setDebug(Settings.DEBUG);

        if (!USE_MYSQL && (PortableCraftingInvsPlugin.getDatabase() != null && PortableCraftingInvsPlugin.getDatabase().mysql.getConnection() != null)) {
            PortableCraftingInvsPlugin.getDatabase().mysql.close();
            PortableCraftingInvsPlugin.setDatabase(null);
            PortableCraftingInvsPlugin.getFakeContainers().setUsingMysql(Settings.USE_MYSQL);
        }

        Utils.debugLog("Reloaded the settings.yml file.");
    }

}
