package com.itsschatten.portablecrafting.events;

import com.shanebeestudios.api.machine.BrewingStand;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class BrewingOpenEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final BrewingStand stand;
    private boolean cancelled;

    public BrewingOpenEvent(Player player, BrewingStand stand) {
        this.player = player;
        this.stand = stand;
        this.cancelled = false;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
