package com.itsschatten.portablecrafting.configs;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.configutils.SimpleConfig;
import lombok.Getter;

public class SignsConfig extends SimpleConfig {

    @Getter
    private static SignsConfig instance;

    public SignsConfig(String fileName) {
        super(fileName);
        instance = this;
    }

    public static void init() {
        new SignsConfig("signs.yml");
    }

    public void reload() {
        instance = null;
        init();
        Utils.debugLog( "Reloaded signs.yml");
    }

}
