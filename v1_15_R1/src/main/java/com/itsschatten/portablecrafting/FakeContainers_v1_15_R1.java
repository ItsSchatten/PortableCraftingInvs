package com.itsschatten.portablecrafting;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.configutils.PlayerConfigManager;
import com.shanebeestudios.vf.api.BrewingManager;
import com.shanebeestudios.vf.api.FurnaceManager;
import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.machine.BrewingStand;
import com.shanebeestudios.vf.api.machine.Furnace;
import com.shanebeestudios.vf.api.property.FurnaceProperties;
import net.minecraft.server.v1_15_R1.*;
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
    public void openLoom(Player player) {
        try {
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            FakeLoom fakeLoom = new FakeLoom(containerID, player);

            ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.LOOM, fakeLoom.getTitle()));

            ePlayer.activeContainer = fakeLoom;
            ePlayer.activeContainer.addSlotListener(ePlayer);
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(debug, ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");

        }
    }

    @Override
    public void openAnvil(Player player) {
        try {
            CraftEventFactory.handleInventoryCloseEvent(((CraftPlayer) player).getHandle());
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            final FakeAnvil fakeAnvil = new FakeAnvil(containerID, player);

            ePlayer.activeContainer = ePlayer.defaultContainer;

            ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.ANVIL, new ChatMessage("Repair & Name")));
            ePlayer.activeContainer = fakeAnvil;
            ePlayer.activeContainer.addSlotListener(ePlayer);
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(debug, ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");

        }
    }

    @Override
    public void openCartography(Player player) {
        try {
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            FakeCartography fakeCartography = new FakeCartography(containerID, player);

            ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.CARTOGRAPHY_TABLE, fakeCartography.getTitle()));

            ePlayer.activeContainer = fakeCartography;
            ePlayer.activeContainer.addSlotListener(ePlayer);
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(debug, ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");

        }
    }

    @Override
    public void openGrindStone(Player player) {
        try {
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerId = ePlayer.nextContainerCounter();
            FakeGrindstone fakeGrindstone = new FakeGrindstone(containerId, player);

            ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId, Containers.GRINDSTONE, fakeGrindstone.getTitle()));

            ePlayer.activeContainer = fakeGrindstone;
            ePlayer.activeContainer.addSlotListener(ePlayer);

        } catch (UnsupportedOperationException ex) {
            Utils.debugLog(debug, ex.getMessage());
            Utils.log("An error occurred while running the grindstone command, make sure you have debug enabled to see this message.");
            player.sendMessage("An error occurred, please contact an administrator.");
        }
    }

    @Override
    public void openStoneCutter(Player player) {
        try {
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            FakeStoneCutter fakeStoneCutter = new FakeStoneCutter(containerID, player);

            ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.STONECUTTER, fakeStoneCutter.getTitle()));

            ePlayer.activeContainer = fakeStoneCutter;
            ePlayer.activeContainer.addSlotListener(ePlayer);
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(debug, ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");

        }
    }

    @Override
    public void openSmithing(Player player) {
        Utils.tell(player, "Smithing tables do not work in 1.15 versions; in fact you really shouldn't be seeing this command.");
    }

    @Override
    public void openEnchant(Player player) {
        EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
        int containerID = ePlayer.nextContainerCounter();
        FakeEnchant fakeEnchant = new FakeEnchant(containerID, player);

        ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.ENCHANTMENT, fakeEnchant.getTitle()));

        ePlayer.activeContainer = fakeEnchant;
        ePlayer.activeContainer.addSlotListener(ePlayer);
    }

    @Override
    public void openEnchant(Player player, int maxLvl) {
        EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
        int containerID = ePlayer.nextContainerCounter();
        FakeEnchant fakeEnchant = new FakeEnchant(containerID, player, maxLvl);

        ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.ENCHANTMENT, fakeEnchant.getTitle()));

        ePlayer.activeContainer = fakeEnchant;
        ePlayer.activeContainer.addSlotListener(ePlayer);
    }
    @Override
    public void openBrewingStand(Player player) {
        if (mysql) {
            if (sql.getStand(player.getUniqueId(), brewingManager) == null) {
                BrewingStand stand = brewingManager.createBrewingStand("Brewing");
                stand.openInventory(player);

                sql.setStand(player.getUniqueId(), stand);
            } else {
                BrewingStand stand = sql.getStand(player.getUniqueId(), brewingManager);

                if (stand == null) {
                    stand = brewingManager.createBrewingStand("Brewing");
                    stand.openInventory(player);

                    sql.setStand(player.getUniqueId(), stand);
                    return;
                }

                stand.openInventory(player);
            }
            return;
        }

        if (PlayerConfigManager.getConfig(player.getUniqueId()).exists()) {
            FileConfiguration playerConfig = PlayerConfigManager.getConfig(player.getUniqueId()).getConfig();

            if (playerConfig.get("brewing") == null) {
                BrewingStand stand = brewingManager.createBrewingStand("Brewing");
                stand.openInventory(player);

                playerConfig.set("brewing", stand.getUniqueID().toString());
                PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
            } else {
                BrewingStand stand = brewingManager.getByID(UUID.fromString(playerConfig.getString("brewing")));

                if (stand == null) {
                    stand = brewingManager.createBrewingStand("Brewing");
                    stand.openInventory(player);

                    playerConfig.set("brewing", stand.getUniqueID().toString());
                    PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
                    return;
                }

                stand.openInventory(player);
            }
        }
    }

    @Override
    public void openFurnace(Player player) {
        if (mysql) {
            if (sql.getFurnace(player.getUniqueId(), manager, MySqlI.FurnaceTypes.FURNACE) == null) {
                Furnace furnace = manager.createFurnace("Furnace", FurnaceProperties.FURNACE);
                furnace.openInventory(player);

                sql.setFurnace(player.getUniqueId(), furnace, MySqlI.FurnaceTypes.FURNACE);
            } else {
                Furnace furnace = sql.getFurnace(player.getUniqueId(), manager, MySqlI.FurnaceTypes.FURNACE);

                if (furnace == null) {
                    furnace = manager.createFurnace("Furnace");
                    furnace.openInventory(player);

                    sql.setFurnace(player.getUniqueId(), furnace, MySqlI.FurnaceTypes.FURNACE);
                    return;
                }

                furnace.openInventory(player);
            }
            return;
        }

        if (PlayerConfigManager.getConfig(player.getUniqueId()).exists()) {
            FileConfiguration playerConfig = PlayerConfigManager.getConfig(player.getUniqueId()).getConfig();

            if (playerConfig.get("furnaces.furnace") == null) {
                Furnace furnace = manager.createFurnace("Furnace");
                furnace.openInventory(player);

                playerConfig.set("furnaces.furnace", furnace.getUniqueID().toString());
                PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
            } else {
                Furnace furnace = manager.getByID(UUID.fromString(playerConfig.getString("furnaces.furnace")));

                if (furnace == null) {
                    furnace = manager.createFurnace("Furnace");
                    furnace.openInventory(player);

                    playerConfig.set("furnaces.furnace", furnace.getUniqueID().toString());
                    PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
                    return;
                }

                furnace.openInventory(player);
            }
        }
    }

    @Override
    public void openBlastFurnace(Player player) {
        if (mysql) {
            if (sql.getFurnace(player.getUniqueId(), manager, MySqlI.FurnaceTypes.BLAST_FURNACE) == null) {
                Furnace furnace = manager.createFurnace("Blast Furnace", FurnaceProperties.BLAST_FURNACE);
                furnace.openInventory(player);

                sql.setFurnace(player.getUniqueId(), furnace, MySqlI.FurnaceTypes.BLAST_FURNACE);
            } else {
                Furnace furnace = sql.getFurnace(player.getUniqueId(), manager, MySqlI.FurnaceTypes.BLAST_FURNACE);

                if (furnace == null) {
                    furnace = manager.createFurnace("Blast Furnace");
                    furnace.openInventory(player);

                    sql.setFurnace(player.getUniqueId(), furnace, MySqlI.FurnaceTypes.BLAST_FURNACE);
                    return;
                }

                furnace.openInventory(player);
            }
            return;
        }

        if (PlayerConfigManager.getConfig(player.getUniqueId()).exists()) {
            FileConfiguration playerConfig = PlayerConfigManager.getConfig(player.getUniqueId()).getConfig();

            if (playerConfig.get("furnaces.blast-furnace") == null) {
                Furnace blastFurnace = manager.createFurnace("Blast Furnace", FurnaceProperties.BLAST_FURNACE);
                blastFurnace.openInventory(player);

                playerConfig.set("furnaces.blast-furnace", blastFurnace.getUniqueID().toString());
                PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
            } else {
                Furnace blastFurnace = manager.getByID(UUID.fromString(playerConfig.getString("furnaces.blast-furnace")));

                if (blastFurnace == null) {
                    blastFurnace = manager.createFurnace("Blast Furnace");
                    blastFurnace.openInventory(player);

                    playerConfig.set("furnaces.blast-furnace", blastFurnace.getUniqueID().toString());
                    PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
                    return;
                }

                blastFurnace.openInventory(player);
            }
        }
    }

    @Override
    public void openSmoker(Player player) {
        if (mysql) {
            if (sql.getFurnace(player.getUniqueId(), manager, MySqlI.FurnaceTypes.SMOKER) == null) {
                Furnace furnace = manager.createFurnace("Smoker", FurnaceProperties.SMOKER);
                furnace.openInventory(player);

                sql.setFurnace(player.getUniqueId(), furnace, MySqlI.FurnaceTypes.SMOKER);
            } else {
                Furnace furnace = sql.getFurnace(player.getUniqueId(), manager, MySqlI.FurnaceTypes.SMOKER);

                if (furnace == null) {
                    furnace = manager.createFurnace("Smoker");
                    furnace.openInventory(player);

                    sql.setFurnace(player.getUniqueId(), furnace, MySqlI.FurnaceTypes.SMOKER);
                    return;
                }

                furnace.openInventory(player);
            }
            return;
        }

        if (PlayerConfigManager.getConfig(player.getUniqueId()).exists()) {
            FileConfiguration playerConfig = PlayerConfigManager.getConfig(player.getUniqueId()).getConfig();

            if (playerConfig.get("furnaces.smoker") == null) {
                Furnace smoker = manager.createFurnace("Smoker", FurnaceProperties.SMOKER);
                smoker.openInventory(player);

                playerConfig.set("furnaces.smoker", smoker.getUniqueID().toString());
                PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
            } else {
                Furnace furnace = manager.getByID(UUID.fromString(playerConfig.getString("furnaces.smoker")));

                if (furnace == null) {
                    furnace = manager.createFurnace("Smoker");
                    furnace.openInventory(player);

                    playerConfig.set("furnaces.smoker", furnace.getUniqueID().toString());
                    PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
                    return;
                }
                furnace.openInventory(player);
            }

        }
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
