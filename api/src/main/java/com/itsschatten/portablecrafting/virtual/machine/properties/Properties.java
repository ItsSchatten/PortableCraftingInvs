package com.itsschatten.portablecrafting.virtual.machine.properties;

import lombok.Getter;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.Map;

/**
 * Base properties of which all properties are made from.
 */
public abstract class Properties implements Keyed {
    static final Map<NamespacedKey, Properties> KEY_MAP = new HashMap<>();

    /**
     * {@link NamespacedKey} associated with this property.
     */
    @Getter
    final NamespacedKey key;

    /**
     * @param key The key for this property.
     */
    Properties(NamespacedKey key) {
        this.key = key;
        KEY_MAP.put(key, this);
    }

}
