package com.itsschatten.portablecrafting.virtual.machine;

import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.virtual.RecipeManager;
import com.itsschatten.portablecrafting.virtual.SQLSerializable;
import com.itsschatten.portablecrafting.virtual.VirtualManager;
import com.itsschatten.portablecrafting.virtual.fuel.BrewingFuel;
import com.itsschatten.portablecrafting.virtual.machine.properties.BrewingProperties;
import com.itsschatten.portablecrafting.virtual.recipe.BrewingRecipe;
import com.itsschatten.portablecrafting.virtual.utils.PropertyHolder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Represents a Brewing Stand, but it's virtual!
 */
@Getter
public class BrewingStand extends Machine implements PropertyHolder<BrewingProperties>, ConfigurationSerializable, SQLSerializable {

    /**
     * The inventory for this brewing stand.
     */
    private final Inventory inventory;

    /**
     * The bottles.
     */
    private final ItemStack[] bottles;

    /**
     * Properties for this Brewing Stand.
     */
    private final BrewingProperties properties;

    /**
     * The fuel.
     */
    private ItemStack fuel = null;
    /**
     * The ingredient.
     */
    private ItemStack ingredient = null;
    /**
     * The time remaining for the fuel.
     */
    private int fuelTime = 0;
    /**
     * The maximum brews.
     */
    private int max = 0;
    /**
     * The current brewing time.
     */
    private int brewTime = 0;
    /**
     * The speed of this brew.
     */
    private int speed = 1;

    // The currently active recipe.
    private BrewingRecipe currentRecipe = null;

    /**
     * Creates a new {@link BrewingStand}.
     *
     * @param name       The title for this stand.
     * @param properties The properties for this stand.
     */
    public BrewingStand(@NotNull String name, BrewingProperties properties) {
        super(name, UUID.randomUUID(), null);
        this.inventory = Bukkit.createInventory(null, InventoryType.BREWING, name);
        this.bottles = new ItemStack[3];
        this.properties = properties;
        this.updateInventory();
    }

    /**
     * Loads the {@link BrewingStand} from a storage.
     *
     * @param name       The title for this stand.
     * @param uniqueId   The UUID of this stand.
     * @param bottles    The bottles in this stand.
     * @param properties The properties of this stand.
     * @param fuel       The fuel items of this stand.
     * @param ingredient The ingredient in this stand.
     * @param fuelTime   The remaining fuel in this stand.
     * @param max        The maximum fuel in this stand.
     * @param brewTime   The current brew time of this stand.
     * @param speed      The speed of this stand.
     * @param date
     */
    public BrewingStand(@NotNull String name, @NotNull UUID uniqueId, ItemStack[] bottles, BrewingProperties properties, ItemStack fuel, ItemStack ingredient, int fuelTime, int max, int brewTime, int speed, @Nullable LocalDateTime date) {
        super(name, uniqueId, date);
        this.inventory = Bukkit.createInventory(null, InventoryType.BREWING, name);
        this.bottles = bottles;
        this.properties = properties;
        this.fuel = fuel;
        this.ingredient = ingredient;
        this.fuelTime = fuelTime;
        this.max = max;
        this.brewTime = brewTime;
        this.speed = speed;
    }

    @Override
    public void openInventory(final @NotNull Player player) {
        preOpen();

        this.updateInventory();
        this.updateBrewSpeed();
        player.openInventory(inventory);
        updateInventoryView();
    }

    @Override
    public void forceCloseInventory() {
        this.inventory.getViewers().forEach(HumanEntity::closeInventory);
    }

    // Updates the item.
    private void updateInventory() {
        this.inventory.setItem(0, bottles[0]);
        this.inventory.setItem(1, bottles[1]);
        this.inventory.setItem(2, bottles[2]);
        this.inventory.setItem(3, ingredient);
        this.inventory.setItem(4, fuel);
    }

    // Updates the values within the menu to be animated.
    private void updateInventoryView() {
        final ItemStack bottle1 = this.inventory.getItem(0);
        final ItemStack bottle2 = this.inventory.getItem(1);
        final ItemStack bottle3 = this.inventory.getItem(2);
        final ItemStack ingredient = this.inventory.getItem(3);
        final ItemStack fuel = this.inventory.getItem(4);

        // Check bottles.
        if (!matchBottle(this.bottles[0], bottle1)) {
            this.bottles[0] = bottle1;
        }
        if (!matchBottle(this.bottles[1], bottle2)) {
            this.bottles[1] = bottle2;
        }
        if (!matchBottle(this.bottles[2], bottle3)) {
            this.bottles[2] = bottle3;
        }

        // Check ingredients.
        if (doesNotMatch(this.ingredient, ingredient)) {
            this.ingredient = ingredient;
        }

        // Check fuel.
        if (doesNotMatch(this.fuel, fuel)) {
            this.fuel = fuel;
        }

        // Update the inventory.
        this.inventory.getViewers().forEach((viewer) -> {
            try {
                // TODO: Use the BrewingStandView
                final InventoryView view = viewer.getOpenInventory();
                view.setProperty(InventoryView.Property.BREW_TIME, brewTime);
                view.setProperty(InventoryView.Property.FUEL_TIME, (int) Math.round(((double) fuelTime) / ((double) (max / 20))));
            } catch (Exception ignored) {
            }
        });
    }

    // Matches a bottle based on it's base potion type and it's Material.
    private boolean matchBottle(final ItemStack first, final ItemStack second) {
        if (first == null || second == null) {
            return false;
        }

        if (!(first.getItemMeta() instanceof PotionMeta firstMeta) || !(second.getItemMeta() instanceof PotionMeta secondMeta)) {
            return false;
        }

        return first.getType() == second.getType() && firstMeta.getBasePotionType() == secondMeta.getBasePotionType();
    }

    // Checks if the provided ItemStacks do not equal one another.
    private boolean doesNotMatch(ItemStack first, ItemStack second) {
        if (first == null || second == null) {
            return true;
        }
        return !first.equals(second);
    }

    // Checks if we can use the fuel in the fuel slot.
    private boolean canUseFuel() {
        if (this.fuel == null) return false;
        return RecipeManager.getInstance().getBrewingFuel(this.fuel.getType()).isPresent();
    }

    // Check if we can brew.
    private boolean canBrew() {
        return assignRecipe() != null;
    }

    /**
     * Process the finished brew.
     */
    private void processBrew() {
        // Get the current recipe.
        final BrewingRecipe recipe = currentRecipe;

        // Ensure all bottles match the required recipe.
        for (int i = 0; i < 3; i++) {
            if (matchBottle(bottles[i], recipe.getInputBottle())) {
                bottles[i] = recipe.getResult();
            }
        }

        // Update fuel.
        if (fuelTime > 0) this.fuelTime--;

        // Update ingredients.
        if (ingredient.getAmount() > 1) {
            ingredient.setAmount(ingredient.getAmount() - 1);
        } else {
            ingredient = null;
        }
        // Reset brew time.
        this.brewTime = 0;
        // Reset current recipe.
        this.currentRecipe = null;
        // Update the items.
        updateInventory();
        updateInventoryView();

        // Play a sound for the viewers.
        for (final HumanEntity viewer : inventory.getViewers()) {
            final Player player = (Player) viewer;
            player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1, 1);
        }
    }

    /**
     * Process the consumption of fuel.
     */
    private void processFuel() {
        // Get the fuel.
        final BrewingFuel brewingFuel = RecipeManager.getInstance().getBrewingFuel(this.fuel.getType()).orElse(null);
        if (brewingFuel == null) return;

        // Update the values.
        this.fuelTime = brewingFuel.getDuration();
        this.max = fuelTime;

        // Update the fuel amounts if larger than 1 stack
        if (this.fuel.getAmount() > 1) {
            this.fuel.setAmount(this.fuel.getAmount() - 1);
        } else {
            this.fuel = null;
        }

        // Update the inventory to reflect those changes.
        updateInventory();
        updateInventoryView();
    }

    // Gets the recipe from the ingredients in the menu.
    private @Nullable BrewingRecipe assignRecipe() {
        if (this.ingredient == null || this.bottles.length == 0) return null;

        BrewingRecipe foundRecipe = null;
        for (int i = 0; i < 3; i++) {
            if (this.bottles[i] != null) {
                final Optional<BrewingRecipe> recipeOptional = RecipeManager.getInstance().getRecipe(this.ingredient, this.bottles[i]);
                foundRecipe = recipeOptional.orElse(null);

                // Check if the recipe is equal to the current.
                if (foundRecipe != null && foundRecipe.equals(currentRecipe)) {
                    return currentRecipe;
                }
            }
        }

        if (foundRecipe == null) {
            return null;
        }

        this.currentRecipe = foundRecipe;
        return foundRecipe;
    }

    // Updates the brewing speed.
    private void updateBrewSpeed() {
        final BrewingRecipe recipe = currentRecipe;
        if (recipe != null) {
            speed = (int) ((400 / recipe.getBrewTime()) / properties.getBrewMultiplier());
        }
    }

    @Override
    public void tick() {
        try {
            if (currentRecipe == null) {
                if (canBrew()) {
                    this.brewTime = 400;
                    updateBrewSpeed();
                }
            }

            // Check if we have fuel.
            if (this.fuelTime > 0) {
                // Check if we are currently brewing.
                if (this.brewTime > 0) {
                    // Ensure we have an ingredient.
                    if (this.ingredient != null) {
                        // Decrement the brew time.
                        this.brewTime -= this.speed;
                        // Check if we are completed.
                        if (this.brewTime <= 0) {
                            // Finish the brew.
                            processBrew();
                        }
                    } else this.brewTime = 0; // We don't have an ingredient, reset to default.
                } else if (canBrew()) {
                    this.brewTime = currentRecipe.getBrewTime();
                    updateBrewSpeed();
                }
            } else if (canUseFuel()) { // No fuel, check if we can process some.
                // Process the fuel.
                processFuel();
            }
            updateInventoryView();
        } catch (AssertionError ignored) {
        }
    }

    //<editor-fold desc="Serializers" defaultstate="collapsed">
    // Serializer for config
    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", this.getName());
        result.put("uuid", this.getUniqueId().toString());
        result.put("brewTime", this.brewTime);
        result.put("fuelTime", this.fuelTime);
        result.put("maxBrews", this.max);
        result.put("fuel", this.fuel);
        result.put("ingredient", this.ingredient);
        result.put("bottle-1", this.bottles[0]);
        result.put("bottle-2", this.bottles[1]);
        result.put("bottle-3", this.bottles[2]);
        result.put("lastOpened", LocalDateTime.now().toString());
        return result;
    }

    @Contract("_ -> new")
    public static @NotNull BrewingStand deserialize(@NotNull Map<String, Object> args) {
        final String name = ((String) args.get("name"));
        final UUID uuid = UUID.fromString(((String) args.get("uuid")));
        final int brewTime = ((Number) args.get("brewTime")).intValue();
        final int fuelTime = ((Number) args.get("fuelTime")).intValue();
        final int maxBrews = ((Number) args.get("maxBrews")).intValue();
        final ItemStack fuel = (ItemStack) args.get("fuel");
        final ItemStack ing = (ItemStack) args.get("ingredient");
        final ItemStack[] bottles = new ItemStack[3];
        bottles[0] = (ItemStack) args.get("bottle-1");
        bottles[1] = (ItemStack) args.get("bottle-2");
        bottles[2] = (ItemStack) args.get("bottle-3");
        final LocalDateTime date = LocalDateTime.parse((String) args.get("lastOpened"));

        return new BrewingStand(name, uuid, bottles, BrewingProperties.NORMAL, fuel, ing, fuelTime, maxBrews, brewTime, 1, date);
    }

    // SQL serializers.
    @Override
    public Map<String, Object> serializeForSQL() {
        final Map<String, Object> map = new HashMap<>();
        map.put("bottle_1", Utils.serialize(bottles[0]));
        map.put("bottle_2", Utils.serialize(bottles[1]));
        map.put("bottle_3", Utils.serialize(bottles[2]));
        map.put("ingredient", Utils.serialize(ingredient));
        map.put("fuel_item", Utils.serialize(fuel));

        map.put("title", getName());

        map.put("brew_time", brewTime);
        map.put("fuel_time", fuelTime);
        map.put("max_brews", max);
        map.put("last_opened", LocalDateTime.now());
        return map;
    }

    @Contract("_ -> new")
    public static @NotNull BrewingStand deserializeFromSQL(@NotNull Map<String, Object> sql) {
        final String name = ((String) sql.get("title"));
        final UUID uuid = UUID.fromString(((String) sql.get("uuid")));
        final int brewTime = ((Number) sql.get("brew_time")).intValue();
        final int fuelTime = ((Number) sql.get("fuel_time")).intValue();
        final int maxBrews = ((Number) sql.get("max_brews")).intValue();
        final ItemStack fuel = Utils.deserialize((String) sql.get("fuel_item"));
        final ItemStack ing = Utils.deserialize((String) sql.get("ingredient"));
        final ItemStack[] bottles = new ItemStack[3];
        bottles[0] = Utils.deserialize((String) sql.get("bottle_1"));
        bottles[1] = Utils.deserialize((String) sql.get("bottle_2"));
        bottles[2] = Utils.deserialize((String) sql.get("bottle_3"));
        final LocalDateTime date = ((Timestamp) sql.get("last_opened")).toLocalDateTime();

        return new BrewingStand(name, uuid, bottles, BrewingProperties.NORMAL, fuel, ing, fuelTime, maxBrews, brewTime, 1, date);
    }
    //</editor-fold>

    /**
     * {@inheritDoc}
     */
    @Override
    public void preOpen() {
        if (getLastOpened() != null) {
            // Get the ticks between now and the last time this inventory was opened.
            final long secondsSinceLastOpen = ChronoUnit.SECONDS.between(getLastOpened(), LocalDateTime.now());
            final long ticks = secondsSinceLastOpen * 20L;

            // Do we have a valid recipe?
            if (canBrew()) {
                // Set the recipe cook time.
                final int recipeCookTime = currentRecipe.getBrewTime();
                this.brewTime += Math.min((int) ticks, recipeCookTime);

                // If we can brew, brew.
                if (brewTime > recipeCookTime) {
                    Utils.debugLog("Processing brew operation for " + this.getUniqueId() + ".");
                    processBrew();
                }
            }
        }
    }

    /**
     * Save this BrewingStand to whichever storage medium is used, this will use this BrewingStand's {@link UUID}.
     */
    public final void save(final Player player) {
        VirtualManager.getInstance().getStorage().saveBrewingStand(player, this);
    }

}
