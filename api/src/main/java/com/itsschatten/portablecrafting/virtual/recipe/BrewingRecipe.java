package com.itsschatten.portablecrafting.virtual.recipe;

import com.itsschatten.libs.Utils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Objects;

/**
 * A brewing recipe, used in a brewing stand.
 */
@Getter
public class BrewingRecipe extends Recipe {

    /**
     * The ingredient for this potion.
     */
    private final ItemStack ingredient;
    /**
     * The input potion.
     */
    private final ItemStack inputBottle;

    /**
     * How long it takes to brew this potion, defaults to 400 ticks.
     */
    private final int brewTime;

    /**
     * "Creates" a new {@link BrewingRecipe}.
     *
     * @param name        The name of this recipe, used in the {@link org.bukkit.NamespacedKey} later.
     * @param result      The result of this recipe.
     * @param ingredient  The ingredient for this recipe, as an {@link ItemStack}
     * @param inputBottle The input bottle, as an {@link ItemStack}.
     * @param brewTime    The time it takes to brew this potion.
     */
    public BrewingRecipe(String name, @NotNull ItemStack result, ItemStack ingredient, ItemStack inputBottle, int brewTime) {
        super(Utils.getKey("brewing_recipe_" + name), result);
        this.ingredient = ingredient;
        this.inputBottle = inputBottle;
        this.brewTime = brewTime;
    }

    /**
     * "Creates" a new {@link BrewingRecipe}, defaults to 400 ticks for the brew time.
     *
     * @param name        The name of this recipe, used in the {@link org.bukkit.NamespacedKey} later.
     * @param result      The result of this recipe.
     * @param ingredient  The ingredient for this recipe, as a {@link Material}
     * @param inputBottle The input bottle, as an {@link ItemStack}.
     */
    public BrewingRecipe(String name, ItemStack result, Material ingredient, ItemStack inputBottle) {
        this(name, result, new ItemStack(ingredient), inputBottle, 400);
    }

    /**
     * "Creates" a new {@link BrewingRecipe}, defaults to 400 ticks for the brew time.
     *
     * @param name        The name of this recipe, used in the {@link org.bukkit.NamespacedKey} later.
     * @param result      The result of this recipe.
     * @param ingredient  The ingredient for this recipe, as an {@link ItemStack}
     * @param inputBottle The input bottle, as an {@link ItemStack}.
     */
    public BrewingRecipe(String name, ItemStack result, ItemStack ingredient, ItemStack inputBottle) {
        this(name, result, ingredient, inputBottle, 400);
    }

    /**
     * Creates a new potion recipe.
     *
     * @param name       The name of the potion.
     * @param input      The {@link PotionType}.
     * @param ingredient The {@link Material ingredient} to craft this potion.
     * @param output     The {@link PotionType} for the output potion.
     * @return Returns a new {@link BrewingRecipe}.
     */
    @Contract("_, _, _, _ -> new")
    public static @NotNull BrewingRecipe get(String name, Material ingredient, PotionType input, PotionType output) {
        return new BrewingRecipe(name, makeBottle(Material.POTION, output), ingredient, makeBottle(Material.POTION, input));
    }

    /**
     * Creates a new splash potion recipe.
     *
     * @param name  The name of the potion.
     * @param input The {@link PotionType}.
     * @return Returns a new {@link BrewingRecipe}.
     */
    @Contract("_, _ -> new")
    public static @NotNull BrewingRecipe getSplash(String name, PotionType input) {
        return new BrewingRecipe("splash_" + name, makeBottle(Material.SPLASH_POTION, input), Material.GUNPOWDER, makeBottle(Material.POTION, input));
    }

    /**
     * Creates a new lingering potion recipe.
     *
     * @param name  The name of the potion.
     * @param input The {@link PotionType}.
     * @return Returns a new {@link BrewingRecipe}.
     */
    @Contract("_, _ -> new")
    public static @NotNull BrewingRecipe getLingering(String name, PotionType input) {
        return new BrewingRecipe("lingering_" + name, makeBottle(Material.LINGERING_POTION, input), Material.DRAGON_BREATH, makeBottle(Material.SPLASH_POTION, input));
    }

    /**
     * Returns all the potion types that can be crafted.
     *
     * @param name       The name of the potion.
     * @param ingredient The ingredient for the base potion.
     * @param input      The {@link PotionType} of the base bottle.
     * @param output     The {@link PotionType} of the output bottle.
     * @return Returns a list containing all valid potions.
     */
    @Contract("_, _, _, _ -> new")
    public static @Unmodifiable List<BrewingRecipe> getAllRecipes(String name, Material ingredient, PotionType input, PotionType output) {
        return List.of(new BrewingRecipe[]{
                get(name, ingredient, input, output),
                getSplash(name, output),
                getLingering(name, output)
        });
    }

    /**
     * Quickly makes a potion.
     *
     * @param material The {@link Material potion bottle material}, this method <b>DOES NOT</b> check if the material is a valid potion.
     * @param type     The {@link PotionType} to set as the potion.
     * @return Returns a new {@link ItemStack} with the {@link PotionType}.
     */
    private static @NotNull ItemStack makeBottle(final Material material, final PotionType type) {
        final ItemStack bottle = new ItemStack(material);
        final PotionMeta bottleMeta = (PotionMeta) bottle.getItemMeta();
        // This shouldn't be null, mainly here to satisfy the IDE.
        assert bottleMeta != null;
        bottleMeta.setBasePotionType(type);
        bottleMeta.clearCustomEffects();
        bottle.setItemMeta(bottleMeta);

        return bottle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrewingRecipe recipe = (BrewingRecipe) o;
        return getBrewTime() == recipe.getBrewTime() && Objects.equals(getIngredient(), recipe.getIngredient()) && Objects.equals(getInputBottle(), recipe.getInputBottle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIngredient(), getInputBottle(), getBrewTime());
    }

    @Override
    public String toString() {
        return "BrewingRecipe{" +
                "ingredient=" + ingredient +
                ", inputBottle=" + inputBottle +
                ", brewTime=" + brewTime +
                "} " + super.toString();
    }
}
