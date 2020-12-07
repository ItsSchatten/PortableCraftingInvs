package com.itsschatten.portablecrafting.listeners;

import com.itsschatten.portablecrafting.commands.AnvilCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

public class AnvilListeners implements Listener {

    @EventHandler
    public void onAnvilClose(final InventoryCloseEvent event) {
        if (AnvilCommand.getActiveAnvils().contains(event.getPlayer().getUniqueId())
                && event.getInventory().getType() == InventoryType.ANVIL) {
            if (event.getInventory().getItem(0) != null) {
                event.getPlayer().getInventory().addItem(event.getInventory().getItem(0));
            }

            if (event.getInventory().getItem(1) != null) {
                event.getPlayer().getInventory().addItem(event.getInventory().getItem(1));
            }

            AnvilCommand.getActiveAnvils().remove(event.getPlayer().getUniqueId());
        }
    }

}
