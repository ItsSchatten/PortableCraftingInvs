package me.itsshadow.portablecrafting.configs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.itsshadow.libs.Utils;
import me.itsshadow.libs.configutils.SimpleConfig;

public class Settings extends SimpleConfig {

    public static boolean
            USE_UPDATER,
            USE_TOO_MANY_ARGS,

            USE_ENDERCHEST,
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
    private static String file = "settings.yml";

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
                " all comments in your file where lost. please open",
                " " + fileName + " directly to browse the default values.",
                " Don't know how to do this? You can also check our github",
                " page for a default file.",
                "--------------------------------------------------------"});

        setInstance(this);
    }

    public static void init() {
        new Settings(file).onLoad();
        Utils.log("&7File &e" + file + "&7 was loaded.");
    }

    private void onLoad() {
        /* Features */
        USE_TOO_MANY_ARGS = (boolean) get("use-too-many-args");
        USE_UPDATER = (boolean) get("use-updater");
        USE_CRAFTING = (boolean) get("use-crafting");
        USE_ENDERCHEST = (boolean) get("use-enderchest");
        USE_ENCHANTTABLE = (boolean) get("use-enchanttable");
        USE_ANVIL = (boolean) get("use-anvil");

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

    }

    public void reload() {
        setInstance(null);

        new Settings(file).onLoad();

        setInstance(this);
        Utils.log("&7The file &e" + file + "&7 has been reloaded.");
    }

}
