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
 * Default properties for a furnace.
 */
public class FurnaceProperties extends Properties implements ConfigurationSerializable {

    public static final FurnaceProperties FURNACE = new FurnaceProperties("furnace");

    /**
     * The multiplier for cooking.
     */
    @Getter
    private final double cookMultiplier;

    /**
     * "Creates" a new {@link FurnaceProperties} with the default values of 1 and 1.
     *
     * @param name           The name of the property.
     * @param cookMultiplier The multiplier for smelting.
     */
    public FurnaceProperties(String name, double cookMultiplier) {
        super(Utils.getKey(name));

        this.cookMultiplier = cookMultiplier;
    }

    /**
     * "Creates" a new {@link FurnaceProperties} with the default values of 1.0.
     *
     * @param name The name of the property.
     */
    public FurnaceProperties(String name) {
        this(name, 1.0);
    }

    /**
     * Gets a property if one is created.
     *
     * @param key The key for this property.
     * @return Returns null or the {@link FurnaceProperties} instance.
     */
    private static @Nullable FurnaceProperties getProperty(String key) {
        for (final NamespacedKey namespacedKey : KEY_MAP.keySet()) {
            if (namespacedKey.getKey().equalsIgnoreCase(key)) {
                return (FurnaceProperties) KEY_MAP.get(namespacedKey);
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
        result.put("cookMultiplier", this.cookMultiplier);
        return result;
    }

    /**
     * Deserializes serialized data from a configuration file.
     *
     * @param args The keys and such.
     * @return Returns {@link FurnaceProperties}.
     */
    public static FurnaceProperties deserialize(@NotNull Map<String, Object> args) {
        final String stringKey = ((String) args.get("key")).split(":")[1];
        final double cook = (double) args.get("cookMultiplier");
        final FurnaceProperties furnaceProperties = getProperty(stringKey);
        return Objects.requireNonNullElseGet(furnaceProperties, () -> new FurnaceProperties(stringKey, cook));
    }

    @Override
    public String toString() {
        return "FurnaceProperties{" +
                "cookMultiplier=" + cookMultiplier +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FurnaceProperties that = (FurnaceProperties) o;
        return Double.compare(cookMultiplier, that.cookMultiplier) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cookMultiplier);
    }
}
