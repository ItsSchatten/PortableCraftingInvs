package com.itsschatten.portablecrafting.events;

import org.bukkit.entity.Player;

public class CraftingOpenEvent extends PCIEventBase {
    public CraftingOpenEvent(Player player) {
        super(player);
    }
}
