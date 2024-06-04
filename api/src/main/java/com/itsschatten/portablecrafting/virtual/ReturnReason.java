package com.itsschatten.portablecrafting.virtual;

/**
 * A reason that was returned when opening a virtual tile.
 */
public enum ReturnReason {

    /**
     * The inventory was opened successfully.
     */
    OPENED,
    /**
     * Reached the maximum allowed virtual tile.
     */
    REACHED_MAXIMUM,
    /**
     * The open event was canceled.
     */
    EVENT_CANCELED

}
