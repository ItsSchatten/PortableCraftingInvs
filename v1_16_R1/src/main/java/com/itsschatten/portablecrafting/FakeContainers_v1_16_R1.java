package com.itsschatten.portablecrafting;

import com.itsschatten.libs.Utils;
import lombok.Getter;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FakeContainers_v1_16_R1 implements FakeContainers {
    
    boolean debug;
    
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

    public static class FakeGrindstone extends ContainerGrindstone {

        public FakeGrindstone(final int containerId, final Player player) {
            super(containerId, ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;
            this.setTitle(new ChatMessage("Repair & Disenchant"));
        }
    }

    public static class FakeCartography extends ContainerCartography {

        public FakeCartography(final int containerId, final Player player) {
            super(containerId, ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;
            this.setTitle(new ChatMessage("Cartography Table"));
        }
    }

    public static class FakeLoom extends ContainerLoom {

        public FakeLoom(final int containerId, final Player player) {
            super(containerId, ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;
            this.setTitle(new ChatMessage("Loom"));
        }
    }

    public static class FakeStoneCutter extends ContainerStonecutter {

        public FakeStoneCutter(final int containerId, final Player player) {
            super(containerId, ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false;
            this.setTitle(new ChatMessage("Stonecutter"));
        }
    }

    public static class FakeAnvil extends ContainerAnvil {

        public FakeAnvil(final int containerID, final Player player) {
            super(containerID, ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())));
            this.checkReachable = false; // ignore if the block is reachable, otherwise open regardless of distance.
            this.setTitle(new ChatMessage("Repair & Name"));
        }

    }

    public static class FakeEnchant extends ContainerEnchantTable {
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