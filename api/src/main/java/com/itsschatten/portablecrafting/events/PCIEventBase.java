package com.itsschatten.portablecrafting.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the base event for PCI events.
 */
public abstract class PCIEventBase extends Event implements Cancellable {

    // The list of handlers for this event.
    private static final HandlerList handlers = new HandlerList();

    /**
     * The player for this event.
     * -- GETTER --
     * Obtain the player for this event.
     *
     * @return The event's player instance.
     */
    @Getter
    private final Player player;

    /**
     * If this event has been canceled or not.
     * -- SETTER --
     * Set if this event is canceled.
     *
     * @param cancel
     */
    @Setter
    private boolean canceled;

    /**
     * Constructs a new event.
     *
     * @param player The {@link Player} for this event.
     */
    public PCIEventBase(Player player) {
        this.canceled = false;
        this.player = player;
    }

    /**
     * @return Returns this event's {@link HandlerList}.
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return Returns this event's {@link HandlerList}.
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * {@inheritDoc}
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        setCanceled(cancel);
    }

    /**
     * {@inheritDoc}
     *
     * @return Returns {@link #isCanceled()}
     */
    @Override
    public boolean isCancelled() {
        return isCanceled();
    }

    public boolean isCanceled() {
        return canceled;
    }

}
