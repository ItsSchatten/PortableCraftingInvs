package com.itsschatten.portablecrafting.virtual;

import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.events.BrewingOpenEvent;
import com.itsschatten.portablecrafting.events.FurnaceOpenEvent;
import com.itsschatten.portablecrafting.virtual.machine.BrewingStand;
import com.itsschatten.portablecrafting.virtual.machine.Furnace;
import com.itsschatten.portablecrafting.virtual.machine.properties.BlastingProperties;
import com.itsschatten.portablecrafting.virtual.machine.properties.BrewingProperties;
import com.itsschatten.portablecrafting.virtual.machine.properties.FurnaceProperties;
import com.itsschatten.portablecrafting.virtual.machine.properties.SmokerProperties;
import com.itsschatten.portablecrafting.virtual.utils.FurnaceType;
import com.itsschatten.portablecrafting.virtual.utils.InventoryClickListener;
import com.itsschatten.portablecrafting.virtual.utils.TickTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The main manager responsible for controlling virtual tiles.
 */
@Getter
public class VirtualManager {

    // Config serialization registration.
    static {
        ConfigurationSerialization.registerClass(Furnace.class, "furnace");
        ConfigurationSerialization.registerClass(FurnaceProperties.class, "furnace_properties");
        ConfigurationSerialization.registerClass(SmokerProperties.class, "smoker_properties");
        ConfigurationSerialization.registerClass(BlastingProperties.class, "blasting_properties");
        ConfigurationSerialization.registerClass(BrewingStand.class, "brewing_stand");
        ConfigurationSerialization.registerClass(BrewingProperties.class, "brewing_stand_properties");
    }

    /**
     * Storage medium, implemented in the main plugin.
     */
    private final Storage storage;

    /**
     * Settings to use in the API.
     */
    @Setter // Required to ensure valid reload data.
    private ISettings settings;

    // All open furnaces, regardless of type.
    private final Map<UUID, Furnace> openFurnaces;
    // All open brewing stands.
    private final Map<UUID, BrewingStand> openBrewingStands;

    // All open furnaces, regardless of type.
    private final Map<Inventory, Furnace> inventoryFurnaceMap;
    // All open brewing stands.
    private final Map<Inventory, BrewingStand> inventoryBrewingStandMap;

    /**
     * The instance of this class.
     */
    @Getter
    private static VirtualManager instance;

    /**
     * If this class has been called.
     */
    private final boolean loaded;

    /**
     * The ticking task for this instance.
     */
    private final TickTask tickTask;

    /**
     * Registers an instance, sets that it has been loaded, and registers required managers and classes.
     */
    public VirtualManager(final Storage storage, final ISettings settings) {
        instance = this;
        this.openFurnaces = new HashMap<>();
        this.openBrewingStands = new HashMap<>();
        this.inventoryBrewingStandMap = new HashMap<>();
        this.inventoryFurnaceMap = new HashMap<>();
        this.loaded = true;

        this.storage = storage;
        this.settings = settings;

        new RecipeManager();
        // The tick task, used to tick any open brewing stands or furnaces.
        this.tickTask = new TickTask();

        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickListener(), Utils.getInstance());
    }

    /**
     * Handles saving furnaces and brewing stands, cancels tasks, closes the storage medium if required, and other what nots.
     */
    public final void shutdown() {
        this.tickTask.cancel();

        if (storage != null) {
            storage.shutdown();
        }
    }

    /**
     * Opens or creates a {@link BlastingProperties Blast} {@link Furnace Furnace} for a player.
     *
     * @param player The player to open the furnace for.
     * @param number The number of this furnace, used to check if a furnace exists in the player's furnace array.
     * @return Returns if the brewing stand has been opened.
     */
    public final ReturnReason openBlasting(final Player player, final int number) {
        return openFurnace(FurnaceType.BLASTING, player, number);
    }

    /**
     * Opens a {@link BlastingProperties Blast} {@link Furnace Furnace} for a player.
     *
     * @param player The player to open the furnace for.
     * @param id     The {@link UUID} for the furnace to open, used to track it in storage.
     * @return Returns if the brewing stand has been opened.
     * @throws UnsupportedOperationException Thrown when a furnace doesn't exist.
     */
    public final ReturnReason openBlasting(final Player player, final UUID id) throws UnsupportedOperationException {
        return openFurnace(player, id);
    }

    /**
     * Opens or creates a {@link SmokerProperties Smoker} {@link Furnace Furnace} for a player.
     *
     * @param player The player to open the furnace for.
     * @param number The number of this furnace, used to check if a furnace exists in the player's furnace array.
     * @return Returns if the brewing stand has been opened.
     */
    public final ReturnReason openSmoker(final Player player, final int number) {
        return openFurnace(FurnaceType.SMOKER, player, number);
    }

    /**
     * Opens a {@link SmokerProperties Smoker} {@link Furnace Furnace} for a player.
     *
     * @param player The player to open the furnace for.
     * @param id     The {@link UUID} for the furnace to open, used to track it in storage.
     * @return Returns if the brewing stand has been opened.
     * @throws UnsupportedOperationException Thrown when a furnace doesn't exist.
     */
    public final ReturnReason openSmoker(final Player player, final UUID id) throws UnsupportedOperationException {
        return openFurnace(player, id);
    }

    /**
     * Opens or creates a {@link Furnace Furnace} for a player.
     *
     * @param player The player to open the furnace for.
     * @param number The number of this furnace, used to check if a furnace exists in the player's furnace array.
     * @return Returns if the brewing stand has been opened.
     */
    public final ReturnReason openFurnace(final Player player, final int number) {
        return openFurnace(FurnaceType.FURNACE, player, number);
    }

    /**
     * Opens a {@link Furnace Furnace} for a player.
     *
     * @param player The player to open the furnace for.
     * @param id     The {@link UUID} for the furnace to open, used to track it in storage.
     * @return Returns if the brewing stand has been opened.
     * @throws UnsupportedOperationException Thrown when a furnace doesn't exist.
     */
    public final ReturnReason openFurnace(final Player player, final UUID id) throws UnsupportedOperationException {
        final Furnace furnace = getFurnace(player, id);
        if (furnace == null) {
            throw new UnsupportedOperationException("Cannot create a furnace with a predefined UUID, please call #openFurnace(type, player, integer).");
        }

        return openFurnace(player, furnace);
    }

    /**
     * Opens or creates any {@link Furnace Furnace} for a player.
     *
     * @param type   The {@link FurnaceType} to track, mainly used to differentiate between properties.
     * @param player The player to open the furnace for.
     * @param number The number of this furnace, used to check if a furnace exists in the player's furnace array.
     * @return Returns if the brewing stand has been opened.
     */
    public final ReturnReason openFurnace(final FurnaceType type, final Player player, final int number) {
        return openFurnace(player, getFurnace(type, player, number));
    }

    /**
     * Creates and opens any {@link Furnace} for a player.
     *
     * @param type   The {@link FurnaceType} to find.
     * @param player The player to search for a furnace with.
     * @param title  Title of the furnace.
     * @return Returns if the brewing stand has been opened.
     */
    public final ReturnReason openFurnace(final FurnaceType type, final Player player, final String title) {
        return openFurnace(player, getFurnace(type, player, title));
    }

    /**
     * Properly opens a furnace.
     *
     * @param player   The player to open the furnace for.
     * @param furnace2 The furnace to open.
     * @return Returns {@code true} if opened, {@code false} if the event was canceled.
     */
    private ReturnReason openFurnace(Player player, Furnace furnace2) {
        if (furnace2 == null) {
            // FIXME: May not be true.
            return ReturnReason.REACHED_MAXIMUM;
        }

        final FurnaceOpenEvent event = new FurnaceOpenEvent(player, furnace2);
        if (event.isCanceled()) return ReturnReason.EVENT_CANCELED;
        furnace2.openInventory(player);

        inventoryFurnaceMap.put(furnace2.getInventory(), furnace2);
        openFurnaces.put(furnace2.getUniqueId(), furnace2);
        return ReturnReason.OPENED;
    }

    /**
     * Creates or returns a {@link Furnace}.
     *
     * @param type   The {@link FurnaceType} to find.
     * @param player The player to search for a furnace with.
     * @param number The number of the furnace to return.
     * @return Returns either a new {@link Furnace} or generates one from values in storage.
     */
    @Contract("_, _, _ -> new")
    public final @Nullable Furnace getFurnace(final FurnaceType type, final Player player, final int number) {
        final UUID uuid = storage.getFurnaceUUIDFromID(player, number, type);
        if (uuid != null) {
            return storage.loadFurnace(player, uuid);
        }

        if (!player.hasPermission("pci.furnace.limit.bypass") && storage.getPlayerFurnaceCount(player, type) >= settings.maximumFurnaces()) {
            return null;
        }

        return new Furnace(type.getName(), type, type.getProperties());
    }

    /**
     * Creates a new furnace with the provided title.
     *
     * @param type   The {@link FurnaceType} for this Furnace.
     * @param player The player, used to check if a furnace exists.
     * @param title  The title to be applied to this furnace.
     * @return Returns a brand-new furnace.
     */
    @Contract("_, _, _ -> new")
    public final @NotNull Furnace getFurnace(final FurnaceType type, final Player player, final String title) {
        return new Furnace(title, type, type.getProperties());
    }

    /**
     * Returns a {@link Furnace}.
     *
     * @param id The {@link UUID} of the Furnace to open.
     * @return Returns the furnace if one is found, otherwise <code>null</code>.
     */
    @Contract(pure = true)
    public final @Nullable Furnace getFurnace(final @NotNull Player player, final UUID id) {
        return getFurnace(player.getUniqueId(), id);
    }

    /**
     * Returns a {@link Furnace}.
     *
     * @param id The {@link UUID} of the Furnace to open.
     * @return Returns the furnace if one is found, otherwise <code>null</code>.
     */
    @Contract(pure = true)
    public final @Nullable Furnace getFurnace(final UUID uuid, final UUID id) {
        if (openFurnaces.containsKey(id)) {
            return openFurnaces.get(id);
        }

        return storage.loadFurnace(uuid, id);
    }

    /**
     * Opens or creates the first {@link BrewingStand} for the provided player.
     *
     * @param player The player to open the brewing stand for.
     * @return Returns if the brewing stand has been opened.
     */
    public final ReturnReason openBrewingStand(final Player player) {
        return openBrewingStand(player, 0);
    }

    /**
     * Opens or creates a {@link BrewingStand}.
     *
     * @param player The player to open the brewing stand for.
     * @param number The brewing stand number.
     * @return Returns if the brewing stand has been opened.
     */
    public final ReturnReason openBrewingStand(final Player player, final int number) {
        final BrewingStand stand = getBrewingStand(player, number);
        if (stand == null) {
            // FIXME: May not be true.
            return ReturnReason.REACHED_MAXIMUM;
        }

        return openStand(player, stand);
    }

    /**
     * Opens a {@link BrewingStand} for a player.
     *
     * @param player The player to open the brewing stand for.
     * @param id     The UUID of the brewing stand.
     * @return Returns if the brewing stand has been opened.
     * @throws UnsupportedOperationException Thrown when a brewing stand doesn't exist.
     */
    public final ReturnReason openBrewingStand(final Player player, final UUID id) throws UnsupportedOperationException {
        final BrewingStand stand = getBrewingStand(player, id);
        if (stand == null)
            throw new UnsupportedOperationException("Cannot create a brewing stand with a predefined UUID, please call #openBrewingStand(player, integer).");

        return openStand(player, stand);
    }

    /**
     * Properly opens a brewing stand.
     *
     * @param player The player to open the brewing stand for.
     * @param stand  The brewing stand to open.
     * @return Returns {@code true} if opened, {@code false} if the event was canceled.
     */
    private ReturnReason openStand(Player player, BrewingStand stand) {
        final BrewingOpenEvent event = new BrewingOpenEvent(player, stand);
        if (event.isCanceled()) return ReturnReason.EVENT_CANCELED;

        stand.openInventory(player);

        openBrewingStands.put(stand.getUniqueId(), stand);
        inventoryBrewingStandMap.put(stand.getInventory(), stand);
        return ReturnReason.OPENED;
    }

    /**
     * Creates or returns a {@link BrewingStand}.
     *
     * @param player The player to search for a furnace with.
     * @param number The number of the brewing stand to return, from the brewing stand array.
     * @return Returns either a new {@link BrewingStand} or generates one from values in storage.
     */
    @Contract(pure = true)
    public final @Nullable BrewingStand getBrewingStand(final Player player, final int number) {
        final UUID uuid = storage.getBrewingStandUUIDFromID(player, number);
        if (uuid != null) {
            return storage.loadBrewingStand(player, uuid);
        }

        if (!player.hasPermission("pci.brewing.limit.bypass") && storage.getPlayerBrewingStandCount(player) >= settings.maximumBrewingStands()) {
            return null;
        }

        return new BrewingStand("Brewing Stand", BrewingProperties.NORMAL);
    }

    /**
     * Returns a {@link BrewingStand}.
     *
     * @param id The {@link UUID} of the BrewingStand to open.
     * @return Returns the brewing stand if one is found, otherwise <code>null</code>.
     */
    @Contract(pure = true)
    public final @Nullable BrewingStand getBrewingStand(final Player player, final UUID id) {
        return getBrewingStand(player.getUniqueId(), id);
    }

    /**
     * Returns a {@link BrewingStand}.
     *
     * @param id The {@link UUID} of the BrewingStand to open.
     * @return Returns the brewing stand if one is found, otherwise <code>null</code>.
     */
    @Contract(pure = true)
    public final @Nullable BrewingStand getBrewingStand(final UUID uuid, final UUID id) {
        if (openBrewingStands.containsKey(id)) {
            return openBrewingStands.get(id);
        }
        return storage.loadBrewingStand(uuid, id);
    }

    /**
     * Forcefully closes all virtual tiles.
     */
    public final void closeAllVirtualTiles() {
        getOpenFurnaces().forEach((uuid, furnace) -> furnace.forceCloseInventory());
        getOpenBrewingStands().forEach((uuid, stand) -> stand.forceCloseInventory());

        Utils.debugLog("All virtual tiles should have been closed.");
    }

    /**
     * @return Returns the total open ticking virtual tiles.
     */
    public final int totalTickingVirtualTiles() {
        return openFurnaces.size() + openBrewingStands.size();
    }

    /**
     * @return Returns all unique players that have made a brewing stand and furnace.
     */
    public final int allUniquePlayers() {
        return storage.getUniquePlayers();
    }
}
