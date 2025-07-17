package com.itsschatten.portablecrafting;

import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.events.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.RandomSourceWrapper;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

/**
 * This class uses built in PaperMC API to open normally unusable inventories.
 * This class also relies on the common API for opening furnaces.
 */
public class FakeContainersPaper extends BaseFakeContainers {

    private final static String SUPPORTED = "1.21.7";

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code> if an error was thrown or an event canceled the opening.
     */
    @Override
    public boolean openAnvil(@NotNull Player player) {
        final AnvilOpenEvent event = new AnvilOpenEvent(player);
        // Check if our event has been canceled.
        if (event.isCanceled()) {
            return false;
        }
        // Forcefully open an anvil inventory at the player's location.
        player.openAnvil(player.getLocation(), true);

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code> if an error was thrown or an event canceled the opening.
     */
    @Override
    public boolean openCartography(Player player) {
        final CartographyOpenEvent event = new CartographyOpenEvent(player);
        // Check if our event has been canceled.
        if (event.isCanceled()) {
            return false;
        }
        // Forcefully open a cartography table at the player's location.
        player.openCartographyTable(player.getLocation(), true);

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code> if an error was thrown or an event canceled the opening.
     */
    @Override
    public boolean openEnchant(@NotNull Player player) {
        final EnchantingOpenEvent event = new EnchantingOpenEvent(player);
        // Check if our event has been canceled.
        if (event.isCanceled()) {
            return false;
        }
        // Forcefully open an enchanting table at the player's location.
        player.openEnchanting(player.getLocation(), true);
        return true;
    }

    /**
     * {@inheritDoc}
     * <br />
     * This is the only method that requires NMS and may break between versions.
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @param maxLvl The maximum level that should be used for this enchantment table.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code> if an error was thrown or an event canceled the opening.
     */
    @Override
    public boolean openEnchant(Player player, int maxLvl) {
        if (!Bukkit.getMinecraftVersion().equals(SUPPORTED)) {
            Utils.logWarning("[WARNING] Opening an enchantment menu with a max level provided may not function and may throw an error.");
            Utils.logWarning("[WARNING] Paper support is built to run exclusively on the latest version.");
            Utils.logWarning("[WARNING] Bug reports regarding inaccurate enchantments on versions less than " + SUPPORTED + " will be closed.");
        }

        Utils.debugLog(player.getName() + " called the openEnchant with the maxLvl parameters. This may break in-between versions.");
        try {
            final ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
            final int containerID = ePlayer.nextContainerCounter();
            final FakeEnchant fakeEnchant = new FakeEnchant(containerID, player, maxLvl);

            final EnchantingOpenEvent event = new EnchantingOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCanceled()) {
                player.openInventory(fakeEnchant.getBukkitView());
                ePlayer.containerMenu = ePlayer.inventoryMenu;
                ePlayer.connection.send(new ClientboundOpenScreenPacket(containerID, MenuType.ENCHANTMENT, fakeEnchant.getTitle()));
                ePlayer.containerMenu = fakeEnchant;
                ePlayer.initMenu(fakeEnchant);
                return true;
            }
            return false;
        } catch (Exception e) {
            Utils.logError(e);
            player.sendMessage("Something went wrong while opening the enchantment menu.");
            return false;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code> if an error was thrown or an event canceled the opening.
     */
    @Override
    public boolean openGrindStone(Player player) {
        final GrindStoneOpenEvent event = new GrindStoneOpenEvent(player);
        // Check if the event is canceled.
        if (event.isCanceled()) {
            return false;
        }
        // Forcefully open a grindstone at the player's location.
        player.openGrindstone(player.getLocation(), true);

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code> if an error was thrown or an event canceled the opening.
     */
    @Override
    public boolean openLoom(Player player) {
        final LoomOpenEvent event = new LoomOpenEvent(player);
        // Check if the event is canceled.
        if (event.isCanceled()) {
            return false;
        }
        // Forcefully open a loom at the player's location.
        player.openLoom(player.getLocation(), true);

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code> if an error was thrown or an event canceled the opening.
     */
    @Override
    public boolean openStoneCutter(Player player) {
        final StoneCutterOpenEvent event = new StoneCutterOpenEvent(player);
        if (event.isCanceled()) {
            return false;
        }
        player.openStonecutter(player.getLocation(), true);

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param player The {@link Player} instance that we wish to open the inventory for.
     * @return Returns <code>true</code> if inventory was opened successfully; <code>false</code> if an error was thrown or an event canceled the opening.
     */
    @Override
    public boolean openSmithing(Player player) {
        final SmithingOpenEvent event = new SmithingOpenEvent(player);
        // Check if the event was canceled.
        if (event.isCanceled()) {
            return false;
        }
        // Forcefully open a smithing table at the player's location.
        player.openSmithingTable(player.getLocation(), true);

        return true;
    }

    private static class FakeEnchant extends EnchantmentMenu {

        private final ContainerLevelAccess myAccess;
        private final RandomSource random;
        private final ServerPlayer player;
        public int maxLevel;

        /**
         * Creates a new FakeEnchant menu that uses a maximum level.
         *
         * @param i        Container ID.
         * @param player   The player that opened this inventory.
         * @param maxLevel The maximum enchantment level.
         */
        public FakeEnchant(final int i, final @NotNull Player player, int maxLevel) {
            super(i, ((CraftPlayer) player).getHandle().getInventory(),
                    ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(),
                            new BlockPos(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ())));
            this.checkReachable = false;

            this.myAccess = ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(),
                    new BlockPos(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()));

            this.maxLevel = maxLevel;
            this.random = new RandomSourceWrapper(new Random());

            this.player = ((CraftPlayer) player).getHandle();
            this.setTitle(Component.literal("Enchant"));
        }

        public FakeEnchant(final int i, final @NotNull Player player) {
            super(i, ((CraftPlayer) player).getHandle().getInventory(), ContainerLevelAccess.create(((CraftPlayer) player).getHandle().level(),
                    new BlockPos(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ())));
            this.checkReachable = false;
            this.random = null;
            this.myAccess = null;
            this.player = ((CraftPlayer) player).getHandle();
            this.setTitle(Component.literal("Enchant"));
        }

        @Override
        public void slotsChanged(Container inventory) {
            // This is different when compared to the underlying default.
            // We cannot check if the inventory is equal to our custom container, instead we just check max level.
            // If we do check the inventory, it will return false because, without reflection, it cannot equal our container.
            // So we just have to check the max level and then hope that all goes well in the method.
            if (maxLevel > 0) {
                final ItemStack itemstack = inventory.getItem(0);

                if (!itemstack.isEmpty()) {
                    this.myAccess.execute((world, blockPos) -> {
                        final IdMap<Holder<Enchantment>> registry = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).asHolderIdMap();
                        final int i = maxLevel;

                        // Normally, this would be where we check for bookshelves.
                        // However, we instead just use the maximum level.
                        // The max level is then later clamped in other methods if required.

                        // Gets the enchantment seed from the underlying enchantment menu.
                        this.random.setSeed(getEnchantmentSeed());

                        int j;
                        for (j = 0; j < 3; ++j) {
                            System.out.println("Enchantment roll");
                            this.costs[j] = EnchantmentHelper.getEnchantmentCost(this.random, j, i, itemstack);
                            this.enchantClue[j] = -1;
                            this.levelClue[j] = -1;
                            if (this.costs[j] < j + 1) {
                                this.costs[j] = 0;
                            }
                        }

                        for (j = 0; j < 3; ++j) {
                            if (this.costs[j] > 0) {
                                List<EnchantmentInstance> list = getEnchantmentList(world.registryAccess(), itemstack, j, this.costs[j]);

                                if (!list.isEmpty()) {
                                    EnchantmentInstance weightedRandomEnchantment = list.get(this.random.nextInt(list.size()));
                                    this.enchantClue[j] = registry.getId(weightedRandomEnchantment.enchantment());
                                    this.levelClue[j] = weightedRandomEnchantment.level();
                                }
                            }
                        }

                        final CraftItemStack item = CraftItemStack.asCraftMirror(itemstack);
                        final org.bukkit.enchantments.EnchantmentOffer[] offers = new EnchantmentOffer[3];
                        for (j = 0; j < 3; ++j) {
                            org.bukkit.enchantments.Enchantment enchantment = (this.enchantClue[j] >= 0) ? CraftEnchantment.minecraftHolderToBukkit(Objects.requireNonNull(registry.byId(this.enchantClue[j]))) : null;
                            offers[j] = (enchantment != null) ? new EnchantmentOffer(enchantment, this.levelClue[j], this.costs[j]) : null;
                        }

                        // Keeping this, ensuring that functionality remains the same with our custom enchantment table and the vanilla ones.
                        final PrepareItemEnchantEvent event = new PrepareItemEnchantEvent(this.player.getBukkitEntity(), this.getBukkitView(), this.myAccess.getLocation().getBlock(), item, offers, i);
                        event.setCancelled(!itemstack.isEnchantable());
                        world.getCraftServer().getPluginManager().callEvent(event);

                        // Event is canceled, go ahead and return nothing.
                        if (event.isCancelled()) {
                            for (j = 0; j < 3; ++j) {
                                this.costs[j] = 0;
                                this.enchantClue[j] = -1;
                                this.levelClue[j] = -1;
                            }
                            return;
                        }

                        // Houston, we have the go-ahead. List the enchantments.
                        for (j = 0; j < 3; j++) {
                            EnchantmentOffer offer = event.getOffers()[j];
                            if (offer != null) {
                                this.costs[j] = offer.getCost();
                                this.enchantClue[j] = registry.getId(CraftEnchantment.bukkitToMinecraftHolder(offer.getEnchantment()));
                                this.levelClue[j] = offer.getEnchantmentLevel();
                            } else {
                                this.costs[j] = 0;
                                this.enchantClue[j] = -1;
                                this.levelClue[j] = -1;
                            }
                        }

                        // Broadcasts the changes to the menu and to the server (I assume).
                        this.broadcastChanges();
                    });
                } else {
                    // ItemStack is empty, return nothing.
                    for (int i = 0; i < 3; ++i) {
                        this.costs[i] = 0;
                        this.enchantClue[i] = -1;
                        this.levelClue[i] = -1;
                    }
                }
                return;
            }

            // We didn't provide a max level, go ahead and pass it along to the super method.
            // This will then "assume" it's an actual enchantment table and WILL use surrounding bookshelves.
            super.slotsChanged(inventory);
        }

        // Duplicate of getEnchantmentList from EnchantMenu, put here, so we can use it.
        private @NotNull List<EnchantmentInstance> getEnchantmentList(@NotNull RegistryAccess registryManager, ItemStack itemstack, int i, int j) {
            this.random.setSeed(super.getEnchantmentSeed() + i);

            final Optional<HolderSet.Named<Enchantment>> optional = registryManager.lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.IN_ENCHANTING_TABLE);

            if (optional.isEmpty()) {
                return List.of();
            }

            final List<EnchantmentInstance> list = EnchantmentHelper.selectEnchantment(this.random, itemstack, j, ((HolderSet.Named) optional.get()).stream());
            if (itemstack.is(Items.BOOK) && list.size() > 1) {
                list.remove(this.random.nextInt(list.size()));
            }

            return list;
        }
    }

}