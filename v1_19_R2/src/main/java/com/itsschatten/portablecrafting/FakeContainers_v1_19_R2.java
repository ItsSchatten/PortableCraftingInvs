package com.itsschatten.portablecrafting;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.configutils.PlayerConfigManager;
import com.itsschatten.portablecrafting.events.*;
import com.shanebeestudios.api.BrewingManager;
import com.shanebeestudios.api.FurnaceManager;
import com.shanebeestudios.api.VirtualFurnaceAPI;
import com.shanebeestudios.api.machine.BrewingStand;
import com.shanebeestudios.api.machine.Furnace;
import com.shanebeestudios.api.property.FurnaceProperties;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R2.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_19_R2.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.v1_19_R2.util.RandomSourceWrapper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class FakeContainers_v1_19_R2 implements FakeContainers, Listener {
    private final FurnaceManager manager;
    private final BrewingManager brewingManager;
    boolean debug, mysql;

    MySqlI sql;

    public FakeContainers_v1_19_R2(JavaPlugin plugin, MySqlI sql) {
        VirtualFurnaceAPI furnaceAPI = new VirtualFurnaceAPI(plugin, true, true);
        this.manager = furnaceAPI.getFurnaceManager();
        this.brewingManager = furnaceAPI.getBrewingManager();
        this.sql = sql;

    }

    @Override
    public void setUsingMysql(boolean bool) {
        mysql = bool;
    }

    @Override
    public void setDebug(boolean bool) {
        debug = bool;
    }

    @Override
    public boolean openLoom(Player player) {
        try {
            ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            final FakeLoom fakeLoom = new FakeLoom(containerID, player);

            final LoomOpenEvent event = new LoomOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                ePlayer.containerMenu = ePlayer.inventoryMenu;
                ePlayer.connection.send(new ClientboundOpenScreenPacket(containerID, MenuType.LOOM, fakeLoom.getTitle()));
                ePlayer.containerMenu = fakeLoom;
                return true;
            }
            return false;
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(debug, ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");
            return false;
        }
    }

    @Override
    public boolean openAnvil(Player player) {
        try {
            CraftEventFactory.handleInventoryCloseEvent(((CraftPlayer) player).getHandle());
            ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            final FakeAnvil fakeAnvil = new FakeAnvil(containerID, player);

            final AnvilOpenEvent event = new AnvilOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                player.openInventory(fakeAnvil.getBukkitView()); // Using this seemingly makes the Anvil listener far more consistent.
                return true;
            }
            return false;
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(debug, ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");
            return false;
        }
    }

    @Override
    public boolean openCartography(Player player) {
        try {
            ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            FakeCartography fakeCartography = new FakeCartography(containerID, player);

            final CartographyOpenEvent event = new CartographyOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                ePlayer.containerMenu = ePlayer.inventoryMenu;
                ePlayer.connection.send(new ClientboundOpenScreenPacket(containerID, MenuType.CARTOGRAPHY_TABLE, fakeCartography.getTitle()));
                ePlayer.containerMenu = fakeCartography;
                return true;
            }
            return false;
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(debug, ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");
            return false;
        }
    }

    @Override
    public boolean openGrindStone(Player player) {
        try {
            ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerId = ePlayer.nextContainerCounter();
            FakeGrindstone fakeGrindstone = new FakeGrindstone(containerId, player);

            final GrindStoneOpenEvent event = new GrindStoneOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                ePlayer.containerMenu = ePlayer.inventoryMenu;
                ePlayer.connection.send(new ClientboundOpenScreenPacket(containerId, MenuType.GRINDSTONE, fakeGrindstone.getTitle()));
                ePlayer.containerMenu = fakeGrindstone;
                return true;
            }
            return false;
        } catch (UnsupportedOperationException ex) {
            Utils.debugLog(debug, ex.getMessage());
            Utils.log("An error occurred while running the grindstone command, make sure you have debug enabled to see this message.");
            player.sendMessage("An error occurred, please contact an administrator.");
            return false;
        }
    }

    @Override
    public boolean openStoneCutter(Player player) {
        try {
            ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            FakeStoneCutter fakeStoneCutter = new FakeStoneCutter(containerID, player);

            final StoneCutterOpenEvent event = new StoneCutterOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                ePlayer.containerMenu = ePlayer.inventoryMenu;
                ePlayer.connection.send(new ClientboundOpenScreenPacket(containerID, MenuType.STONECUTTER, fakeStoneCutter.getTitle()));
                ePlayer.containerMenu = fakeStoneCutter;

                return true;
            }
            return false;
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(debug, ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");
        }
        return true;
    }

    @Override
    public boolean openEnchant(Player player) {
        ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
        int containerID = ePlayer.nextContainerCounter();
        FakeEnchant fakeEnchant = new FakeEnchant(containerID, player);
        return callEnchant(player, ePlayer, containerID, fakeEnchant);
    }

    private boolean callEnchant(Player player, ServerPlayer ePlayer, int containerID, FakeEnchant fakeEnchant) {
        final EnchantingOpenEvent event = new EnchantingOpenEvent(player);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
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
        ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
        int containerID = ePlayer.nextContainerCounter();
        FakeEnchant fakeEnchant = new FakeEnchant(containerID, player, maxLvl);

        return callEnchant(player, ePlayer, containerID, fakeEnchant);
    }

    @Override
    public boolean openBrewingStand(Player player) {
        if (mysql) {
            BrewingStand stand;
            if (sql.getStand(player.getUniqueId(), brewingManager) == null) {
                stand = brewingManager.createBrewingStand("Brewing");

                sql.setStand(player.getUniqueId(), stand);
            } else {
                stand = sql.getStand(player.getUniqueId(), brewingManager);

                if (stand == null) {
                    stand = brewingManager.createBrewingStand("Brewing");

                    sql.setStand(player.getUniqueId(), stand);
                }

            }
            final BrewingOpenEvent event = new BrewingOpenEvent(player, stand);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                stand.openInventory(player);
                return true;
            }
            return false;
        }

        if (PlayerConfigManager.getConfig(player.getUniqueId()).exists()) {
            FileConfiguration playerConfig = PlayerConfigManager.getConfig(player.getUniqueId()).getConfig();
            BrewingStand stand;
            if (playerConfig.get("brewing") == null) {
                stand = brewingManager.createBrewingStand("Brewing");

                playerConfig.set("brewing", stand.getUniqueID().toString());
                PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
            } else {
                stand = brewingManager.getByID(UUID.fromString(playerConfig.getString("brewing")));

                if (stand == null) {
                    stand = brewingManager.createBrewingStand("Brewing");

                    playerConfig.set("brewing", stand.getUniqueID().toString());
                    PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
                }
            }
            final BrewingOpenEvent event = new BrewingOpenEvent(player, stand);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                stand.openInventory(player);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean openFurnace(Player player) {
        if (mysql) {
            Furnace furnace;
            if (sql.getFurnace(player.getUniqueId(), manager, MySqlI.FurnaceTypes.FURNACE) == null) {
                furnace = manager.createFurnace("Furnace", FurnaceProperties.FURNACE);

                sql.setFurnace(player.getUniqueId(), furnace, MySqlI.FurnaceTypes.FURNACE);
            } else {
                furnace = sql.getFurnace(player.getUniqueId(), manager, MySqlI.FurnaceTypes.FURNACE);

                if (furnace == null) {
                    furnace = manager.createFurnace("Furnace");

                    sql.setFurnace(player.getUniqueId(), furnace, MySqlI.FurnaceTypes.FURNACE);
                }

            }
            final FurnaceOpenEvent event = new FurnaceOpenEvent(player, furnace);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                furnace.openInventory(player);
                return true;
            }
            return false;
        }

        if (PlayerConfigManager.getConfig(player.getUniqueId()).exists()) {
            FileConfiguration playerConfig = PlayerConfigManager.getConfig(player.getUniqueId()).getConfig();
            Furnace furnace;
            if (playerConfig.get("furnaces.furnace") == null) {
                furnace = manager.createFurnace("Furnace");

                playerConfig.set("furnaces.furnace", furnace.getUniqueID().toString());
                PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
            } else {
                furnace = manager.getByID(UUID.fromString(playerConfig.getString("furnaces.furnace")));

                if (furnace == null) {
                    furnace = manager.createFurnace("Furnace");

                    playerConfig.set("furnaces.furnace", furnace.getUniqueID().toString());
                    PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
                }
            }
            final FurnaceOpenEvent event = new FurnaceOpenEvent(player, furnace);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                furnace.openInventory(player);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean openBlastFurnace(Player player) {
        if (mysql) {
            Furnace furnace;
            if (sql.getFurnace(player.getUniqueId(), manager, MySqlI.FurnaceTypes.BLAST_FURNACE) == null) {
                furnace = manager.createFurnace("Blast Furnace", FurnaceProperties.BLAST_FURNACE);
                furnace.openInventory(player);

                sql.setFurnace(player.getUniqueId(), furnace, MySqlI.FurnaceTypes.BLAST_FURNACE);
            } else {
                furnace = sql.getFurnace(player.getUniqueId(), manager, MySqlI.FurnaceTypes.BLAST_FURNACE);

                if (furnace == null) {
                    furnace = manager.createFurnace("Blast Furnace");

                    sql.setFurnace(player.getUniqueId(), furnace, MySqlI.FurnaceTypes.BLAST_FURNACE);
                }
            }
            final FurnaceOpenEvent event = new FurnaceOpenEvent(player, furnace);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                furnace.openInventory(player);
                return true;
            }
            return false;
        }

        if (PlayerConfigManager.getConfig(player.getUniqueId()).exists()) {
            FileConfiguration playerConfig = PlayerConfigManager.getConfig(player.getUniqueId()).getConfig();
            Furnace furnace;
            if (playerConfig.get("furnaces.blast-furnace") == null) {
                furnace = manager.createFurnace("Blast Furnace", FurnaceProperties.BLAST_FURNACE);

                playerConfig.set("furnaces.blast-furnace", furnace.getUniqueID().toString());
                PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
            } else {
                furnace = manager.getByID(UUID.fromString(playerConfig.getString("furnaces.blast-furnace")));

                if (furnace == null) {
                    furnace = manager.createFurnace("Blast Furnace");

                    playerConfig.set("furnaces.blast-furnace", furnace.getUniqueID().toString());
                    PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
                }
            }
            final FurnaceOpenEvent event = new FurnaceOpenEvent(player, furnace);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                furnace.openInventory(player);
                return true;
            }
        }
        return false;
    }

    // TODO: Implement later.
    @Override
    public boolean queryVirtualTileAPI() {
        return false;
    }

    @Override
    public boolean openSmoker(Player player) {
        if (mysql) {
            Furnace furnace;
            if (sql.getFurnace(player.getUniqueId(), manager, MySqlI.FurnaceTypes.SMOKER) == null) {
                furnace = manager.createFurnace("Smoker", FurnaceProperties.SMOKER);
                furnace.openInventory(player);

                sql.setFurnace(player.getUniqueId(), furnace, MySqlI.FurnaceTypes.SMOKER);
            } else {
                furnace = sql.getFurnace(player.getUniqueId(), manager, MySqlI.FurnaceTypes.SMOKER);

                if (furnace == null) {
                    furnace = manager.createFurnace("Smoker");

                    sql.setFurnace(player.getUniqueId(), furnace, MySqlI.FurnaceTypes.SMOKER);
                }
            }

            final FurnaceOpenEvent event = new FurnaceOpenEvent(player, furnace);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                furnace.openInventory(player);
                return true;
            }
            return false;
        }

        if (PlayerConfigManager.getConfig(player.getUniqueId()).exists()) {
            FileConfiguration playerConfig = PlayerConfigManager.getConfig(player.getUniqueId()).getConfig();
            Furnace furnace;
            if (playerConfig.get("furnaces.smoker") == null) {
                furnace = manager.createFurnace("Smoker", FurnaceProperties.SMOKER);

                playerConfig.set("furnaces.smoker", furnace.getUniqueID().toString());
                PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
            } else {
                furnace = manager.getByID(UUID.fromString(playerConfig.getString("furnaces.smoker")));

                if (furnace == null) {
                    furnace = manager.createFurnace("Smoker");

                    playerConfig.set("furnaces.smoker", furnace.getUniqueID().toString());
                    PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
                }
            }

            final FurnaceOpenEvent event = new FurnaceOpenEvent(player, furnace);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                furnace.openInventory(player);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean openSmithing(Player player) {
        try {
            ServerPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            FakeSmithing fakeSmithing = new FakeSmithing(containerID, player);

            final SmithingOpenEvent event = new SmithingOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                ePlayer.containerMenu = ePlayer.inventoryMenu;
                ePlayer.connection.send(new ClientboundOpenScreenPacket(containerID, MenuType.SMITHING, fakeSmithing.getTitle()));
                ePlayer.containerMenu = fakeSmithing;
                return true;
            }
            return false;
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(debug, ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");
            return false;
        }
    }

    @Override
    public void removeFromEnchantList(Player player) {
        FakeEnchant.getOpenEnchantTables().remove(player.getUniqueId());
    }

    private static class FakeGrindstone extends GrindstoneMenu {

        public FakeGrindstone(final int containerId, final Player player) {
            super(containerId, ((CraftPlayer) player).getHandle().getInventory(), ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(), new BlockPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;
            this.setTitle(Component.literal("Repair & Disenchant"));
        }
    }

    private static class FakeCartography extends CartographyTableMenu {

        public FakeCartography(final int containerId, final Player player) {
            super(containerId, ((CraftPlayer) player).getHandle().getInventory(), ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(), new BlockPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;
            this.setTitle(Component.literal("Cartography Table"));
        }
    }

    private static class FakeLoom extends LoomMenu {

        public FakeLoom(final int containerId, final Player player) {
            super(containerId, ((CraftPlayer) player).getHandle().getInventory(),
                    ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(), new BlockPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;
            this.setTitle(Component.literal("Loom"));
        }
    }

    private static class FakeStoneCutter extends StonecutterMenu {

        public FakeStoneCutter(final int containerId, final Player player) {
            super(containerId, ((CraftPlayer) player).getHandle().getInventory(), ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(), new BlockPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;
            this.setTitle(Component.literal("Stonecutter"));
        }
    }

    private static class FakeAnvil extends AnvilMenu {

        public FakeAnvil(final int containerID, final Player player) {
            super(containerID, ((CraftPlayer) player).getHandle().getInventory(),
                    ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(),
                            new BlockPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false; // ignore if the block is reachable, otherwise open regardless of distance.
            this.setTitle(Component.literal("Repair & Name"));
        }
    }

    private static class FakeSmithing extends SmithingMenu {

        public FakeSmithing(final int containerID, final Player player) {
            super(containerID, ((CraftPlayer) player).getHandle().getInventory(),
                    ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(),
                            new BlockPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false; // ignore if the block is reachable, otherwise open regardless of distance.
            this.setTitle(Component.literal("Upgrade Gear"));
        }
    }

    private static class FakeEnchant extends EnchantmentMenu {
        @Getter
        private static final Map<UUID, FakeEnchant> openEnchantTables = new HashMap<>();
        private final ContainerLevelAccess myAccess;
        private final RandomSource random;
        private final DataSlot enchantSeed;
        private final ServerPlayer player;
        public int maxLevel;

        public FakeEnchant(final int i, final Player player, int maxLevel) {
            super(i, ((CraftPlayer) player).getHandle().getInventory(),
                    ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(),
                            new BlockPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;
            this.myAccess = ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(),
                    new BlockPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
            this.maxLevel = maxLevel;
            this.random = new RandomSourceWrapper(new Random());
            this.enchantSeed = DataSlot.standalone();
            enchantSeed.set(((CraftPlayer) player).getHandle().getEnchantmentSeed());
            openEnchantTables.put(player.getUniqueId(), this);
            this.player = ((CraftPlayer) player).getHandle();
            this.setTitle(Component.literal("Enchant"));
        }

        public FakeEnchant(final int i, final Player player) {
            super(i, ((CraftPlayer) player).getHandle().getInventory(), ContainerLevelAccess.create(((CraftPlayer) player).getHandle().level,
                    new BlockPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;
            this.random = null;
            this.myAccess = null;
            this.enchantSeed = null;
            this.player = ((CraftPlayer) player).getHandle();
            this.setTitle(Component.literal("Enchant"));
        }

        @Override
        public void slotsChanged(Container iinventory) {
            if (openEnchantTables.containsKey(player.getBukkitEntity().getUniqueId())) {
                ItemStack itemstack = iinventory.getItem(0);
                if (iinventory.isEmpty()) {
                    for (int i = 0; i < 3; ++i) {
                        this.costs[i] = 0;
                        this.enchantClue[i] = -1;
                        this.levelClue[i] = -1;
                    }
                    enchantSeed.set(player.getEnchantmentSeed());
                    return;
                }
                myAccess.execute((world, blockPos) -> {
                    int i = maxLevel;
                    int j;
                    this.random.setSeed(this.enchantSeed.get());

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
                            List<EnchantmentInstance> list = this.getEnchantmentList(itemstack, j, this.costs[j]);
                            if (list != null && !list.isEmpty()) {
                                EnchantmentInstance weightedRandomEnchant = list.get(this.random.nextInt(list.size()));
                                this.enchantClue[j] = BuiltInRegistries.ENCHANTMENT.getId(weightedRandomEnchant.enchantment);
                                this.levelClue[j] = weightedRandomEnchant.level;
                            }
                        }
                    }

                    CraftItemStack item = CraftItemStack.asCraftMirror(itemstack);
                    EnchantmentOffer[] offers = new EnchantmentOffer[3];

                    for (j = 0; j < 3; ++j) {
                        Enchantment enchantment = this.enchantClue[j] >= 0 ?
                                Enchantment.getByKey(CraftNamespacedKey.fromMinecraft(Objects.requireNonNull(BuiltInRegistries.ENCHANTMENT.getKey(BuiltInRegistries.ENCHANTMENT.byId(this.enchantClue[j]))))) : null;
                        offers[j] = enchantment != null ? new EnchantmentOffer(enchantment, this.levelClue[j], this.costs[j]) : null;
                    }

                    PrepareItemEnchantEvent event = new PrepareItemEnchantEvent(this.player.getBukkitEntity(), this.getBukkitView(), this.myAccess.getLocation().getBlock(), item, offers, i);
                    event.setCancelled(!itemstack.isEnchantable());
                    world.getCraftServer().getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        for (j = 0; j < 3; ++j) {
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

                        this.broadcastChanges();
                    } else {
                        for (j = 0; j < 3; ++j) {
                            this.costs[j] = 0;
                            this.enchantClue[j] = -1;
                            this.levelClue[j] = -1;
                        }

                    }

                });
                return;
            }
            super.slotsChanged(iinventory);
        }

        private List<EnchantmentInstance> getEnchantmentList(ItemStack itemstack, int i, int j) {
            this.random.setSeed(this.enchantSeed.get() + i);
            List<EnchantmentInstance> list = EnchantmentHelper.selectEnchantment(this.random, itemstack, j, false);
            if (itemstack.is(Items.BOOK) && list.size() > 1) {
                list.remove(this.random.nextInt(list.size()));
            }

            return list;
        }
    }
}
