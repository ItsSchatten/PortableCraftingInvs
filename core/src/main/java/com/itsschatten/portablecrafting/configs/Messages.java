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

    // TODO: Improve this.

    public static String
            PREFIX,
            NO_PERMS,
            HELP_MESSAGE,
            RELOAD_MESSAGE,
            RELOAD_SPECIFIC,
            WRONG_ARGS,
            TOO_MANY_ARGS,
            NOT_ENOUGH_ARGS,
            DOES_NOT_EXIST,
            PLAYER_DOES_NOT_EXIST,
            UPDATE_AVAILABLE_MESSAGE,
            OPENED_ENDERCHEST,
            OPEN_TARGET_ENDERCHEST_OLD,
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
            YOU,
            BLAST_FURNACE,
            SMOKER,
            FURNACE,
            BREWING_STAND,

    OPENED_FURNACE,
            OPENED_FURNACE_OTHER,
            OPENED_BLAST_FURNACE,
            OPENED_BLAST_FURNACE_OTHER,
            OPENED_SMOKER,
            OPENED_SMOKER_OTHER,
            OPENED_BREWING,
            OPENED_BREWING_OTHER,
            REACHED_MAXIMUM_FEATURE,

    // Sign Messages

    ANVIL_SIGN,
            CARTOGRAPHY_SIGN,
            CRAFTING_SIGN,
            BLAST_FURNACE_SIGN,
            BREWING_SIGN,
            ENCHANT_TABLE_SIGN,
            ENDERCHEST_SIGN,
            FURNACE_SIGN,
            GRINDSTONE_SIGN,
            LOOM_SIGN,
            STONE_CUTTER_SIGN,
            SMITHING_TABLE_SIGN,
            SMOKER_SIGN,
            ANVIL_SIGN_CREATED,
            BREWING_SIGN_CREATED,
            BLAST_FURNACE_SIGN_CREATED,
            CARTOGRAPHY_SIGN_CREATED,
            CRAFTING_SIGN_CREATED,
            ENCHANT_TABLE_SIGN_CREATED,
            ENDERCHEST_SIGN_CREATED,
            FURNACE_SIGN_CREATED,
            GRINDSTONE_SIGN_CREATED,
            LOOM_SIGN_CREATED,
            STONE_CUTTER_SIGN_CREATED,
            SMITHING_TABLE_SIGN_CREATED,
            SMOKER_SIGN_CREATED;
    @Getter
    @Setter(value = AccessLevel.PRIVATE)
    private static Messages instance;

    private Messages(String fileName) {
        super(fileName);

        setInstance(this);
    }

    public static void init() {
        if (!Settings.USE_MESSAGES) {
            new Messages("messages.yml").onLoadNoMessages();
        } else
            new Messages("messages.yml").onLoad();
        Utils.setPrefix(PREFIX);
        Utils.setUpdateAvailableMessage(Messages.UPDATE_AVAILABLE_MESSAGE);
        Utils.setNoPermsMessage(NO_PERMS);
        Utils.debugLog("Loaded the file messages.yml");
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

        YOU = getString("you");
        BLAST_FURNACE = getString("blast-furnace");
        BREWING_STAND = getString("brewing-stand");
        FURNACE = getString("furnace");
        SMOKER = getString("smoker");

        UPDATE_AVAILABLE_MESSAGE = (String) get("update-available");

        OPENED_ENDERCHEST = getString("opened-enderchest");
        OPEN_TARGET_ENDERCHEST_OLD = getString("open-target-enderchest-old");
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

        OPENED_BREWING = getString("opened-brewing");
        OPENED_BREWING_OTHER = getString("opened-brewing-other");

        REACHED_MAXIMUM_FEATURE = getString("reached-maximum-feature");

        CANT_RETRIEVE_ITEM_FROM_ENDER = getString("cant-retrieve-from-enderchest");

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

        BLAST_FURNACE_SIGN = getString("blast-furnace-sign");
        BREWING_SIGN = getString("brewing-sign");
        FURNACE_SIGN = getString("furnace-sign");
        SMOKER_SIGN = getString("smoker-sign");

        ANVIL_SIGN_CREATED = getString("anvil-sign-created");
        CARTOGRAPHY_SIGN_CREATED = getString("cartography-sign-created");
        CRAFTING_SIGN_CREATED = getString("crafting-sign-created");
        ENCHANT_TABLE_SIGN_CREATED = getString("enchant-table-sign-created");
        ENDERCHEST_SIGN_CREATED = getString("enderchest-sign-created");
        GRINDSTONE_SIGN_CREATED = getString("grindstone-sign-created");
        LOOM_SIGN_CREATED = getString("loom-sign-created");
        STONE_CUTTER_SIGN_CREATED = getString("stonecutter-sign-created");
        SMITHING_TABLE_SIGN_CREATED = getString("smithing-sign-created");

        BLAST_FURNACE_SIGN_CREATED = getString("blast-furnace-sign-created");
        BREWING_SIGN_CREATED = getString("brewing-sign-created");
        FURNACE_SIGN_CREATED = getString("furnace-sign-created");
        SMOKER_SIGN_CREATED = getString("smoker-sign-created");
    }

    public void onLoadNoMessages() {
        PREFIX = "";
        NO_PERMS = "";
        HELP_MESSAGE = (String) get("help-message");
        RELOAD_MESSAGE = "";
        RELOAD_SPECIFIC = "";
        WRONG_ARGS = "";
        TOO_MANY_ARGS = "";
        NOT_ENOUGH_ARGS = "";
        DOES_NOT_EXIST = "";

        PLAYER_DOES_NOT_EXIST = "";

        UPDATE_AVAILABLE_MESSAGE = (String) get("update-available");

        YOU = getString("you");
        BLAST_FURNACE = getString("blast-furnace");
        BREWING_STAND = getString("brewing-stand");
        FURNACE = getString("furnace");
        SMOKER = getString("smoker");

        REACHED_MAXIMUM_FEATURE = getString("reached-maximum-feature");

        OPENED_ENDERCHEST = "";
        OPEN_TARGET_ENDERCHEST_OLD = "";
        OPEN_TARGET_ENDERCHEST = "";
        OPENED_CRAFTING = "";
        OPENED_CRAFTING_OTHER = "";
        OPENED_ANVIL = "";
        OPENED_ANVIL_OTHER = "";
        OPENED_ENCHANT_TABLE = "";
        OPENED_ENCHANT_TABLE_OTHER = "";
        MUST_BE_IN_RANGE = "";
        OPENED_ENCHANT_WITH_MAX_LEVEL = "";

        OPENED_FURNACE = "";
        OPENED_FURNACE_OTHER = "";
        OPENED_BLAST_FURNACE = "";
        OPENED_BLAST_FURNACE_OTHER = "";
        OPENED_SMOKER = "";
        OPENED_SMOKER_OTHER = "";

        OPENED_BREWING = "";
        OPENED_BREWING_OTHER = "";

        CANT_RETRIEVE_ITEM_FROM_ENDER = "";

        OPENED_CARTOGRAPHY = "";
        OPENED_CARTOGRAPHY_OTHER = "";

        OPENED_GRINDSTONE = "";
        OPENED_GRINDSTONE_OTHER = "";

        OPENED_LOOM = "";
        OPENED_LOOM_OTHER = "";

        OPENED_STONE_CUTTER = "";
        OPENED_STONE_CUTTER_OTHER = "";

        OPENED_SMITHING_TABLE = "";
        OPENED_SMITHING_TABLE_OTHER = "";

        // Sign messages
        MUST_SHIFT_CLICK_TO_BREAK_SIGN = "";

        ANVIL_SIGN = getString("anvil-sign");
        CARTOGRAPHY_SIGN = getString("cartography-sign");
        CRAFTING_SIGN = getString("crafting-sign");
        ENCHANT_TABLE_SIGN = getString("enchant-table-sign");
        ENDERCHEST_SIGN = getString("enderchest-sign");
        GRINDSTONE_SIGN = getString("grindstone-sign");
        LOOM_SIGN = getString("loom-sign");
        STONE_CUTTER_SIGN = getString("stonecutter-sign");
        SMITHING_TABLE_SIGN = getString("smithing-sign");

        BLAST_FURNACE_SIGN = getString("blast-furnace-sign");
        BREWING_SIGN = getString("brewing-sign");
        FURNACE_SIGN = getString("furnace-sign");
        SMOKER_SIGN = getString("smoker-sign");

        ANVIL_SIGN_CREATED = "";
        CARTOGRAPHY_SIGN_CREATED = "";
        CRAFTING_SIGN_CREATED = "";
        ENCHANT_TABLE_SIGN_CREATED = "";
        ENDERCHEST_SIGN_CREATED = "";
        GRINDSTONE_SIGN_CREATED = "";
        LOOM_SIGN_CREATED = "";
        STONE_CUTTER_SIGN_CREATED = "";
        SMITHING_TABLE_SIGN_CREATED = "";

        BLAST_FURNACE_SIGN_CREATED = "";
        BREWING_SIGN_CREATED = "";
        FURNACE_SIGN_CREATED = "";
        SMOKER_SIGN_CREATED = "";
    }

    public void reload() {
        setInstance(null);

        init();
        Utils.debugLog("Reloaded the messages.yml file.");
    }


}
