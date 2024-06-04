package com.itsschatten.portablecrafting.events;

import com.itsschatten.portablecrafting.virtual.machine.Furnace;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class FurnaceOpenEvent extends PCIEventBase {
    private final Furnace furnace;

    public FurnaceOpenEvent(Player player, Furnace furnace) {
        super(player);
        this.furnace = furnace;
    }
}
