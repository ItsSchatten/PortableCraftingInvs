package com.itsschatten.portablecrafting.virtual.fuel;

import lombok.Getter;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * Represents the base Fuel.
 */
@Getter
public abstract class Fuel implements Keyed {

    /**
     * The key for this fuel, used to store it in a map.
     */
    final NamespacedKey key;
    /**
     * The ItemStack that is considered fuel.
     */
    final ItemStack fuelItem;
    /**
     * A material tag used for the fuel. If it matches its fuel.
     */
    final Tag<Material> tag;

    /**
     * @param key      The {@link NamespacedKey}.
     * @param fuelItem The {@link ItemStack} to use as the Fuel item, used in comparisons.
     * @param tag      The {@link Tag<Material>} for the fuel, used in comparisons.
     */
    public Fuel(NamespacedKey key, ItemStack fuelItem, Tag<Material> tag) {
        this.key = key;
        // Ensure the fuel item only ever has 1 item.
        if (fuelItem != null && fuelItem.getAmount() > 1) {
            fuelItem.setAmount(1);
        }
        this.fuelItem = fuelItem;
        this.tag = tag;
    }

    /**
     * Check if an {@link ItemStack} matches the fuel item.
     *
     * @param item The {@link ItemStack} to check.
     * @return <code>true</code> if the ItemStack matches {@link #fuelItem} or matches the {@link #tag}
     */
    public final boolean matches(ItemStack item) {
        if (item == null) return false;
        final ItemStack clone = item.clone();
        clone.setAmount(1);

        if (this.fuelItem != null && this.fuelItem.equals(clone)) {
            return true;
        } else return tag != null && tag.isTagged(item.getType());
    }

    /**
     * Check if a {@link Material} matches the fuel item.
     *
     * @param material The {@link Material} to check.
     * @return <code>true</code> if the Material matches {@link #fuelItem} or matches the {@link #tag}
     */
    public final boolean matches(Material material) {
        if (material == null) return false;
        if (this.fuelItem != null && this.fuelItem.getType() == material) {
            return true;
        } else return tag != null && tag.isTagged(material);
    }

    @Override
    public String toString() {
        return "Fuel{" +
                "key=" + key +
                ", fuelItem=" + fuelItem +
                ", tag=" + tag +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fuel fuel = (Fuel) o;
        return Objects.equals(getKey(), fuel.getKey()) && Objects.equals(getFuelItem(), fuel.getFuelItem()) && Objects.equals(getTag(), fuel.getTag());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getFuelItem(), getTag());
    }
}
