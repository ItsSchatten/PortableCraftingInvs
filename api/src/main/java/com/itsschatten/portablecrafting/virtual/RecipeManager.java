package com.itsschatten.portablecrafting.virtual;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.virtual.fuel.BrewingFuel;
import com.itsschatten.portablecrafting.virtual.fuel.FurnaceFuel;
import com.itsschatten.portablecrafting.virtual.recipe.BrewingRecipe;
import com.itsschatten.portablecrafting.virtual.recipe.FurnaceRecipe;
import com.itsschatten.portablecrafting.virtual.utils.FurnaceType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

/**
 * Manager for Recipes.
 */
@Getter
public class RecipeManager {

    /**
     * Instance of this class.
     */
    @Getter
    private static RecipeManager instance;

    // Fuel map, stores the fuel similar to a Registry.
    private final Map<NamespacedKey, FurnaceFuel> furnaceFuelMap;
    // Brewing fuel map, stores the brewing fuel similar to a Registry.
    private final Map<NamespacedKey, BrewingFuel> brewingFuelMap;

    // Furnace recipe map, stores the recipes similar to a Registry.
    private final Multimap<NamespacedKey, FurnaceRecipe> furnaceRecipeMap;
    // Brewing recipe map, stores the recipes similar to a Registry.
    private final Map<NamespacedKey, BrewingRecipe> brewingRecipeMap;

    /**
     * Registers all fuels (custom included) and registers maps
     */
    public RecipeManager() {
        instance = this;
        this.furnaceFuelMap = new HashMap<>();
        this.brewingFuelMap = new HashMap<>();
        this.furnaceRecipeMap = HashMultimap.create();
        this.brewingRecipeMap = new HashMap<>();

        // Load fuels.
        loadFuels();

        // Load brewing and furnace recipes.
        loadFurnaceRecipes();
        loadBrewingRecipes();

        if (Utils.isDebugMode()) {
            logNotRegisteredFuels();
        }
    }

    /**
     * Registers a {@link FurnaceFuel} into a list of valid fuels.
     *
     * @param fuel The {@link FurnaceFuel} to add.
     * @return Returns whether the fuel has been successfully registered.
     */
    // The return value is never inherently used in this project, but is suggested to be used in custom implementations to ensure the fuel is registered.
    public final boolean registerFurnaceFuel(@NotNull FurnaceFuel fuel) {
        if (this.furnaceFuelMap.containsKey(fuel.getKey())) return false;
        this.furnaceFuelMap.put(fuel.getKey(), fuel);
        return true;
    }

    /**
     * Registers a {@link BrewingFuel} into a list of valid fuels.
     *
     * @param fuel The {@link BrewingFuel} to add.
     * @return Returns whether the fuel has been successfully registered.
     */
    // The return value is never inherently used in this project, but is suggested to be used in custom implementations to ensure the fuel is registered.
    public final boolean registerBrewingFuel(@NotNull BrewingFuel fuel) {
        if (this.brewingFuelMap.containsKey(fuel.getKey())) return false;
        this.brewingFuelMap.put(fuel.getKey(), fuel);
        return true;
    }

    /**
     * Mainly used in development to figure out which materials are not registered as valid fuel.
     * <br/><br/>
     * <b><i>This should NOT be used in an environment where debug is not enabled, this must filter every {@link Material} which may cause some server slow down.</i></b>
     */
    public final void logNotRegisteredFuels() {
        Arrays.stream(Material.values()).filter(Material::isFuel)
                .filter((fuel) -> FurnaceFuel.VANILLA_FUELS.stream().filter((furnaceFuel) -> furnaceFuel.matches(fuel)).findAny().isEmpty())
                .forEach((fuel) -> Utils.debugLog("Furnace Fuel not registered: " + fuel));
    }

    /**
     * Loads all vanilla fuels.
     */
    public final void loadFuels() {
        // Brewing Fuel.
        registerBrewingFuel(BrewingFuel.BLAZE_POWDER);

        // Furnace Fuel.
        FurnaceFuel.VANILLA_FUELS.forEach(this::registerFurnaceFuel);
    }

    /**
     * Loads all loaded furnace recipes, this uses Bukkit's recipeIterator, so it should, in theory, function with other Plugins.
     */
    public final void loadFurnaceRecipes() {
        // Gets the Bukkit recipe iterator.
        final Iterator<Recipe> recipes = Bukkit.recipeIterator();

        // Loop it.
        while (recipes.hasNext()) {
            // Next recipe.
            final Recipe recipe = recipes.next();

            // Don't worry about campfire recipes.
            if (recipe instanceof CampfireRecipe) continue;

            // Make sure this recipe is a CookingRecipe.
            if (recipe instanceof final CookingRecipe<?> furnaceRecipe) {
                // Get the furnace type for this recipe.
                final FurnaceType type = recipe instanceof BlastingRecipe ? FurnaceType.BLASTING : (recipe instanceof SmokingRecipe ? FurnaceType.SMOKER : FurnaceType.FURNACE);
                Utils.debugLog("Loading " + furnaceRecipe.getKey() + " (a " + type + " recipe) into the recipes.");

                // Send a "warning" message about non-minecraft recipes.
                // This is not foolproof, and plugins/data packs that erroneously use the "minecraft" namespaces will not be detected.
                if (!furnaceRecipe.getKey().getNamespace().equalsIgnoreCase("minecraft")) {
                    Utils.log("Loading a non-minecraft recipe (or at minimum a recipe that is not under the Minecraft namespace): " + furnaceRecipe.getKey() + " If any issues arise with this recipe please post an issue on the PortableCraftingInvs Github. (run /pci info in-game)");
                }

                // This is set later.
                FurnaceRecipe customRecipe;
                // Check input choice to see if it's MaterialChoice.
                if (furnaceRecipe.getInputChoice() instanceof RecipeChoice.MaterialChoice choice) {
                    if (choice.getChoices().size() > 1) {
                        // Gets the key, something akin to 'nether_brick', the result of the recipe, the ingredient,
                        // the cooking time, the experience, and then we use the type configured above.
                        customRecipe = new FurnaceRecipe(furnaceRecipe.getKey().getKey(), furnaceRecipe.getResult(),
                                choice.getChoices(), furnaceRecipe.getCookingTime(), furnaceRecipe.getExperience(), type);
                    } else {
                        // Gets the key, something akin to 'nether_brick', the result of the recipe, the ingredient (in this case, the first in the choice list),
                        // the cooking time, the experience, and then we use the type configured above.
                        customRecipe = new FurnaceRecipe(furnaceRecipe.getKey().getKey(), furnaceRecipe.getResult(),
                                choice.getChoices().getFirst(), furnaceRecipe.getCookingTime(), furnaceRecipe.getExperience(), type);
                    }
                } else {
                    // Not a valid choice, continue.
                    continue;
                }

                this.furnaceRecipeMap.put(customRecipe.getKey(), customRecipe);
            }
        }

        Utils.log("Loaded a total of " + furnaceRecipeMap.size() + " furnace recipes.");
    }

    /**
     * Loads all vanilla brewing recipes.
     * <p></p>
     * Because Minecraft doesn't support custom brewing recipes (that I know of), we don't allow registering them.
     */
    public final void loadBrewingRecipes() {
        final List<BrewingRecipe> recipes = new ArrayList<>();
        recipes.addAll(BrewingRecipe.getAllRecipes("awkward", Material.NETHER_WART, PotionType.WATER, PotionType.AWKWARD));

        //<editor-fold desc="Useless Potions" defaultstate="collapsed">
        // Mundane potions.
        recipes.addAll(getMundane(Material.REDSTONE));
        recipes.addAll(getMundane(Material.SUGAR));
        recipes.addAll(getMundane(Material.RABBIT));
        recipes.addAll(getMundane(Material.GLISTERING_MELON_SLICE));
        recipes.addAll(getMundane(Material.SPIDER_EYE));
        recipes.addAll(getMundane(Material.MAGMA_CREAM));
        recipes.addAll(getMundane(Material.BLAZE_POWDER));
        recipes.addAll(getMundane(Material.GHAST_TEAR));

        // Thick
        recipes.addAll(BrewingRecipe.getAllRecipes("thick", Material.GLOWSTONE_DUST, PotionType.WATER, PotionType.THICK));
        //</editor-fold>

        //<editor-fold desc="Functional Potions" defaultstate="collapsed">
        // Speed
        recipes.addAll(BrewingRecipe.getAllRecipes("swiftness", Material.SUGAR, PotionType.AWKWARD, PotionType.SWIFTNESS));
        recipes.addAll(BrewingRecipe.getAllRecipes("swiftness_strong", Material.GLOWSTONE, PotionType.SWIFTNESS, PotionType.STRONG_SWIFTNESS));
        recipes.addAll(BrewingRecipe.getAllRecipes("swiftness_long", Material.REDSTONE, PotionType.SWIFTNESS, PotionType.LONG_SWIFTNESS));

        // Slowness
        recipes.addAll(BrewingRecipe.getAllRecipes("slowness", Material.FERMENTED_SPIDER_EYE, PotionType.SWIFTNESS, PotionType.SLOWNESS));
        recipes.addAll(BrewingRecipe.getAllRecipes("slowness_strong", Material.GLOWSTONE, PotionType.SLOWNESS, PotionType.STRONG_SLOWNESS));
        recipes.addAll(BrewingRecipe.getAllRecipes("slowness_long", Material.REDSTONE, PotionType.SLOWNESS, PotionType.LONG_SLOWNESS));
        recipes.addAll(BrewingRecipe.getAllRecipes("slowness_long_2", Material.FERMENTED_SPIDER_EYE, PotionType.LONG_SWIFTNESS, PotionType.LONG_SLOWNESS));
        recipes.addAll(BrewingRecipe.getAllRecipes("slowness_long_3", Material.FERMENTED_SPIDER_EYE, PotionType.LONG_LEAPING, PotionType.LONG_SLOWNESS));

        // Jump
        recipes.addAll(BrewingRecipe.getAllRecipes("jump_boost", Material.RABBIT_FOOT, PotionType.AWKWARD, PotionType.LEAPING));
        recipes.addAll(BrewingRecipe.getAllRecipes("jump_boost_strong", Material.GLOWSTONE, PotionType.LEAPING, PotionType.STRONG_LEAPING));
        recipes.addAll(BrewingRecipe.getAllRecipes("jump_boost_long", Material.REDSTONE, PotionType.LEAPING, PotionType.LONG_LEAPING));

        // Strength
        recipes.addAll(BrewingRecipe.getAllRecipes("strength", Material.BLAZE_POWDER, PotionType.AWKWARD, PotionType.STRENGTH));
        recipes.addAll(BrewingRecipe.getAllRecipes("strength_strong", Material.GLOWSTONE, PotionType.STRENGTH, PotionType.STRONG_STRENGTH));
        recipes.addAll(BrewingRecipe.getAllRecipes("strength_long", Material.REDSTONE, PotionType.STRENGTH, PotionType.LONG_STRENGTH));

        // Healing
        recipes.addAll(BrewingRecipe.getAllRecipes("healing", Material.GLISTERING_MELON_SLICE, PotionType.AWKWARD, PotionType.HEALING));
        recipes.addAll(BrewingRecipe.getAllRecipes("healing_strong", Material.GLOWSTONE, PotionType.HEALING, PotionType.STRONG_HEALING));

        // Poison
        recipes.addAll(BrewingRecipe.getAllRecipes("poison", Material.SPIDER_EYE, PotionType.AWKWARD, PotionType.POISON));
        recipes.addAll(BrewingRecipe.getAllRecipes("poison_strong", Material.GLOWSTONE, PotionType.POISON, PotionType.STRONG_POISON));
        recipes.addAll(BrewingRecipe.getAllRecipes("poison_long", Material.REDSTONE, PotionType.POISON, PotionType.LONG_POISON));

        // Harming
        recipes.addAll(BrewingRecipe.getAllRecipes("harming", Material.FERMENTED_SPIDER_EYE, PotionType.POISON, PotionType.HARMING));
        recipes.addAll(BrewingRecipe.getAllRecipes("harming_2", Material.FERMENTED_SPIDER_EYE, PotionType.HEALING, PotionType.HARMING));
        recipes.addAll(BrewingRecipe.getAllRecipes("harming_strong", Material.FERMENTED_SPIDER_EYE, PotionType.STRONG_POISON, PotionType.STRONG_HARMING));
        recipes.addAll(BrewingRecipe.getAllRecipes("harming_strong_2", Material.FERMENTED_SPIDER_EYE, PotionType.STRONG_HEALING, PotionType.STRONG_HARMING));
        recipes.addAll(BrewingRecipe.getAllRecipes("harming_strong_3", Material.GLOWSTONE, PotionType.HARMING, PotionType.STRONG_HARMING));

        // Regen
        recipes.addAll(BrewingRecipe.getAllRecipes("regeneration", Material.GHAST_TEAR, PotionType.AWKWARD, PotionType.REGENERATION));
        recipes.addAll(BrewingRecipe.getAllRecipes("regeneration_strong", Material.GLOWSTONE, PotionType.REGENERATION, PotionType.STRONG_REGENERATION));
        recipes.addAll(BrewingRecipe.getAllRecipes("regeneration_long", Material.REDSTONE, PotionType.AWKWARD, PotionType.LONG_REGENERATION));

        // Fire Resist
        recipes.addAll(BrewingRecipe.getAllRecipes("fire_resistance", Material.MAGMA_CREAM, PotionType.AWKWARD, PotionType.FIRE_RESISTANCE));
        recipes.addAll(BrewingRecipe.getAllRecipes("fire_resistance_long", Material.GLOWSTONE, PotionType.FIRE_RESISTANCE, PotionType.LONG_FIRE_RESISTANCE));

        // Water Breathing
        recipes.addAll(BrewingRecipe.getAllRecipes("water_breathing", Material.PUFFERFISH, PotionType.AWKWARD, PotionType.WATER_BREATHING));
        recipes.addAll(BrewingRecipe.getAllRecipes("water_breathing_long", Material.GLOWSTONE, PotionType.WATER_BREATHING, PotionType.LONG_WATER_BREATHING));

        // Night Vision
        recipes.addAll(BrewingRecipe.getAllRecipes("night_vision", Material.GOLDEN_CARROT, PotionType.AWKWARD, PotionType.NIGHT_VISION));
        recipes.addAll(BrewingRecipe.getAllRecipes("night_vision_long", Material.GLOWSTONE, PotionType.NIGHT_VISION, PotionType.LONG_NIGHT_VISION));

        // Invisibility
        recipes.addAll(BrewingRecipe.getAllRecipes("invisibility", Material.FERMENTED_SPIDER_EYE, PotionType.NIGHT_VISION, PotionType.INVISIBILITY));
        recipes.addAll(BrewingRecipe.getAllRecipes("invisibility_long", Material.FERMENTED_SPIDER_EYE, PotionType.LONG_NIGHT_VISION, PotionType.LONG_INVISIBILITY));
        recipes.addAll(BrewingRecipe.getAllRecipes("invisibility_long_2", Material.GLOWSTONE, PotionType.INVISIBILITY, PotionType.LONG_INVISIBILITY));

        // Turtle Master
        recipes.addAll(BrewingRecipe.getAllRecipes("turtle_master", Material.TURTLE_HELMET, PotionType.AWKWARD, PotionType.TURTLE_MASTER));
        recipes.addAll(BrewingRecipe.getAllRecipes("turtle_master_strong", Material.GLOWSTONE, PotionType.TURTLE_MASTER, PotionType.STRONG_TURTLE_MASTER));
        recipes.addAll(BrewingRecipe.getAllRecipes("turtle_master_long", Material.REDSTONE, PotionType.TURTLE_MASTER, PotionType.LONG_TURTLE_MASTER));

        // Slow Falling
        recipes.addAll(BrewingRecipe.getAllRecipes("slow_falling", Material.PHANTOM_MEMBRANE, PotionType.AWKWARD, PotionType.SLOW_FALLING));
        recipes.addAll(BrewingRecipe.getAllRecipes("slow_falling_long", Material.REDSTONE, PotionType.SLOW_FALLING, PotionType.LONG_SLOW_FALLING));

        // Weakness
        recipes.addAll(BrewingRecipe.getAllRecipes("weakness", Material.FERMENTED_SPIDER_EYE, PotionType.WATER, PotionType.WEAKNESS));
        recipes.addAll(BrewingRecipe.getAllRecipes("weakness_long", Material.REDSTONE, PotionType.WEAKNESS, PotionType.LONG_WEAKNESS));
        //</editor-fold>

        recipes.forEach((recipe) -> this.brewingRecipeMap.put(recipe.getKey(), recipe));

        Utils.log("Loaded a total of " + recipes.size() + " brewing recipes.");
    }

    /**
     * Turns an ingredient into the mundane potion.
     *
     * @param ingredient The {@link Material} to convert to mundane.
     * @return Return a list of all mundane potions.
     */
    @Contract("_ -> new")
    private @Unmodifiable List<BrewingRecipe> getMundane(final Material ingredient) {
        return BrewingRecipe.getAllRecipes("mundane_" + ingredient.getKey().getKey().toLowerCase(), ingredient, PotionType.WATER, PotionType.MUNDANE);
    }

    /**
     * Gets {@link FurnaceFuel} based on a {@link Material}.
     *
     * @param material The {@link Material} to search for.
     * @return Returns the fuel if existing, otherwise <code>null</code>.
     */
    public Optional<FurnaceFuel> getFurnaceFuel(final Material material) {
        return this.furnaceFuelMap.values().stream().filter((fuel) -> fuel.matches(material)).findAny();
    }

    /**
     * Gets {@link BrewingFuel} based on a {@link Material}.
     *
     * @param material The {@link Material} to search for.
     * @return Returns the fuel if existing, otherwise <code>null</code>.
     */
    public Optional<BrewingFuel> getBrewingFuel(final Material material) {
        return this.brewingFuelMap.values().stream().filter((fuel) -> fuel.matches(material)).findAny();
    }

    /**
     * Gets {@link BrewingRecipe} based on a {@link ItemStack}s.
     *
     * @param ingredient The {@link ItemStack} ingredient.
     * @param bottle     The {@link ItemStack} bottle.
     * @return Returns the recipe if existing, otherwise <code>null</code>.
     */
    public final @NotNull Optional<BrewingRecipe> getRecipe(@NotNull ItemStack ingredient, ItemStack bottle) {
        final ItemStack checkIng = ingredient.clone();
        checkIng.setAmount(1);

        return this.brewingRecipeMap.values().stream().filter((recipe) -> {
            final PotionMeta inputMeta = (PotionMeta) recipe.getInputBottle().getItemMeta();
            if (inputMeta == null) return false;

            final PotionMeta passedMeta = (PotionMeta) bottle.getItemMeta();
            if (passedMeta == null) return false;

            return recipe.getIngredient().isSimilar(checkIng) && bottle.getType() == recipe.getInputBottle().getType() && inputMeta.getBasePotionType() == passedMeta.getBasePotionType();
        }).findAny();
    }

    /**
     * Gets {@link BrewingRecipe} based on a {@link Material}\s.
     *
     * @param material The {@link Material} to get the recipe from.
     * @return Returns the recipe if existing, otherwise <code>null</code>.
     */
    public final @NotNull Optional<FurnaceRecipe> getRecipe(final Material material, final FurnaceType type) {
        return this.furnaceRecipeMap.values().stream().filter((recipe) -> {
            if (recipe.getIngredients() != null) {
                return recipe.getIngredients().contains(material) && type == recipe.getType();
            }

            return recipe.getIngredient() == material && type == recipe.getType();
        }).findAny();
    }

}
