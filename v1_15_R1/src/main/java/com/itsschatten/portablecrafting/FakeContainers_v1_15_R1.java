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
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class FakeContainers_v1_15_R1 implements FakeContainers {
    private final FurnaceManager manager;
    private final BrewingManager brewingManager;
    boolean debug, mysql;
    MySqlI sql;

    public FakeContainers_v1_15_R1(JavaPlugin plugin, MySqlI sql) {
        VirtualFurnaceAPI furnaceAPI = new VirtualFurnaceAPI(plugin, true);
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
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            final FakeLoom fakeLoom = new FakeLoom(containerID, player);

            final LoomOpenEvent event = new LoomOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                ePlayer.activeContainer = ePlayer.defaultContainer;
                ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.LOOM, fakeLoom.getTitle()));
                ePlayer.activeContainer = fakeLoom;
                ePlayer.activeContainer.addSlotListener(ePlayer);
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
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            final FakeAnvil fakeAnvil = new FakeAnvil(containerID, player);

            final AnvilOpenEvent event = new AnvilOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                ePlayer.activeContainer = ePlayer.defaultContainer;
                ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.ANVIL, new ChatMessage("Repair & Name")));
                ePlayer.activeContainer = fakeAnvil;
                ePlayer.activeContainer.addSlotListener(ePlayer);
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
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            FakeCartography fakeCartography = new FakeCartography(containerID, player);

            final CartographyOpenEvent event = new CartographyOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                ePlayer.activeContainer = ePlayer.defaultContainer;
                ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.CARTOGRAPHY_TABLE, fakeCartography.getTitle()));
                ePlayer.activeContainer = fakeCartography;
                ePlayer.activeContainer.addSlotListener(ePlayer);
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
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerId = ePlayer.nextContainerCounter();
            FakeGrindstone fakeGrindstone = new FakeGrindstone(containerId, player);

            final GrindStoneOpenEvent event = new GrindStoneOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                ePlayer.activeContainer = ePlayer.defaultContainer;
                ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId, Containers.GRINDSTONE, fakeGrindstone.getTitle()));
                ePlayer.activeContainer = fakeGrindstone;
                ePlayer.activeContainer.addSlotListener(ePlayer);
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
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            FakeStoneCutter fakeStoneCutter = new FakeStoneCutter(containerID, player);

            final StoneCutterOpenEvent event = new StoneCutterOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                ePlayer.activeContainer = ePlayer.defaultContainer;
                ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.STONECUTTER, fakeStoneCutter.getTitle()));
                ePlayer.activeContainer = fakeStoneCutter;
                ePlayer.activeContainer.addSlotListener(ePlayer);

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
        EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
        int containerID = ePlayer.nextContainerCounter();
        FakeEnchant fakeEnchant = new FakeEnchant(containerID, player);
        return callEnchant(player, ePlayer, containerID, fakeEnchant);
    }

    private boolean callEnchant(Player player, EntityPlayer ePlayer, int containerID, FakeEnchant fakeEnchant) {
        final EnchantingOpenEvent event = new EnchantingOpenEvent(player);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            ePlayer.activeContainer = ePlayer.defaultContainer;
            ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.ENCHANTMENT, fakeEnchant.getTitle()));
            ePlayer.activeContainer = fakeEnchant;
            ePlayer.activeContainer.addSlotListener(ePlayer);
            return true;
        }
        return false;
    }

    @Override
    public boolean openEnchant(Player player, int maxLvl) {
        EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
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
    public void removeFromEnchantList(Player player) {
    }


    @Override
    public boolean openSmithing(Player player) {
        Utils.tell(player, "Smithing tables do not work in 1.15 versions; in fact you really shouldn't be seeing this command.");
        return false;
    }

    private static class FakeGrindstone extends ContainerGrindstone {

        public FakeGrindstone(final int containerId, final Player player) {
            super(containerId, ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;
            this.setTitle(new ChatMessage("Repair & Disenchant"));
        }
    }

    private static class FakeCartography extends ContainerCartography {

        public FakeCartography(final int containerId, final Player player) {
            super(containerId, ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;
            this.setTitle(new ChatMessage("Cartography Table"));
        }
    }

    private static class FakeLoom extends ContainerLoom {

        public FakeLoom(final int containerId, final Player player) {
            super(containerId, ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;
            this.setTitle(new ChatMessage("Loom"));
        }
    }

    private static class FakeStoneCutter extends ContainerStonecutter {

        public FakeStoneCutter(final int containerId, final Player player) {
            super(containerId, ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;
            this.setTitle(new ChatMessage("Stonecutter"));
        }
    }

    private static class FakeAnvil extends ContainerAnvil {

        public FakeAnvil(final int containerID, final Player player) {
            super(containerID, ((CraftPlayer) player).getHandle().inventory,
                    ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(),
                            new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false; // ignore if the block is reachable, otherwise open regardless of distance.
            this.setTitle(new ChatMessage("Repair & Name"));
        }

        @Override
        public void b(EntityHuman entityhuman) {
        }

        @Override
        public void e() {
            super.e();
        }

        @Override
        protected void a(EntityHuman entityhuman, World world, IInventory iinventory) {
        }
    }

    private static class FakeEnchant extends ContainerEnchantTable {
        public int maxLevel;

        public FakeEnchant(final int i, final Player player, int maxLevel) {
            super(i, ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;
            this.maxLevel = maxLevel;

            this.setTitle(new ChatMessage("Enchant"));
        }

        public FakeEnchant(final int i, final Player player) {
            super(i, ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;

            this.setTitle(new ChatMessage("Enchant"));
        }

    }

}
