package com.itsschatten.portablecrafting;

import com.itsschatten.portablecrafting.virtual.ReturnReason;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Interface used to open the containers for PCI.
 * <p></p>
 * Containers opened from this API will all function as proper containers.
 */
public interface PCIAPI {

    /**
     * Opens an Anvil for the provided player.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code> if an error was thrown or an event canceled the opening.
     */
    boolean openAnvil(Player player);

    /**
     * Opens a cartography table for the player.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code> if an error was thrown or an event canceled the opening.
     */
    boolean openCartography(Player player);

    /**
     * Opens an enchanting table for the player.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code> if an error was thrown or an event canceled the opening.
     */
    boolean openEnchant(Player player);

    /**
     * Opens an enchanting table for the player with the provided maximum level.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param maxLvl The maximum level that should be used for this enchantment table.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code> if an error was thrown or an event canceled the opening.
     */
    boolean openEnchant(Player player, int maxLvl);

    /**
     * Opens a grindstone for a player.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code> if an error was thrown or an event canceled the opening.
     */
    boolean openGrindStone(Player player);

    /**
     * Opens a loom for a player.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code> if an error was thrown or an event canceled the opening.
     */
    boolean openLoom(Player player);

    /**
     * Opens a stone cutter for a player.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code> if an error was thrown or an event canceled the opening.
     */
    boolean openStoneCutter(Player player);

    /**
     * Opens a smithing table for the player.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code> if an error was thrown or an event canceled the opening.
     */
    boolean openSmithing(Player player);

    /**
     * Opens the player's blast furnace for them.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code>
     * if an error was thrown, an event canceled the opening, or if the API to open a furnace has been disabled.
     * @see ReturnReason
     * @see #queryVirtualTileAPI()
     */
    default ReturnReason openBlastFurnace(Player player) {
        return openBlastFurnace(player, 0);
    }

    /**
     * Opens the player's smoker for them.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     * @see #queryVirtualTileAPI()
     */
    default ReturnReason openSmoker(Player player) {
        return openSmoker(player, 0);
    }

    /**
     * Opens the player's furnace for them.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     * @see #queryVirtualTileAPI()
     */
    default ReturnReason openFurnace(Player player) {
        return openFurnace(player, 0);
    }

    /**
     * Queries the current status of the furnace and brewing stand API.
     *
     * @return <code>true</code> if enabled, <code>false</code> otherwise.
     */
    boolean queryVirtualTileAPI();

    /**
     * Opens the player's brewing stand for them.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     * @see #queryVirtualTileAPI()
     */
    default ReturnReason openBrewingStand(Player player) {
        return openBrewingStand(player, 0);
    }

    /**
     * Opens the player's blast furnace for them.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param number The number of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     * @see #queryVirtualTileAPI()
     */
    ReturnReason openBlastFurnace(Player player, int number);

    /**
     * Opens the player's smoker for them.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param number The number of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     * @see #queryVirtualTileAPI()
     */
    ReturnReason openSmoker(Player player, int number);

    /**
     * Opens the player's furnace for them.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param number The number of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     * @see #queryVirtualTileAPI()
     */
    ReturnReason openFurnace(Player player, int number);

    /**
     * Opens the player's brewing stand for them.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param number The number of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     * @see #queryVirtualTileAPI()
     */
    ReturnReason openBrewingStand(Player player, int number);

    /**
     * Opens the player's blast furnace for them.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param title  The title for this Furnace.
     * @param number The number of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     * @see #queryVirtualTileAPI()
     */
    ReturnReason openBlastFurnace(Player player, String title, int number);

    /**
     * Opens the player's smoker for them.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param title  The title for this Furnace.
     * @param number The number of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     * @see #queryVirtualTileAPI()
     */
    ReturnReason openSmoker(Player player, String title, int number);

    /**
     * Opens the player's furnace for them.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param title  The title for this Furnace.
     * @param number The number of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     * @see #queryVirtualTileAPI()
     */
    ReturnReason openFurnace(Player player, String title, int number);

    /**
     * Opens the player's brewing stand for them.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param title  The title for this Furnace.
     * @param number The number of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     * @see #queryVirtualTileAPI()
     */
    ReturnReason openBrewingStand(Player player, String title, int number);

    /**
     * Opens the player's blast furnace for them.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param id     The id of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     * @see #queryVirtualTileAPI()
     */
    ReturnReason openBlastFurnace(Player player, UUID id);

    /**
     * Opens the player's smoker for them.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param id     The id of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     * @see #queryVirtualTileAPI()
     */
    ReturnReason openSmoker(Player player, UUID id);

    /**
     * Opens the player's furnace for them.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param id     The id of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     * @see #queryVirtualTileAPI()
     */
    ReturnReason openFurnace(Player player, UUID id);

    /**
     * Opens the player's brewing stand for them.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param id     The id of the virtual tile to open.
     * @return A {@link ReturnReason}.
     * @see ReturnReason
     * @see #queryVirtualTileAPI()
     */
    ReturnReason openBrewingStand(Player player, UUID id);

}
