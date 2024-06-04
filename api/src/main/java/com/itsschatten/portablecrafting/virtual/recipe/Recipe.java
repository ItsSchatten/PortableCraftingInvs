package com.itsschatten.portablecrafting.virtual.recipe;

import lombok.Getter;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Getter
public abstract class Recipe implements Keyed {

    /**
     * The key for this recipe.
     */
    final NamespacedKey key;

    /**
     * The result of this recipe.
     */
    final ItemStack result;

    /**
     * "Creates" a new recipe.
     *
     * @param key    The key to use for storage.
     * @param result What the recipe produces, as an {@link ItemStack}.
     */
    public Recipe(final NamespacedKey key, @NotNull final ItemStack result) {
        this.key = key;
        this.result = result;
    }

    /**
     * "Creates" a new recipe.
     *
     * @param key    The key to use for storage.
     * @param result What the recipe produces, as a {@link Material}.
     */
    public Recipe(final NamespacedKey key, @NotNull final Material result) {
        this.key = key;
        this.result = new ItemStack(result);
    }

    /**
     * @return Returns a clone of {@link #result}.
     */
    public ItemStack getResult() {
        return result.clone();
    }

    /**
     * Return's the {@link #result} as it's underlying {@link Material}.
     *
     * @return Returns a non-null {@link Material} based on the {@link #result}
     */
    public final @NotNull Material getMaterialResult() {
        return result.getType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(getKey(), recipe.getKey()) && Objects.equals(getResult(), recipe.getResult());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getResult());
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "key=" + key +
                ", result=" + result +
                '}';
    }
}
