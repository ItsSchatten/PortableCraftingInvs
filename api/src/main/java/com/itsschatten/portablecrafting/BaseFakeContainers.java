package com.itsschatten.portablecrafting;

import com.itsschatten.portablecrafting.virtual.ReturnReason;
import com.itsschatten.portablecrafting.virtual.VirtualManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class BaseFakeContainers implements PCIAPI {

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param number The number of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     */
    @Override
    public final ReturnReason openBlastFurnace(Player player, int number) {
        if (queryVirtualTileAPI()) return VirtualManager.getInstance().openBlasting(player, number);
        else
            throw new UnsupportedOperationException("The virtual tile functionality has been disabled, not loaded properly, or crashed.");
    }

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param number The number of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     */
    @Override
    public final ReturnReason openSmoker(Player player, int number) {
        if (queryVirtualTileAPI()) return VirtualManager.getInstance().openSmoker(player, number);
        else
            throw new UnsupportedOperationException("The virtual tile functionality has been disabled, not loaded properly, or crashed.");
    }


    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param number The number of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     */
    @Override
    public final ReturnReason openFurnace(Player player, int number) {
        if (queryVirtualTileAPI()) return VirtualManager.getInstance().openFurnace(player, number);
        else
            throw new UnsupportedOperationException("The virtual tile functionality has been disabled, not loaded properly, or crashed.");
    }

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param number The number of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     */
    @Override
    public final ReturnReason openBrewingStand(Player player, int number) {
        if (queryVirtualTileAPI()) return VirtualManager.getInstance().openBrewingStand(player, number);
        else
            throw new UnsupportedOperationException("The virtual tile functionality has been disabled, not loaded properly, or crashed.");
    }

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param title  The title for this Furnace.
     * @param number The number of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     */
    @Override
    public final ReturnReason openBlastFurnace(Player player, String title, int number) {
        if (queryVirtualTileAPI()) return VirtualManager.getInstance().openBlasting(player, number);
        else
            throw new UnsupportedOperationException("The virtual tile functionality has been disabled, not loaded properly, or crashed.");
    }

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param title  The title for this Furnace.
     * @param number The number of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     */
    @Override
    public final ReturnReason openSmoker(Player player, String title, int number) {
        if (queryVirtualTileAPI()) return VirtualManager.getInstance().openSmoker(player, number);
        else
            throw new UnsupportedOperationException("The virtual tile functionality has been disabled, not loaded properly, or crashed.");
    }

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param title  The title for this Furnace.
     * @param number The number of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     */
    @Override
    public final ReturnReason openFurnace(Player player, String title, int number) {
        if (queryVirtualTileAPI()) return VirtualManager.getInstance().openFurnace(player, number);
        else
            throw new UnsupportedOperationException("The virtual tile functionality has been disabled, not loaded properly, or crashed.");
    }

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param title  The title for this Furnace.
     * @param number The number of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     */
    @Override
    public final ReturnReason openBrewingStand(Player player, String title, int number) {
        if (queryVirtualTileAPI()) return VirtualManager.getInstance().openBrewingStand(player, number);
        else
            throw new UnsupportedOperationException("The virtual tile functionality has been disabled, not loaded properly, or crashed.");
    }

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param id     The id of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     */
    @Override
    public final ReturnReason openBlastFurnace(Player player, UUID id) {
        if (queryVirtualTileAPI()) return VirtualManager.getInstance().openBlasting(player, id);
        else
            throw new UnsupportedOperationException("The virtual tile functionality has been disabled, not loaded properly, or crashed.");
    }

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param id     The id of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     */
    @Override
    public final ReturnReason openSmoker(Player player, UUID id) {
        if (queryVirtualTileAPI()) return VirtualManager.getInstance().openSmoker(player, id);
        else
            throw new UnsupportedOperationException("The virtual tile functionality has been disabled, not loaded properly, or crashed.");
    }

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param id     The id of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     */
    @Override
    public final ReturnReason openFurnace(Player player, UUID id) {
        if (queryVirtualTileAPI()) return VirtualManager.getInstance().openFurnace(player, id);
        else
            throw new UnsupportedOperationException("The virtual tile functionality has been disabled, not loaded properly, or crashed.");
    }

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param id     The id of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     */
    @Override
    public final ReturnReason openBrewingStand(Player player, UUID id) {
        if (queryVirtualTileAPI()) return VirtualManager.getInstance().openBrewingStand(player, id);
        else
            throw new UnsupportedOperationException("The virtual tile functionality has been disabled, not loaded properly, or crashed.");
    }

    /**
     * {@inheritDoc}
     *
     * @return <code>true</code> if enabled, <code>false</code> otherwise.
     */
    @Override
    public final boolean queryVirtualTileAPI() {
        return VirtualManager.getInstance() != null && VirtualManager.getInstance().isLoaded();
    }


}
