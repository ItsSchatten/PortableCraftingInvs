package com.itsschatten.portablecrafting.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SmithingOpenEvent extends PCIEventBase {
    public SmithingOpenEvent(Player player) {
        super(player);
    }
}
