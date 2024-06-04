package com.itsschatten.portablecrafting.virtual.machine.properties;

import com.itsschatten.libs.Utils;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Default properties for a brewing stand.
 */
public class BrewingProperties extends Properties implements ConfigurationSerializable {

    public static final BrewingProperties NORMAL = new BrewingProperties("brewing");

    /**
     * Multiplier for brewing
     */
    @Getter
    private final double brewMultiplier;

    /**
     * Creates the default {@link BrewingProperties}.
     *
     * @param key The key for this property.
     */
    public BrewingProperties(String key) {
        super(Utils.getKey(key));
        this.brewMultiplier = 1.0;
    }

    /**
     * @param key The key for this property.
     */
    public BrewingProperties(String key, double brewMultiplier) {
        super(Utils.getKey(key));
        this.brewMultiplier = brewMultiplier;
    }

    /**
     * Gets a property if one is created.
     *
     * @param key The key for this property.
     * @return Returns null or the {@link FurnaceProperties} instance.
     */
    private static @Nullable BrewingProperties getProperty(String key) {
        for (final NamespacedKey namespacedKey : KEY_MAP.keySet()) {
            if (namespacedKey.getKey().equalsIgnoreCase(key)) {
                return (BrewingProperties) KEY_MAP.get(namespacedKey);
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
        Map<String, Object> serialized = new LinkedHashMap<>();
        serialized.put("key", this.getKey().toString());
        serialized.put("brewMultiplier", getBrewMultiplier());
        return serialized;
    }

    /**
     * Deserializes serialized data from a configuration file.
     *
     * @param args The keys and such.
     * @return Returns {@link FurnaceProperties}.
     */
    public static BrewingProperties deserialize(@NotNull Map<String, Object> args) {
        final String stringKey = ((String) args.get("key")).split(":")[1];
        final double cook = (double) args.get("brewMultiplier");
        final BrewingProperties furnaceProperties = getProperty(stringKey);
        return Objects.requireNonNullElseGet(furnaceProperties, () -> new BrewingProperties(stringKey, cook));
    }

    @Override
    public String toString() {
        return "BrewingProperties{" +
                "brewMultiplier=" + brewMultiplier +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrewingProperties that = (BrewingProperties) o;
        return Double.compare(getBrewMultiplier(), that.getBrewMultiplier()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getBrewMultiplier());
    }
}
