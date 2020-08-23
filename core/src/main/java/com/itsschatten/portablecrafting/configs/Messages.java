package com.itsschatten.portablecrafting.configs;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.configutils.SimpleConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Sets the messages to be used throughout the plugin.
 */
public class Messages extends SimpleConfig {

    public static String
            PREFIX,
            NO_PERMS,
            HELP_MESSAGE,
            RELOAD_MESSAGE,
            RELOAD_SPECIFIC,
            WRONG_ARGS,
            TOO_MANY_ARGS,
            NOT_ENOUGH_ARGS,
            FEATURE_DISABLED,
            DOES_NOT_EXIST,
            PLAYER_DOES_NOT_EXIST,
            UPDATE_AVAILABLE_MESSAGE,
            OPENED_ENDERCHEST,
            OPEN_TARGET_ENDERCHEST,
            OPENED_CRAFTING,
            OPENED_CRAFTING_OTHER,
            OPENED_ANVIL,
            OPENED_ANVIL_OTHER,
            OPENED_ENCHANT_TABLE,
            OPENED_ENCHANT_TABLE_OTHER,
            MUST_BE_IN_RANGE,
            OPENED_ENCHANT_WITH_MAX_LEVEL,
            OPENED_GRINDSTONE,
            OPENED_GRINDSTONE_OTHER,
            OPENED_LOOM,
            OPENED_LOOM_OTHER,
            OPENED_CARTOGRAPHY,
            OPENED_CARTOGRAPHY_OTHER,
            OPENED_STONE_CUTTER,
            OPENED_STONE_CUTTER_OTHER,
            OPENED_SMITHING_TABLE,
            OPENED_SMITHING_TABLE_OTHER,
            CANT_RETRIEVE_ITEM_FROM_ENDER,
            MUST_SHIFT_CLICK_TO_BREAK_SIGN,
            CANT_USE_SMITHING_IN_1_15,

    OPENED_FURNACE,
            OPENED_FURNACE_OTHER,
            OPENED_BLAST_FURNACE,
            OPENED_BLAST_FURNACE_OTHER,
            OPENED_SMOKER,
            OPENED_SMOKER_OTHER,

    // Sign Messages

    ANVIL_SIGN,
            CARTOGRAPHY_SIGN,
            CRAFTING_SIGN,
            ENCHANT_TABLE_SIGN,
            ENDERCHEST_SIGN,
            GRINDSTONE_SIGN,
            LOOM_SIGN,
            STONE_CUTTER_SIGN,
            SMITHING_TABLE_SIGN,
            ANVIL_SIGN_CREATED,
            CARTOGRAPHY_SIGN_CREATED,
            CRAFTING_SIGN_CREATED,
            ENCHANT_TABLE_SIGN_CREATED,
            ENDERCHEST_SIGN_CREATED,
            GRINDSTONE_SIGN_CREATED,
            LOOM_SIGN_CREATED,
            STONE_CUTTER_SIGN_CREATED,
            SMITHING_TABLE_SIGN_CREATED;
    @Getter
    @Setter(value = AccessLevel.PRIVATE)
    private static Messages instance;

    private Messages(String fileName) {
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
        new Messages("messages.yml").onLoad();
        Utils.setPrefix(PREFIX);
        Utils.setUpdateAvailableMessage(Messages.UPDATE_AVAILABLE_MESSAGE);
        Utils.setNoPermsMessage(NO_PERMS);
        Utils.debugLog(Settings.DEBUG, "Loaded the file messages.yml");
    }

    private void onLoad() {
        PREFIX = getString("prefix");
        NO_PERMS = getString("no-perms");
        HELP_MESSAGE = (String) get("help-message");
        RELOAD_MESSAGE = getString("reload-message");
        RELOAD_SPECIFIC = getString("reload-message-specific");
        WRONG_ARGS = getString("wrong-args");
        TOO_MANY_ARGS = getString("too-many-args");
        NOT_ENOUGH_ARGS = getString("not-enough-args");
        DOES_NOT_EXIST = getString("doesnt-exist");

        PLAYER_DOES_NOT_EXIST = getString("player-doesnt-exist");

        FEATURE_DISABLED = getString("feature-disabled");

        UPDATE_AVAILABLE_MESSAGE = (String) get("update-available");

        OPENED_ENDERCHEST = getString("opened-enderchest");
        OPEN_TARGET_ENDERCHEST = getString("open-target-enderchest");
        OPENED_CRAFTING = getString("opened-craftingtable");
        OPENED_CRAFTING_OTHER = getString("opened-craftingtable-other");
        OPENED_ANVIL = getString("opened-anvil");
        OPENED_ANVIL_OTHER = getString("opened-anvil-other");
        OPENED_ENCHANT_TABLE = getString("opened-enchanttable");
        OPENED_ENCHANT_TABLE_OTHER = getString("opened-enchanttable-other");
        MUST_BE_IN_RANGE = getString("must-be-in-range");
        OPENED_ENCHANT_WITH_MAX_LEVEL = getString("opened-enchanttable-with-max-level");

        OPENED_FURNACE = getString("opened-furnace");
        OPENED_FURNACE_OTHER = getString("opened-furnace-other");
        OPENED_BLAST_FURNACE = getString("opened-blast-furnace");
        OPENED_BLAST_FURNACE_OTHER = getString("opened-blast-furnace-other");
        OPENED_SMOKER = getString("opened-smoker");
        OPENED_SMOKER_OTHER = getString("opened-smoker-other");

        CANT_RETRIEVE_ITEM_FROM_ENDER = getString("cant-retrieve-from-enderchest");
        CANT_USE_SMITHING_IN_1_15 = getString("cant-use-smithing-1-15");

        OPENED_CARTOGRAPHY = getString("opened-cartography");
        OPENED_CARTOGRAPHY_OTHER = getString("opened-cartography-other");


        OPENED_GRINDSTONE = getString("opened-grindstone");
        OPENED_GRINDSTONE_OTHER = getString("opened-grindstone-other");


        OPENED_LOOM = getString("opened-loom");
        OPENED_LOOM_OTHER = getString("opened-loom-other");


        OPENED_STONE_CUTTER = getString("opened-stonecutter");
        OPENED_STONE_CUTTER_OTHER = getString("opened-stonecutter-other");

        OPENED_SMITHING_TABLE = getString("opened-smithing-table");
        OPENED_SMITHING_TABLE_OTHER = getString("opened-smithing-table-other");

        // Sign messages
        MUST_SHIFT_CLICK_TO_BREAK_SIGN = getString("shift-click-to-break-sign");

        ANVIL_SIGN = getString("anvil-sign");
        CARTOGRAPHY_SIGN = getString("cartography-sign");
        CRAFTING_SIGN = getString("crafting-sign");
        ENCHANT_TABLE_SIGN = getString("enchant-table-sign");
        ENDERCHEST_SIGN = getString("enderchest-sign");
        GRINDSTONE_SIGN = getString("grindstone-sign");
        LOOM_SIGN = getString("loom-sign");
        STONE_CUTTER_SIGN = getString("stonecutter-sign");
        SMITHING_TABLE_SIGN = getString("smithing-sign");

        ANVIL_SIGN_CREATED = getString("anvil-sign-created");
        CARTOGRAPHY_SIGN_CREATED = getString("cartography-sign-created");
        CRAFTING_SIGN_CREATED = getString("crafting-sign-created");
        ENCHANT_TABLE_SIGN_CREATED = getString("enchant-table-sign-created");
        ENDERCHEST_SIGN_CREATED = getString("enderchest-sign-created");
        GRINDSTONE_SIGN_CREATED = getString("grindstone-sign-created");
        LOOM_SIGN_CREATED = getString("loom-sign-created");
        STONE_CUTTER_SIGN_CREATED = getString("stonecutter-sign-created");
        SMITHING_TABLE_SIGN_CREATED = getString("smithing-sign-created");
    }

    public void reload() {
        setInstance(null);

        init();
        Utils.debugLog(Settings.DEBUG, "Reloaded the messages.yml file.");
    }


}
