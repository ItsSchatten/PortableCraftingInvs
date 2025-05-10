package com.itsschatten.portablecrafting.virtual.recipe;

import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.virtual.utils.FurnaceType;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * A furnace recipe, responsible for storing a recipe that goes into a furnace.
 */
@Getter
public class FurnaceRecipe extends Recipe {

    /**
     * The ingredient.
     */
    private final ItemStack ingredient;

    /**
     * A {@link Tag} for the ingredients.
     */
    private final List<ItemStack> ingredients;

    /**
     * How long it takes for this recipe to cook.
     */
    private final int cookTime;
    /**
     * The experience dropped from claiming this recipe.
     */
    private final float experience;

    /**
     * The type of furnace this recipe can be cooked in.
     */
    private final FurnaceType type;

    /**
     * "Creates" a new FurnaceRecipe.
     *
     * @param name        The name of this recipe, used in the {@link org.bukkit.NamespacedKey}
     * @param result      The result of this recipe, as an {@link ItemStack}.
     * @param ingredient  The ingredient for this recipe.
     * @param ingredients A tag that represents an ingredient for this recipe.
     * @param cookTime    How long this recipe takes to smelt.
     * @param experience  The amount of experience given for this recipe.
     * @param type        The type of furnace this recipe can be used in, all recipes inherit the {@link FurnaceType#FURNACE} type.
     */
    public FurnaceRecipe(String name, @NotNull ItemStack result, ItemStack ingredient, List<ItemStack> ingredients, int cookTime, float experience, FurnaceType type) {
        super(Utils.getKey("furnace_recipe_" + name), result);
        this.ingredient = ingredient;
        this.ingredients = ingredients == null ? null : List.copyOf(ingredients);
        this.cookTime = cookTime;
        this.experience = experience;
        this.type = type;
    }

    /**
     * "Creates" a new FurnaceRecipe.
     *
     * @param name       The name of this recipe, used in the {@link org.bukkit.NamespacedKey}
     * @param result     The result of this recipe, as {@link Material}. This is converted to an {@link ItemStack}.
     * @param ingredient The ingredient for this recipe.
     * @param cookTime   How long this recipe takes to smelt.
     * @param experience The amount of experience given for this recipe.
     * @param type       The type of furnace this recipe can be used in, all recipes inherit the {@link FurnaceType#FURNACE} type.
     */
    public FurnaceRecipe(String name, @NotNull ItemStack result, ItemStack ingredient, int cookTime, float experience, FurnaceType type) {
        this(name, new ItemStack(result), ingredient, null, cookTime, experience, type);
    }


    /**
     * "Creates" a new FurnaceRecipe.
     *
     * @param name        The name of this recipe, used in the {@link org.bukkit.NamespacedKey}
     * @param result      The result of this recipe, as {@link Material}. This is converted to an {@link ItemStack}.
     * @param ingredients A tag that represents an ingredient for this recipe.
     * @param cookTime    How long this recipe takes to smelt.
     * @param experience  The amount of experience given for this recipe.
     * @param type        The type of furnace this recipe can be used in, all recipes inherit the {@link FurnaceType#FURNACE} type.
     */
    public FurnaceRecipe(String name, @NotNull Material result, List<ItemStack> ingredients, int cookTime, float experience, FurnaceType type) {
        this(name, new ItemStack(result), null, ingredients, cookTime, experience, type);
    }

    /**
     * "Creates" a new FurnaceRecipe.
     *
     * @param name        The name of this recipe, used in the {@link org.bukkit.NamespacedKey}
     * @param result      The result of this recipe, as an {@link ItemStack}.
     * @param ingredients A tag that represents an ingredient for this recipe.
     * @param cookTime    How long this recipe takes to smelt.
     * @param experience  The amount of experience given for this recipe.
     * @param type        The type of furnace this recipe can be used in, all recipes inherit the {@link FurnaceType#FURNACE} type.
     */
    public FurnaceRecipe(String name, @NotNull ItemStack result, List<ItemStack> ingredients, int cookTime, float experience, FurnaceType type) {
        this(name, result, null, ingredients, cookTime, experience, type);
    }

    @Override
    public String toString() {
        return "FurnaceRecipe{" +
                "ingredient=" + ingredient +
                ", ingredientTag=" + ingredients +
                ", cookTime=" + cookTime +
                ", experience=" + experience +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FurnaceRecipe that = (FurnaceRecipe) o;
        return getCookTime() == that.getCookTime() && Float.compare(getExperience(), that.getExperience()) == 0 && getIngredient() == that.getIngredient() && Objects.equals(getIngredients(), that.getIngredients()) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIngredient(), getIngredients(), getCookTime(), getExperience(), type);
    }
}
