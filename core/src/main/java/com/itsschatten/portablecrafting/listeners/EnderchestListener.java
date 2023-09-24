package com.itsschatten.portablecrafting.listeners;

import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.commands.EnderChestCommand;
import com.itsschatten.portablecrafting.configs.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

public class EnderchestListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onEnderchestClick(InventoryClickEvent event) {
        if (EnderChestCommand.getPlayers().contains(event.getWhoClicked().getUniqueId()) && event.getWhoClicked().getOpenInventory().getType().equals(InventoryType.ENDER_CHEST)) {
            Utils.debugLog("Player " + event.getWhoClicked().getName() + " is contained in the set, and their open inventory is the Enderchest.");

            if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                return;
            }

            if (event.getClick().isShiftClick()) {
                event.setCancelled(true);
                return;
            }

            if (event.getClickedInventory().equals(event.getWhoClicked().getInventory())
                    && (event.getAction().equals(InventoryAction.PLACE_ALL) || event.getAction().equals(InventoryAction.PLACE_SOME)
                    || event.getAction().equals(InventoryAction.PLACE_ONE))) {
                Utils.tell(event.getWhoClicked(), Messages.CANT_RETRIEVE_ITEM_FROM_ENDER);
                Utils.debugLog("Player " + event.getWhoClicked().getName() + " clicked their own inventory with an item. Event canceled, message sent.");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer().getOpenInventory().getType().equals(InventoryType.ENDER_CHEST) && EnderChestCommand.getPlayers().contains(event.getPlayer().getUniqueId())) {
            EnderChestCommand.getPlayers().remove(event.getPlayer().getUniqueId());
            Utils.debugLog("Removed " + event.getPlayer().getName() + " from the enderchest Set.");
        }
    }

}
