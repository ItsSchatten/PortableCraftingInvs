package com.itsschatten.portablecrafting.events;

import lombok.Getter;
import org.bukkit.entity.Player;

public class EnchantingOpenEvent extends PCIEventBase {
    public EnchantingOpenEvent(Player player) {
        super(player);
    }
}
