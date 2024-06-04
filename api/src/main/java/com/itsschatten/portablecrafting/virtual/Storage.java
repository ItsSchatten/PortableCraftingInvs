package com.itsschatten.portablecrafting.virtual;

import com.itsschatten.portablecrafting.virtual.machine.BrewingStand;
import com.itsschatten.portablecrafting.virtual.machine.Furnace;
import com.itsschatten.portablecrafting.virtual.utils.FurnaceType;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface Storage {

    /**
     * @return The implementation name.
     */
    String implementationName();

    /**
     * Saves a furnace to the storage medium.
     *
     * @param player  The player to save for.
     * @param furnace The furnace to save.
     */
    default void saveFurnace(final Player player, final Furnace furnace) {
        saveFurnace(player.getUniqueId(), furnace);
    }

    /**
     * Saves a furnace to the storage medium.
     *
     * @param uuid    The uuid to save for.
     * @param furnace The furnace to save.
     */
    void saveFurnace(final UUID uuid, final Furnace furnace);

    /**
     * Loads a furnace from the storage medium.
     *
     * @param player The owner of the furnace.
     * @param uuid   The UUID of the furnace.
     * @return Returns the furnace, might be null.
     */
    default Furnace loadFurnace(final Player player, final UUID uuid) {
        return loadFurnace(player.getUniqueId(), uuid);
    }

    /**
     * Loads a furnace from the storage medium.
     *
     * @param owner The owner of the furnace.
     * @param uuid  The UUID of the furnace.
     * @return Returns the furnace, might be null.
     */
    Furnace loadFurnace(final UUID owner, final UUID uuid);

    /**
     * Saves a brewing stand to the storage medium.
     *
     * @param player       The player to save for.
     * @param brewingStand The brewing stand to save.
     */
    default void saveBrewingStand(final Player player, final BrewingStand brewingStand) {
        saveBrewingStand(player.getUniqueId(), brewingStand);
    }

    /**
     * Saves a brewing stand to the storage medium.
     *
     * @param uuid         The uuid to save for.
     * @param brewingStand The brewing stand to save.
     */
    void saveBrewingStand(final UUID uuid, final BrewingStand brewingStand);


    /**
     * Loads a brewing stand from the storage medium.
     *
     * @param player The owner of the brewing stand.
     * @param uuid   The UUID of the brewing stand.
     * @return Returns the brewing stand, might be null.
     */
    default BrewingStand loadBrewingStand(final Player player, final UUID uuid) {
        return loadBrewingStand(player.getUniqueId(), uuid);
    }

    /**
     * Loads a brewing stand from the storage medium.
     *
     * @param owner The owner of the brewing stand.
     * @param uuid  The UUID of the brewing stand.
     * @return Returns the brewing stand, might be null.
     */
    BrewingStand loadBrewingStand(final UUID owner, final UUID uuid);

    /**
     * Gets a Furnace UUID based on the Type and the location in a list of all furnaces for the player.
     *
     * @param player The player to get the UUID from.
     * @param id     The ID in the list to get the UUID.
     * @param type   The type of furnace to filter.
     * @return Returns a UUID if one is found, otherwise null.
     */
    default UUID getFurnaceUUIDFromID(final Player player, final Integer id, final FurnaceType type) {
        return getFurnaceUUIDFromID(player.getUniqueId(), id, type);
    }

    /**
     * Gets a Furnace UUID based on the Type and the location in a list of all furnaces for the player.
     *
     * @param uuid The player to get the UUID from.
     * @param id   The ID in the list to get the UUID.
     * @param type The type of furnace to filter.
     * @return Returns a UUID if one is found, otherwise null.
     */
    UUID getFurnaceUUIDFromID(final UUID uuid, final Integer id, final FurnaceType type);

    /**
     * Gets a Brewing Stand UUID based on the location in a list of all brewing stands for the player.
     *
     * @param player The player to get the UUID from.
     * @param id     The ID in the list to get the UUID.
     * @return Returns a UUID if one is found, otherwise null.
     */
    default UUID getBrewingStandUUIDFromID(final Player player, final Integer id) {
        return getBrewingStandUUIDFromID(player.getUniqueId(), id);
    }

    /**
     * Gets a Brewing Stand UUID based on the location in a list of all brewing stands for the player.
     *
     * @param owner The player to get the UUID from.
     * @param id    The ID in the list to get the UUID.
     * @return Returns a UUID if one is found, otherwise null.
     */
    UUID getBrewingStandUUIDFromID(final UUID owner, final Integer id);

    /**
     * Get the number of furnaces for a player.
     *
     * @param player The player to get the count for.
     * @param type   The furnace type to count.
     * @return The number of furnaces found, defaults to 0 on errors.
     */
    int getPlayerFurnaceCount(final Player player, final FurnaceType type);

    /**
     * Get the number of brewing stands for a player.
     *
     * @param player The player to get the count for.
     * @return The number of furnaces found, defaults to 0 on errors.
     */
    int getPlayerBrewingStandCount(final Player player);

    /**
     * What to do on shutdown.
     */
    void shutdown();

    /**
     * @return Return the number of unique players that have a brewing stand or furnace.
     */
    int getUniquePlayers();

}
