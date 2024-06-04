package com.itsschatten.portablecrafting.events;

import com.itsschatten.portablecrafting.virtual.machine.BrewingStand;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class BrewingOpenEvent extends PCIEventBase {

    private final BrewingStand stand;

    public BrewingOpenEvent(Player player, BrewingStand stand) {
        super(player);
        this.stand = stand;
    }
}
