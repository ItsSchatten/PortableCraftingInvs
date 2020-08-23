package com.itsschatten.portablecrafting;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.configutils.PlayerConfigManager;
import com.shanebeestudios.vf.api.FurnaceManager;
import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.machine.Furnace;
import com.shanebeestudios.vf.api.property.FurnaceProperties;
import lombok.Getter;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FakeContainers_v1_16_R1 implements FakeContainers, Listener {
    private final FurnaceManager manager;
    boolean debug;

    public FakeContainers_v1_16_R1(JavaPlugin plugin) {
        VirtualFurnaceAPI furnaceAPI = new VirtualFurnaceAPI(plugin, true);
        manager = furnaceAPI.getFurnaceManager();
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
            ePlayer.activeContainer = fakeLoom;
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
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            FakeAnvil fakeAnvil = new FakeAnvil(containerID, player);

            ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.ANVIL, fakeAnvil.getTitle()));

            ePlayer.activeContainer = fakeAnvil;
            ePlayer.activeContainer.addSlotListener(ePlayer);
            ePlayer.activeContainer = fakeAnvil;
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
            ePlayer.activeContainer = fakeCartography;
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
            ePlayer.activeContainer = fakeGrindstone;

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
            ePlayer.activeContainer = fakeStoneCutter;
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(debug, ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");

        }
    }

    @Override
    public void openEnchant(Player player) {
        EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
        int containerID = ePlayer.nextContainerCounter();
        FakeEnchant fakeEnchant = new FakeEnchant(containerID, player);

        ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.ENCHANTMENT, fakeEnchant.getTitle()));

        ePlayer.activeContainer = fakeEnchant;
        ePlayer.activeContainer.addSlotListener(ePlayer);
        ePlayer.activeContainer = fakeEnchant;
    }

    @Override
    public void openEnchant(Player player, int maxLvl) {
        EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
        int containerID = ePlayer.nextContainerCounter();
        FakeEnchant fakeEnchant = new FakeEnchant(containerID, player, maxLvl);

        ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.ENCHANTMENT, fakeEnchant.getTitle()));

        ePlayer.activeContainer = fakeEnchant;
        ePlayer.activeContainer.addSlotListener(ePlayer);
        ePlayer.activeContainer = fakeEnchant;
    }

    @Override
    public void openFurnace(Player player) {
        if (PlayerConfigManager.getConfig(player.getUniqueId()).exists()) {
            FileConfiguration playerConfig = PlayerConfigManager.getConfig(player.getUniqueId()).getConfig();

            if (playerConfig.get("furnaces.furnace") == null) {
                Furnace furnace = manager.createFurnace("Furnace");
                furnace.openInventory(player);

                playerConfig.set("furnaces.furnace", furnace.getUniqueID().toString());
                PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
            } else {
                Furnace furnace = manager.getByID(UUID.fromString(playerConfig.getString("furnaces.furnace")));
                furnace.openInventory(player);
            }
        }
    }

    @Override
    public void openBlastFurnace(Player player) {
        if (PlayerConfigManager.getConfig(player.getUniqueId()).exists()) {
            FileConfiguration playerConfig = PlayerConfigManager.getConfig(player.getUniqueId()).getConfig();

            if (playerConfig.get("furnaces.blast-furnace") == null) {
                Furnace blastFurnace = manager.createFurnace("Blast Furnace", FurnaceProperties.BLAST_FURNACE);
                blastFurnace.openInventory(player);

                playerConfig.set("furnaces.blast-furnace", blastFurnace.getUniqueID().toString());
                PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
            } else {
                Furnace blastFurnace = manager.getByID(UUID.fromString(playerConfig.getString("furnaces.blast-furnace")));
                blastFurnace.openInventory(player);
            }
        }
    }

    @Override
    public void openSmoker(Player player) {
        if (PlayerConfigManager.getConfig(player.getUniqueId()).exists()) {
            FileConfiguration playerConfig = PlayerConfigManager.getConfig(player.getUniqueId()).getConfig();

            if (playerConfig.get("furnaces.smoker") == null) {
                Furnace smoker = manager.createFurnace("Smoker", FurnaceProperties.SMOKER);
                smoker.openInventory(player);

                playerConfig.set("furnaces.smoker", smoker.getUniqueID().toString());
                PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
            } else {
                Furnace blastFurnace = manager.getByID(UUID.fromString(playerConfig.getString("furnaces.smoker")));
                blastFurnace.openInventory(player);
            }

        }
    }

    @Override
    public void openSmithing(Player player) {
        try {
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            FakeSmithing fakeSmithing = new FakeSmithing(containerID, player);

            ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.SMITHING, fakeSmithing.getTitle()));

            ePlayer.activeContainer = fakeSmithing;
            ePlayer.activeContainer.addSlotListener(ePlayer);
            ePlayer.activeContainer = fakeSmithing;
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(debug, ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");

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
            super(containerID, ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false; // ignore if the block is reachable, otherwise open regardless of distance.
            this.setTitle(new ChatMessage("Repair & Name"));
        }

    }

    private static class FakeSmithing extends ContainerSmithing {

        public FakeSmithing(final int containerID, final Player player) {
            super(containerID, ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false; // ignore if the block is reachable, otherwise open regardless of distance.
            this.setTitle(new ChatMessage("Upgrade Gear"));
        }

    }

    private static class FakeEnchant extends ContainerEnchantTable {
        @Getter
        private static final Map<UUID, FakeEnchant> openEnchantTables = new HashMap<>();
        public int maxLevel;

        public FakeEnchant(final int i, final Player player, int maxLevel) {
            super(i, ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;
            this.maxLevel = maxLevel;

            openEnchantTables.put(player.getUniqueId(), this);
            this.setTitle(new ChatMessage("Enchant"));
        }

        public FakeEnchant(final int i, final Player player) {
            super(i, ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;

            this.setTitle(new ChatMessage("Enchant"));
        }
    }
}
