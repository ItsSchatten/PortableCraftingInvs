package com.itsschatten.portablecrafting.events;

import com.shanebeestudios.api.machine.Furnace;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class FurnaceOpenEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Furnace furnace;
    private boolean cancelled;

    public FurnaceOpenEvent(Player player, Furnace furnace) {
        this.player = player;
        this.furnace = furnace;
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
