package com.itsschatten.portablecrafting;

import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.events.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.v1_20_R4.util.RandomSourceWrapper;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class FakeContainers_v1_20_R4 extends BaseFakeContainers {

    @Override
    public boolean openLoom(Player player) {
        try {
            final ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
            final int containerID = ePlayer.nextContainerCounter();
            final FakeLoom fakeLoom = new FakeLoom(containerID, player);

            final LoomOpenEvent event = new LoomOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCanceled()) {
                ePlayer.containerMenu = ePlayer.inventoryMenu;
                ePlayer.connection.send(new ClientboundOpenScreenPacket(containerID, MenuType.LOOM, fakeLoom.getTitle()));
                ePlayer.containerMenu = fakeLoom;
                return true;
            }
            return false;
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");
            return false;
        }
    }

    @Override
    public boolean openAnvil(Player player) {
        try {
            CraftEventFactory.handleInventoryCloseEvent(((CraftPlayer) player).getHandle());
            final ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
            final int containerID = ePlayer.nextContainerCounter();
            final FakeAnvil fakeAnvil = new FakeAnvil(containerID, player);

            final AnvilOpenEvent event = new AnvilOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCanceled()) {
                player.openInventory(fakeAnvil.getBukkitView()); // Using this seemingly makes the Anvil listener far more consistent.
                return true;
            }
            return false;
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");
            return false;
        }
    }

    @Override
    public boolean openCartography(Player player) {
        try {
            final ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
            final int containerID = ePlayer.nextContainerCounter();
            final FakeCartography fakeCartography = new FakeCartography(containerID, player);

            final CartographyOpenEvent event = new CartographyOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCanceled()) {
                player.openInventory(fakeCartography.getBukkitView());
                return true;
            }
            return false;
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");
            return false;
        }
    }

    @Override
    public boolean openGrindStone(Player player) {
        try {
            final ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
            final int containerId = ePlayer.nextContainerCounter();
            final FakeGrindstone fakeGrindstone = new FakeGrindstone(containerId, player);

            final GrindStoneOpenEvent event = new GrindStoneOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCanceled()) {
                ePlayer.containerMenu = ePlayer.inventoryMenu;
                ePlayer.connection.send(new ClientboundOpenScreenPacket(containerId, MenuType.GRINDSTONE, fakeGrindstone.getTitle()));
                ePlayer.containerMenu = fakeGrindstone;
                return true;
            }
            return false;
        } catch (UnsupportedOperationException ex) {
            Utils.debugLog(ex.getMessage());
            Utils.log("An error occurred while running the grindstone command, make sure you have debug enabled to see this message.");
            player.sendMessage("An error occurred, please contact an administrator.");
            return false;
        }
    }

    @Override
    public boolean openStoneCutter(Player player) {
        try {
            final ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
            final int containerID = ePlayer.nextContainerCounter();
            final FakeStoneCutter fakeStoneCutter = new FakeStoneCutter(containerID, player);

            final StoneCutterOpenEvent event = new StoneCutterOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCanceled()) {
                ePlayer.containerMenu = ePlayer.inventoryMenu;
                ePlayer.connection.send(new ClientboundOpenScreenPacket(containerID, MenuType.STONECUTTER, fakeStoneCutter.getTitle()));
                ePlayer.containerMenu = fakeStoneCutter;

                return true;
            }
            return false;
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");
        }
        return true;
    }

    @Override
    public boolean openEnchant(Player player) {
        final ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
        final int containerID = ePlayer.nextContainerCounter();
        final FakeEnchant fakeEnchant = new FakeEnchant(containerID, player);
        return callEnchant(player, ePlayer, containerID, fakeEnchant);
    }

    private boolean callEnchant(Player player, ServerPlayer ePlayer, int containerID, FakeEnchant fakeEnchant) {
        final EnchantingOpenEvent event = new EnchantingOpenEvent(player);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCanceled()) {
            ePlayer.containerMenu = ePlayer.inventoryMenu;
            ePlayer.connection.send(new ClientboundOpenScreenPacket(containerID, MenuType.ENCHANTMENT, fakeEnchant.getTitle()));
            ePlayer.containerMenu = fakeEnchant;
            ePlayer.initMenu(fakeEnchant);
            return true;
        }
        return false;
    }

    @Override
    public boolean openEnchant(Player player, int maxLvl) {
        final ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
        final int containerID = ePlayer.nextContainerCounter();
        final FakeEnchant fakeEnchant = new FakeEnchant(containerID, player, maxLvl);

        return callEnchant(player, ePlayer, containerID, fakeEnchant);
    }

    @Override
    public boolean openSmithing(Player player) {
        try {
            final ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
            final int containerID = ePlayer.nextContainerCounter();
            final FakeSmithing fakeSmithing = new FakeSmithing(containerID, player);

            final SmithingOpenEvent event = new SmithingOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCanceled()) {
                ePlayer.containerMenu = ePlayer.inventoryMenu;
                ePlayer.connection.send(new ClientboundOpenScreenPacket(containerID, MenuType.SMITHING, fakeSmithing.getTitle()));
                ePlayer.containerMenu = fakeSmithing;
                return true;
            }
            return false;
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");
            return false;
        }
    }

    private static class FakeGrindstone extends GrindstoneMenu {
        public FakeGrindstone(final int containerId, final @NotNull Player player) {
            super(containerId, ((CraftPlayer) player).getHandle().getInventory(), ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(), new BlockPos(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ())));
            this.checkReachable = false;
            this.setTitle(Component.literal("Repair & Disenchant"));
        }
    }

    private static class FakeCartography extends CartographyTableMenu {
        public FakeCartography(final int containerId, final @NotNull Player player) {
            super(containerId, ((CraftPlayer) player).getHandle().getInventory(), ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(), new BlockPos(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ())));
            this.checkReachable = false;
            this.setTitle(Component.literal("Cartography Table"));
        }
    }

    private static class FakeLoom extends LoomMenu {
        public FakeLoom(final int containerId, final @NotNull Player player) {
            super(containerId, ((CraftPlayer) player).getHandle().getInventory(),
                    ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(), new BlockPos(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ())));
            this.checkReachable = false;
            this.setTitle(Component.literal("Loom"));
        }
    }

    private static class FakeStoneCutter extends StonecutterMenu {
        public FakeStoneCutter(final int containerId, final @NotNull Player player) {
            super(containerId, ((CraftPlayer) player).getHandle().getInventory(), ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(), new BlockPos(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ())));
            this.checkReachable = false;
            this.setTitle(Component.literal("Stonecutter"));
        }
    }

    private static class FakeAnvil extends AnvilMenu {
        public FakeAnvil(final int containerID, final @NotNull Player player) {
            super(containerID, ((CraftPlayer) player).getHandle().getInventory(),
                    ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(),
                            new BlockPos(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ())));
            this.checkReachable = false; // ignore if the block is reachable, otherwise open regardless of distance.
            this.setTitle(Component.literal("Repair & Name"));
        }
    }

    private static class FakeSmithing extends SmithingMenu {
        public FakeSmithing(final int containerID, final @NotNull Player player) {
            super(containerID, ((CraftPlayer) player).getHandle().getInventory(),
                    ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(),
                            new BlockPos(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ())));
            this.checkReachable = false; // ignore if the block is reachable, otherwise open regardless of distance.
            this.setTitle(Component.literal("Upgrade Gear"));
        }
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
                        int i = maxLevel;

                        // Normally, this would be where we check for bookshelves.
                        // However, we instead just use the maximum level.
                        // The max level is then later clamped in other methods if required.

                        // Gets the enchantment seed from the underlying enchantment menu.
                        this.random.setSeed(getEnchantmentSeed());

                        int j;
                        for (j = 0; j < 3; ++j) {
                            this.costs[j] = EnchantmentHelper.getEnchantmentCost(this.random, j, i, itemstack);
                            this.enchantClue[j] = -1;
                            this.levelClue[j] = -1;
                            if (this.costs[j] < j + 1) {
                                this.costs[j] = 0;
                            }
                        }

                        for (j = 0; j < 3; ++j) {
                            if (this.costs[j] > 0) {
                                List<EnchantmentInstance> list = getEnchantmentList(world.enabledFeatures(), itemstack, j, this.costs[j]);

                                if (list != null && !list.isEmpty()) {
                                    EnchantmentInstance weightedRandomEnchantment = list.get(this.random.nextInt(list.size()));

                                    this.enchantClue[j] = BuiltInRegistries.ENCHANTMENT.getId(weightedRandomEnchantment.enchantment);
                                    this.levelClue[j] = weightedRandomEnchantment.level;
                                }
                            }
                        }

                        final CraftItemStack item = CraftItemStack.asCraftMirror(itemstack);
                        final org.bukkit.enchantments.EnchantmentOffer[] offers = new EnchantmentOffer[3];
                        for (j = 0; j < 3; ++j) {
                            org.bukkit.enchantments.Enchantment enchantment = (this.enchantClue[j] >= 0) ? org.bukkit.enchantments.Enchantment.getByKey(CraftNamespacedKey.fromMinecraft(Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.getKey(BuiltInRegistries.ENCHANTMENT.byId(this.enchantClue[j]))))) : null;
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
                                this.enchantClue[j] = BuiltInRegistries.ENCHANTMENT.getId(BuiltInRegistries.ENCHANTMENT.get(CraftNamespacedKey.toMinecraft(offer.getEnchantment().getKey())));
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
            super.slotsChanged(inventory);
        }

        // Duplicate of getEnchantmentList from EnchantMenu, put here, so we can use it.
        private List<EnchantmentInstance> getEnchantmentList(FeatureFlagSet featureflagset, ItemStack itemstack, int i, int j) {
            this.random.setSeed(super.getEnchantmentSeed() + i);

            final List<EnchantmentInstance> list = EnchantmentHelper.selectEnchantment(featureflagset, this.random, itemstack, j, false);
            if (itemstack.is(Items.BOOK) && list.size() > 1) {
                list.remove(this.random.nextInt(list.size()));
            }

            return list;
        }
    }
}
