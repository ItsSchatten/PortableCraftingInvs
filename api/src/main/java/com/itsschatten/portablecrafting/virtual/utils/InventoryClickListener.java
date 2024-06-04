package com.itsschatten.portablecrafting.virtual.utils;

import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.virtual.RecipeManager;
import com.itsschatten.portablecrafting.virtual.VirtualManager;
import com.itsschatten.portablecrafting.virtual.fuel.FurnaceFuel;
import com.itsschatten.portablecrafting.virtual.machine.BrewingStand;
import com.itsschatten.portablecrafting.virtual.machine.Furnace;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Handles clicking in virtual Furnaces and Brewing stands.
 */
public class InventoryClickListener implements Listener {
    /**
     * Used to send a loaded debug message mainly.
     */
    public InventoryClickListener() {
        Utils.debugLog("Loaded the InventoryClickListener class from the virtual tile API.");
    }

    // Handles closing furnaces and brewing stands.
    // Removes them from the active map.
    @EventHandler
    private void onInventoryClose(final @NotNull InventoryCloseEvent event) {
        final Inventory inventory = event.getInventory();

        if (VirtualManager.getInstance().getInventoryFurnaceMap().containsKey(inventory)) {
            // Get the furnace from the map.
            final Furnace furnace = VirtualManager.getInstance().getInventoryFurnaceMap().get(inventory);
            VirtualManager.getInstance().getOpenFurnaces().remove(furnace.getUniqueId());
            VirtualManager.getInstance().getInventoryFurnaceMap().remove(inventory);
            furnace.save((Player) event.getPlayer());
        } else if (VirtualManager.getInstance().getInventoryBrewingStandMap().containsKey(inventory)) {
            // Get the stand from the map.
            final BrewingStand stand = VirtualManager.getInstance().getInventoryBrewingStandMap().get(inventory);
            VirtualManager.getInstance().getOpenBrewingStands().remove(stand.getUniqueId());
            VirtualManager.getInstance().getInventoryBrewingStandMap().remove(inventory);
            stand.save((Player) event.getPlayer());
        }
    }

    // Handles clicking within the inventory.
    @EventHandler
    private void onInventoryClick(@NotNull InventoryClickEvent event) {
        // The clicked inventory.
        final Inventory inventory = event.getInventory();
        // View of the inventory, used to update certain menu feature.
        final InventoryView view = event.getView();
        // Who clicked.
        final HumanEntity clicker = event.getWhoClicked();
        // Clicked slot.
        final int slot = event.getRawSlot();

        // Check if we have a furnace, and that our clicker is a player.
        if (VirtualManager.getInstance().getInventoryFurnaceMap().containsKey(inventory) && clicker instanceof Player) {
            final Furnace furnace = VirtualManager.getInstance().getInventoryFurnaceMap().get(inventory);
            // Give XP to player when they extract from the furnace
            if (slot == 2) {
                final ItemStack output = furnace.getOutput();
                if (output != null) {
                    final float exp = furnace.extractExperience();

                    ((Player) clicker).giveExp((int) exp);
                    event.setCurrentItem(output);
                }
            } else if (slot == 1) {  // Enable putting custom fuels in the furnaces
                final ItemStack cursor = clicker.getItemOnCursor();

                final FurnaceFuel fuel = RecipeManager.getInstance().getFurnaceFuel(cursor.getType()).orElse(null);
                if (fuel != null && isNotVanillaFuel(cursor)) {
                    final ItemStack furnaceFuel = furnace.getFuel();
                    event.setCancelled(true);

                    // Check if we have some fuel ion the furnace, and if the fuel equals what's on the cursor.
                    if (furnaceFuel != null && furnaceFuel.getType() == cursor.getType()) {
                        final int fuelAmount = furnaceFuel.getAmount();
                        final int cursorAmount = cursor.getAmount();
                        final int maxStack = cursor.getType().getMaxStackSize();

                        final ItemStack fuelSlot = view.getItem(1);
                        assert fuelSlot != null;
                        // Check if we are not at max stack size.
                        if (fuelAmount < maxStack) {
                            final int diff = maxStack - fuelAmount;
                            // Check if the amount is less than the diff, if so set the cursor amount to 0 and update the amount.
                            if (cursorAmount < diff) {
                                cursor.setAmount(0);
                                fuelSlot.setAmount(fuelAmount + cursorAmount);
                            } else {
                                cursor.setAmount(cursorAmount - diff);
                                fuelSlot.setAmount(maxStack);
                            }
                        }

                    } else {
                        // Handel swapping the cursor and the furnace fuel.
                        final ItemStack oldCursor = cursor.clone();
                        clicker.setItemOnCursor(furnaceFuel);
                        event.getView().setItem(1, oldCursor);
                    }
                }
            }
        }
    }

    /**
     * Checks if an {@link ItemStack} is a registered VANILLA_FUEL.
     *
     * @param itemStack The {@link ItemStack} to check.
     * @return true if it doesn't match, false if it does.
     */
    private boolean isNotVanillaFuel(ItemStack itemStack) {
        for (FurnaceFuel fuel : FurnaceFuel.VANILLA_FUELS) {
            if (fuel.getFuel() == itemStack.getType()) {
                return false;
            }
        }
        return true;
    }

}
