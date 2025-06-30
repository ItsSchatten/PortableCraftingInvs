package com.itsschatten.portablecrafting.virtual.machine;

import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.virtual.RecipeManager;
import com.itsschatten.portablecrafting.virtual.SQLSerializable;
import com.itsschatten.portablecrafting.virtual.VirtualManager;
import com.itsschatten.portablecrafting.virtual.fuel.FurnaceFuel;
import com.itsschatten.portablecrafting.virtual.machine.properties.FurnaceProperties;
import com.itsschatten.portablecrafting.virtual.recipe.FurnaceRecipe;
import com.itsschatten.portablecrafting.virtual.utils.FurnaceType;
import com.itsschatten.portablecrafting.virtual.utils.PropertyHolder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.view.FurnaceView;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Represents a Furnace, but virtual!
 */
@Getter
public class Furnace extends Machine implements PropertyHolder<FurnaceProperties>, ConfigurationSerializable, SQLSerializable {

    /**
     * Properties for this Furnace.
     */
    private final FurnaceProperties properties;

    /**
     * The inventory for this furnace.
     */
    private final Inventory inventory;

    /**
     * What type of furnace this is, used to set the proper InventoryType.
     */
    private final FurnaceType type;

    // Fuel items.
    private ItemStack fuel;
    // Input items, the thing to be smelted.
    private ItemStack input;
    // The output, what happened because of the smelt.
    private ItemStack output;

    // Our current cook time.
    private int cookTime;
    // How long it takes an item to smelt.
    private int totalCookTime;
    // The current time of the fuel.
    private int fuelTime;
    // How long the fuel can last.
    private int totalFuelTime;
    // The experience.
    private float experience;

    // Current recipe, this way we don't have to call a check 200 times.
    private FurnaceRecipe currentRecipe = null;

    public Furnace(String name) {
        this(name, FurnaceType.FURNACE, FurnaceProperties.FURNACE);
    }

    public Furnace(String name, @NotNull FurnaceType type, FurnaceProperties properties) {
        super(name, UUID.randomUUID(), null);
        this.type = type;
        this.properties = properties;
        this.inventory = Bukkit.createInventory(null, InventoryType.FURNACE, name);
    }

    public Furnace(@NotNull String name, @NotNull UUID uniqueId, FurnaceProperties properties, @NotNull FurnaceType type, ItemStack fuel, ItemStack input, ItemStack output, int cookTime, int fuelTime, int totalFuel, float experience, @Nullable LocalDateTime date) {
        super(name, uniqueId, date);
        this.properties = properties;
        this.type = type;
        this.inventory = Bukkit.createInventory(null, InventoryType.FURNACE, name);
        this.fuel = fuel;
        this.input = input;
        this.output = output;
        this.cookTime = cookTime;
        this.fuelTime = fuelTime;
        this.totalFuelTime = totalFuel;
        this.experience = experience;
    }

    @Override
    public void tick() {
        // Check if we have some fuel remaining.
        if (this.fuelTime > 0) {
            // Decrement the fuel.
            this.fuelTime--;
            // Can we smelt?
            if (canSmelt()) {
                // Update the cook time.
                this.cookTime++;
                // Check if we are at maximum cook time.
                if (this.cookTime >= this.totalCookTime) {
                    // Reset to 0.
                    this.cookTime = 1;
                    // Process our smelt.
                    processSmelt();
                }
            } else {
                // Can't smelt, ensure cook time is 0.
                this.cookTime = 0;
            }
        } else if (canBurnFuel() && canSmelt()) { // Make sure we can burn a fuel and can smelt an item.
            // Burn a fuel item.
            processFuelBurn();
        } else if (this.cookTime > 0) { // Check if we have some cook time.
            // Make sure we can still smelt.
            if (canSmelt()) {
                // Decrement cook time by 5.
                this.cookTime -= 5;
            } else {
                // Can't smelt no more, remove cook.
                this.cookTime = 0;
            }
        }

        // Finally, update the inventory view.
        updateInventoryView();
    }

    /**
     * Get the output of this furnace and remove it, similar to {@link #extractExperience()}.
     *
     * @return Return's the output of the furnace.
     */
    public ItemStack getOutput() {
        if (output == null) return null;

        final ItemStack output = this.output.clone();
        this.output = null;

        return output;
    }

    @Override
    public void openInventory(@NotNull Player player) {
        preOpen();
        // update items.
        updateInventory();
        player.openInventory(this.inventory);
    }

    @Override
    public void forceCloseInventory() {
        this.inventory.getViewers().forEach(HumanEntity::closeInventory);
    }

    // If we can burn the fuel item.
    private boolean canBurnFuel() {
        if (this.fuel == null) return false;
        return RecipeManager.getInstance().getFurnaceFuel(this.fuel.getType()).orElse(null) != null;
    }

    // Process a fuel burn.
    private void processFuelBurn() {
        final FurnaceFuel furnaceFuel = RecipeManager.getInstance().getFurnaceFuel(this.fuel.getType()).orElse(null);
        if (furnaceFuel == null) return;

        // Handle the decrement of fuel.
        if (this.fuel.getAmount() > 1) {
            this.fuel.setAmount(this.fuel.getAmount() - 1);
        } else {
            // If a lava bucket, we want to return a bucket.
            if (this.fuel.getType() == Material.LAVA_BUCKET) {
                this.fuel.setType(Material.BUCKET);
            } else this.fuel = null;
        }

        final int burn = (int) (furnaceFuel.getBurnTime() / properties.getCookMultiplier());
        // Assume that cook multiplier correlates with how long the cook multiplier takes.
        this.fuelTime = burn;
        this.totalFuelTime = burn;
        updateInventory();
    }

    // If the input item can be smelted.
    private boolean canSmelt() {
        // Check for a proper recipe.
        if (assignRecipe() == null) return false;

        // Check if the output is null, if so immediately allow a smelt.
        if (this.output == null) return true;

        // Check the output items to make sure we can add to it.
        final Material outputType = this.output.getType();
        if (outputType == currentRecipe.getResult().getType()) {
            return this.output.getAmount() < outputType.getMaxStackSize();
        }

        return false;
    }

    // Gets the recipe from the ingredients in the menu.
    private @Nullable FurnaceRecipe assignRecipe() {
        // Make sure we have an input.
        if (this.input == null) {
            // Do we have a current recipe? If so, nullify.
            if (currentRecipe != null) {
                this.currentRecipe = null;
                // We assume this is also set if we have a recipe, so update to 0 for convenience.
                this.totalCookTime = 0;
            }
            return null;
        }

        // Get the recipe based on the input.
        final FurnaceRecipe recipe = RecipeManager.getInstance().getRecipe(this.input, this.getType()).orElse(null);

        // Ensure it's a valid recipe.
        if (recipe == null) {
            // Not valid, do we have a current?
            if (currentRecipe != null) {
                this.currentRecipe = null;
                // We assume this is also set if we have a recipe, so update to 0 for convenience.
                this.totalCookTime = 0;
            }
            return null;
        }

        // Check if the recipe is equal to current, if so return the currentRecipe.
        if (recipe.equals(currentRecipe)) return currentRecipe;

        // Update the current totalCookTime and recipe.
        this.currentRecipe = recipe;
        this.totalCookTime = recipe.getCookTime();

        return recipe;
    }

    // Processes a smelt.
    private void processSmelt() {
        // Check if we have a recipe, it should be stored if one is found.
        if (currentRecipe == null) return;

        // Update the output stack.
        if (this.output == null) {
            this.output = currentRecipe.getResult();
        } else {
            this.output.setAmount(this.output.getAmount() + 1);
        }
        // Update the stored experience.
        this.experience += currentRecipe.getExperience();

        // Update the input amounts.
        if (this.input != null && this.input.getAmount() > 1) {
            this.input.setAmount(this.input.getAmount() - 1);
        } else this.input = null;

        // Finally, update the inventory.
        updateInventory();
    }

    // Sets the items in the inventory.
    private void updateInventory() {
        this.inventory.setItem(0, this.input);
        this.inventory.setItem(1, this.fuel);
        this.inventory.setItem(2, this.output);
    }

    // Updates the inventory to show the progress.
    private void updateInventoryView() {
        final ItemStack input = this.inventory.getItem(0);
        if (input != null && !input.equals(this.input)) { // Check if the input is not null, then check if it equals the current input item.
            // If not, update the input.
            this.input = input;
        } else if (input == null) {
            this.input = null;
        }

        final ItemStack fuel = this.inventory.getItem(1);
        if (fuel != null && !fuel.equals(this.fuel)) { // Check if the fuel is not null, then check if it equals the current fuel item.
            // If not, update the fuel.
            this.fuel = fuel;
        } else if (fuel == null) {
            this.fuel = null;
        }

        final ItemStack output = this.inventory.getItem(2);
        if (output != null && !output.equals(this.output)) { // Check if the output is not null, then check if it equals the current output item.
            // If not, update the output.
            this.output = output;
        } else if (output == null) {
            this.output = null;
        }

        // Updates the views in the inventory.
        this.inventory.getViewers().forEach((viewer) -> {
            try {
                final InventoryView view = viewer.getOpenInventory();
                if (view instanceof FurnaceView furnace) {
                    // TODO: Update this to non-deprecated options.
                    // furnace.setCookTime(this.cookTime, this.totalCookTime);
                    // furnace.setBurnTime(this.fuelTime, this.totalFuelTime);
                    furnace.setProperty(InventoryView.Property.COOK_TIME, this.cookTime);
                    furnace.setProperty(InventoryView.Property.TICKS_FOR_CURRENT_SMELTING, this.totalCookTime);
                    furnace.setProperty(InventoryView.Property.BURN_TIME, this.fuelTime);
                    furnace.setProperty(InventoryView.Property.TICKS_FOR_CURRENT_FUEL, this.totalFuelTime);
                }

            } catch (Exception ignored) {
            } catch (NoClassDefFoundError ignored) { // TODO: Remove.
            }
        });

    }

    /**
     * Get the currently stored experience of this Furnace and then reset it to 0.
     *
     * @return Current experience stored in this furnace
     */
    public final float extractExperience() {
        final float experience = this.experience;
        this.experience = 0f;
        return experience;
    }

    //<editor-fold desc="Serializers" defaultstate="collapsed">
    // Serializer for config
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", this.getName());
        result.put("uuid", this.getUniqueId().toString());
        result.put("cookTime", this.cookTime);
        result.put("fuelTime", this.fuelTime);
        result.put("maxFuel", this.totalFuelTime);
        result.put("xp", this.experience);
        result.put("fuel", this.fuel);
        result.put("input", this.input);
        result.put("output", this.output);
        result.put("type", this.type.name());
        result.put("lastOpened", getLastOpened().toString());
        return result;
    }

    public static @NotNull Furnace deserialize(@NotNull Map<String, Object> args) {
        final String name = ((String) args.get("name"));
        final UUID uuid = UUID.fromString(((String) args.get("uuid")));
        final int cookTime = ((Number) args.get("cookTime")).intValue();
        final int fuelTime = ((Number) args.get("fuelTime")).intValue();
        final int totalFuel = ((Number) args.get("maxFuel")).intValue();
        final float xp = args.containsKey("xp") ? ((Number) args.get("xp")).floatValue() : 0.0f;
        final ItemStack fuel = ((ItemStack) args.get("fuel"));
        final ItemStack input = ((ItemStack) args.get("input"));
        final ItemStack output = ((ItemStack) args.get("output"));
        final FurnaceType furnaceType = FurnaceType.valueOf((String) args.get("type"));
        final FurnaceProperties furnaceProperties = furnaceType.getProperties();
        final LocalDateTime date = LocalDateTime.parse((String) args.get("lastOpened"));

        return new Furnace(name, uuid, furnaceProperties, furnaceType, fuel, input, output, cookTime, fuelTime, totalFuel, xp, date);
    }

    @Override
    public Map<String, Object> serializeForSQL() {
        final Map<String, Object> map = new HashMap<>();
        map.put("type", type);

        map.put("cook_time", cookTime);
        map.put("fuel_time", fuelTime);
        map.put("total_fuel", totalFuelTime);
        map.put("experience", experience);

        map.put("fuel_item", Utils.serialize(fuel));
        map.put("input_item", Utils.serialize(input));
        map.put("output_item", Utils.serialize(output));

        map.put("title", getName());

        map.put("last_opened", LocalDateTime.now());
        return map;
    }

    @Contract(pure = true)
    public static @NotNull Furnace deserializeFromSQL(@NotNull Map<String, Object> sql) {
        final String name = ((String) sql.get("title"));
        final UUID uuid = UUID.fromString(((String) sql.get("uuid")));
        final int cookTime = ((Number) sql.get("cook_time")).intValue();
        final int fuelTime = ((Number) sql.get("fuel_time")).intValue();
        final int totalFuel = ((Number) sql.get("total_fuel")).intValue();
        final float xp = ((Number) sql.get("experience")).floatValue();
        final ItemStack fuel = Utils.deserialize((String) sql.get("fuel_item"));
        final ItemStack input = Utils.deserialize((String) sql.get("input_item"));
        final ItemStack output = Utils.deserialize((String) sql.get("output_item"));
        final FurnaceType furnaceType = FurnaceType.valueOf((String) sql.get("type"));
        final FurnaceProperties furnaceProperties = furnaceType.getProperties();
        final LocalDateTime date = ((Timestamp) sql.get("last_opened")).toLocalDateTime();

        return new Furnace(name, uuid, furnaceProperties, furnaceType, fuel, input, output, cookTime, fuelTime, totalFuel, xp, date);
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
            final long ticksSinceLastOpened = secondsSinceLastOpen * 20L;

            setLastOpened(LocalDateTime.now());

            // Do we have a valid recipe?
            if (canSmelt()) {
                // Set the recipe cook time.
                final int recipeCookTime = currentRecipe.getCookTime();

                // If we are opening less than when a recipe can be cooked, add the time and remove the fuel time.
                if (ticksSinceLastOpened < recipeCookTime) {
                    // We have some fuel, go ahead and progress the smelt and consume the fuel.
                    if (fuelTime > 0) {
                        this.cookTime += (int) ticksSinceLastOpened;
                        this.fuelTime -= (int) ticksSinceLastOpened;
                    }
                } else {
                    // We can't use null fuel.
                    if (fuelTime == 0 && (fuel == null || fuel.getType().isAir())) {
                        return;
                    }

                    // Ignore if the output is wrong.
                    if (output != null && output.getType() != currentRecipe.getResult().getType()) {
                        return;
                    }

                    // Our FurnaceFuel.
                    final FurnaceFuel furnaceFuel = this.fuel == null ? null : RecipeManager.getInstance().getFurnaceFuel(this.fuel.getType()).orElse(null);

                    // How long one fuel lasts.
                    // In ticks.
                    final int maxFurnaceFuelTime = furnaceFuel != null ? (int) (furnaceFuel.getBurnTime() / properties.getCookMultiplier()) : 0;

                    // How many items one fuel can smelt.
                    // I.E., for coal you can smelt 8 items with it.
                    final int maxItemsPerOneFuel = maxFurnaceFuelTime / recipeCookTime;

                    // We don't have any fuel in the furnace.
                    if ((fuel == null || furnaceFuel == null || fuel.getType().isAir())) {
                        // Get our operations.
                        final int operations = maximumOperations((this.fuelTime + this.cookTime) / recipeCookTime);
                        // Consume the fuel.
                        this.fuelTime -= recipeCookTime * operations;

                        Utils.debugLog("Running " + operations + " smelting operations for " + this.getUniqueId() + ".");
                        for (int i = 0; i < operations; i++) {
                            // Smelt the recipe, looped for all operations.
                            processSmelt();
                        }
                        return;
                    }

                    // Operations we can perform since the last opening.
                    final int operations = Math.min((int) ((ticksSinceLastOpened + this.cookTime) / recipeCookTime), maximumOperations(maxItemsPerOneFuel * fuel.getAmount()));

                    // Total fuel consumed.
                    final int fuelConsumed = operations * recipeCookTime;
                    // Fuel consumed ignoring the current fuel.
                    final int fuelConsumedUpdated = fuelConsumed - (this.fuelTime + this.cookTime);

                    // Consume the current fuel.
                    if (fuelConsumed < this.fuelTime) {
                        this.fuelTime -= fuelConsumed;
                    } else {
                        this.fuelTime = 0;
                    }

                    // How many of the fuel items are consumed.
                    final int fuelItemsConsumed = fuelConsumedUpdated / (maxItemsPerOneFuel * recipeCookTime);
                    // How much remaining fuel we have after our operations.
                    final int remaining = fuelConsumedUpdated % (maxItemsPerOneFuel * recipeCookTime);

                    // Make sure we have a fuel item.
                    if (this.fuel != null) {
                        // Handle the decrement of fuel.
                        if (this.fuel.getAmount() > 1) {
                            this.fuel.setAmount(this.fuel.getAmount() - fuelItemsConsumed);
                        } else {
                            // If a lava bucket, we want to return a bucket.
                            if (this.fuel.getType() == Material.LAVA_BUCKET) {
                                this.fuel.setType(Material.BUCKET);
                            } else this.fuel = null;
                        }
                    }

                    Utils.debugLog("Running " + operations + " smelting operations for " + this.getUniqueId() + ".");
                    for (int i = 0; i < operations; i++) {
                        // Smelt the recipe, looped for all operations.
                        processSmelt();
                    }

                    // Update the fuel time to our remaining amount if valid.
                    if (remaining > 0) {
                        this.fuelTime = remaining;
                    }
                }
            } else if (fuelTime > 0) { // Decrease fuel time even if not smelting?
                fuelTime -= Math.toIntExact(Math.min(ticksSinceLastOpened, totalFuelTime));
            }
        }
    }

    // Utility method to determine the max amount of smelt operations we can perform.
    private int maximumOperations(final int maxItems) {
        int operations;
        // Check if we have an output.
        if (output != null && output.getAmount() > 0) {
            // We do, and we have some in the slot already.
            final ItemMeta outputMeta = output.getItemMeta();
            assert outputMeta != null;
            // Check if we have a custom maximum stack.
            if (outputMeta.hasMaxStackSize()) {
                // We do, use item meta to get the stack size.
                operations = outputMeta.getMaxStackSize() - output.getAmount();
            } else {
                // We don't, use normal stack size.
                operations = output.getMaxStackSize() - output.getAmount();
            }
        } else {
            // We don't have output, assume input is not null.
            final ItemMeta inputMeta = input.getItemMeta();
            assert inputMeta != null;
            final int maxInputSize = inputMeta.hasMaxStackSize() ? inputMeta.getMaxStackSize() : input.getMaxStackSize();

            // Minimize this to max input or the size of the item stack.
            operations = Math.min(input.getAmount(), maxInputSize);
        }

        // Bind to the minimum between how many items we can smelt and our suspected operations.
        return Math.min(maxItems, operations);
    }

    /**
     * Save this Furnace to whichever storage medium is used, this will use this Furnace's {@link UUID}.
     */
    public final void save(final Player player) {
        VirtualManager.getInstance().getStorage().saveFurnace(player, this);
    }

    @Override
    public String toString() {
        return "Furnace{" +
                "properties=" + properties +
                ", inventory=" + inventory +
                ", type=" + type +
                ", fuel=" + fuel +
                ", input=" + input +
                ", output=" + output +
                ", cookTime=" + cookTime +
                ", totalCookTime=" + totalCookTime +
                ", fuelTime=" + fuelTime +
                ", totalFuelTime=" + totalFuelTime +
                ", experience=" + experience +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Furnace furnace = (Furnace) o;
        return getCookTime() == furnace.getCookTime() && getTotalCookTime() == furnace.getTotalCookTime() && getFuelTime() == furnace.getFuelTime() && getTotalFuelTime() == furnace.getTotalFuelTime() && Float.compare(getExperience(), furnace.getExperience()) == 0 && Objects.equals(getProperties(), furnace.getProperties()) && Objects.equals(getInventory(), furnace.getInventory()) && getType() == furnace.getType() && Objects.equals(getFuel(), furnace.getFuel()) && Objects.equals(getInput(), furnace.getInput()) && Objects.equals(getOutput(), furnace.getOutput());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getProperties(), getInventory(), getType(), getFuel(), getInput(), getOutput(), getCookTime(), getTotalCookTime(), getFuelTime(), getTotalFuelTime(), getExperience());
    }
}
