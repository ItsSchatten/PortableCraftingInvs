package com.itsschatten.portablecrafting;

import org.bukkit.entity.Player;

/**
 * Internally used class used for the main PCI plugin
 * <p>
 * All API functionality is stored in {@link PCIFakeContainers}
 */
public interface FakeContainers extends PCIFakeContainers {

    /**
     * Sets if furnaces and brewing stands are stored using an SQL database and thus if we should enforce SQL support.
     *
     * @param bool If we should be using SQL support or not.
     */
    void setUsingMysql(boolean bool);

    /**
     * Sets if we are in DEBUG mode for the PCI plugin, if debug is enabled it will send certain information to the console log.
     * <p>
     * This may only be useful to PCI development; if using the API it may be recommended to implement debug messages into your project.
     *
     * @param bool If we are using DEBUG mode.
     */
    void setDebug(boolean bool);

    /**
     * Removes players from a map if they have a custom level enchantment table open.
     *
     * @param player The {@link Player} we should attempt to remove.
     */
    void removeFromEnchantList(Player player);
}
