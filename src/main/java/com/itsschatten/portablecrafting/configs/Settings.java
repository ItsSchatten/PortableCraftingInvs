package com.itsschatten.portablecrafting.configs;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.configutils.SimpleConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Sets values to be used throughout the plugin.
 */
public class Settings extends SimpleConfig {

    public static boolean
            DEBUG,
            USE_UPDATER,
            USE_METRICS,
            USE_TOO_MANY_ARGS,
            USE_HELP_IF_WRONG_ARGS,
            USE_RANDOM_SOUND_PITCH,

    USE_ENDERCHEST,
            USE_ENDERCHEST_RESTRICTION,
            USE_ENCHANTTABLE,
            USE_CRAFTING,
            USE_ANVIL,

    USE_ANVIL_SOUNDS,
            USE_CRAFTING_SOUNDS,
            USE_ENCHANTTABLE_SOUNDS,
            USE_ENDERCHEST_SOUNDS;

    public static String
            ENDER_CHEST_OPEN_SOUND,
            ENCHANTTABLE_OPEN_SOUND,
            CRAFTING_OPEN_SOUND,
            ANVIL_OPEN_SOUND;

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
                "(https://github.com/ItsSchatten/PortableCraftingInvs)",
                "--------------------------------------------------------"});

        setInstance(this);
    }

    public static void init() {
        new Settings("settings.yml").onLoad();
        Utils.debugLog(DEBUG, "Loaded the settings.yml file.");
    }

    private void onLoad() {
        /* Features */
        DEBUG = (boolean) get("debug");
        USE_TOO_MANY_ARGS = (boolean) get("use-too-many-args");
        USE_HELP_IF_WRONG_ARGS = (boolean) get("use-help-if-no-args");
        USE_UPDATER = (boolean) get("use-updater");
        USE_METRICS = (boolean) get("metrics");
        USE_CRAFTING = (boolean) get("use-crafting");
        USE_ENDERCHEST = (boolean) get("use-enderchest");
        USE_ENCHANTTABLE = (boolean) get("use-enchanttable");
        USE_ANVIL = (boolean) get("use-anvil");
        USE_ENDERCHEST_RESTRICTION = (boolean) get("ender-chest-restrictions");
        /* Sound Booleans */
        USE_ANVIL_SOUNDS = (boolean) get("use-anvil-sounds");
        USE_CRAFTING_SOUNDS = (boolean) get("use-crafting-sounds");
        USE_ENCHANTTABLE_SOUNDS = (boolean) get("use-enchanttable-sounds");
        USE_ENDERCHEST_SOUNDS = (boolean) get("use-enderchest-sounds");

        /* Sounds */
        ENDER_CHEST_OPEN_SOUND = getString("enderchest-open-sound");
        ENCHANTTABLE_OPEN_SOUND = getString("enchanttable-open-sound");
        CRAFTING_OPEN_SOUND = getString("crafting-open-sound");
        ANVIL_OPEN_SOUND = getString("anvil-open-sound");
        USE_RANDOM_SOUND_PITCH = (boolean) get("use-random-sound-pitch");

    }

    public void reload() {
        setInstance(null);

        init();
        Utils.debugLog(Settings.DEBUG, "Reloaded the settings.yml file.");
    }

}
