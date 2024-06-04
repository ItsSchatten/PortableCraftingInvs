package com.itsschatten.portablecrafting.virtual.machine.properties;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Default properties for a Smoker.
 */
public class SmokerProperties extends FurnaceProperties {
    public static final SmokerProperties SMOKER = new SmokerProperties("smoker");

    /**
     * Creates the default {@link SmokerProperties}.
     *
     * @param name The name for these properties.
     */
    public SmokerProperties(String name) {
        super(name, 2.0);
    }


    private SmokerProperties(String name, double cookMultiplier) {
        super(name, cookMultiplier);
    }

    /**
     * Gets a property if one is created.
     *
     * @param key The key for this property.
     * @return Returns null or the {@link FurnaceProperties} instance.
     */
    private static @Nullable SmokerProperties getProperty(String key) {
        for (final NamespacedKey namespacedKey : KEY_MAP.keySet()) {
            if (namespacedKey.getKey().equalsIgnoreCase(key)) {
                return (SmokerProperties) KEY_MAP.get(namespacedKey);
            }
        }
        return null;
    }

    /**
     * Serializes this class to something that can be serialized into a yaml config.
     *
     * @return Returns a map of keys and values.
     */
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("key", this.key.toString());
        result.put("cookMultiplier", getCookMultiplier());
        return result;
    }

    /**
     * Deserializes serialized data from a configuration file.
     *
     * @param args The keys and such.
     * @return Returns {@link SmokerProperties}.
     */
    public static SmokerProperties deserialize(@NotNull Map<String, Object> args) {
        final String stringKey = ((String) args.get("key")).split(":")[1];
        final double cook = (double) args.get("cookMultiplier");
        final SmokerProperties furnaceProperties = getProperty(stringKey);
        return Objects.requireNonNullElseGet(furnaceProperties, () -> new SmokerProperties(stringKey, cook));
    }
}
