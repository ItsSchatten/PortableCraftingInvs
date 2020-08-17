package com.itsschatten.portablecrafting;

import com.itsschatten.libs.Utils;
import lombok.Getter;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.*;

public class FakeContainers_v1_16_R1 implements FakeContainers, Listener {

    private static Map<UUID, FakeFurnace> smokers;
    private static Map<UUID, FakeFurnace> furnaces;
    private static Map<UUID, FakeFurnace> blastFurnaces;
    boolean debug;

    public FakeContainers_v1_16_R1(Plugin plugin) {
        furnaces = new HashMap<>();
        blastFurnaces = new HashMap<>();
        smokers = new HashMap<>();

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> furnaces.forEach((uuid, furnace) -> furnace.tick()), 0, 0);
    }

    @EventHandler
    public void onFurnaceClick(final InventoryClickEvent event) {

        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE)
            return;

        if (event.getClickedInventory() == null)
            return;

        if (event.getClickedInventory().getType() == InventoryType.FURNACE) {
            Utils.log(event.getSlot() + "");

            if (event.getCursor() != null) {
                if (!furnaces.containsKey(event.getWhoClicked().getUniqueId()))
                    return;

                FakeFurnace fakeFurnace = furnaces.get(event.getWhoClicked().getUniqueId());
                if (event.getSlot() == 1) {

                    if (event.getCursor() == null)
                        return;

                    Utils.log(fakeFurnace.burnTime + " BEFORE");
                    fakeFurnace.setFurnaceProperty(1, TileEntityFurnace.f().get(CraftItemStack.asNMSCopy(event.getCursor()).getItem()));
                    Utils.log(fakeFurnace.burnTime + " AFTER");

                }

                if (fakeFurnace.cookTimeTotal == 0) {
                    if (event.getInventory().getItem(0) != null)
                        fakeFurnace.cookTimeTotal = event.getInventory().getItem(0).getAmount() * 200;
                }


                Utils.log(event.getCurrentItem() + "");
            }


        }
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
        try {
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();

            FakeFurnace fakeFurnace = new FakeFurnace(player);

            ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.FURNACE, new ChatMessage("Furnace")));

            ePlayer.activeContainer = fakeFurnace.createMenu(containerID, ((CraftPlayer) player).getHandle().inventory, ((CraftPlayer) player).getHandle());

        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(debug, ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");

        }
    }

    @Override
    public void openBlastFurnace(Player player) {

    }

    @Override
    public void openSmoker(Player player) {

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

    private static class FakeFurnace extends TileEntityFurnaceFurnace implements IInventory {

        private static World mainWorld;
        private static Method fuelTimeMeth;

        static {
            try {
                fuelTimeMeth = TileEntityFurnace.class.getDeclaredMethod("fuelTime", ItemStack.class);
                fuelTimeMeth.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private final ItemStack[] contents = new ItemStack[3];
        private final IInventory inventory;
        private final Player player;
        private double cookingTime = 0D;
        private Item lastId;

        public FakeFurnace(Player player) {
            world = ((CraftWorld) player.getLocation().getWorld()).getHandle();

            inventory = ((CraftPlayer) player).getHandle().inventory;

            this.player = player;

            setFurnaceProperty(0, 0);
            setFurnaceProperty(1, 0);
            setFurnaceProperty(2, 0);
            setFurnaceProperty(3, 0);

            furnaces.put(player.getUniqueId(), this);
        }

        public void setFurnaceProperty(int i, int j) {
            b.setProperty(i, j);
        }

        @Override
        public void tick() {
            if (contents[0] == null || contents[1] == null)
                return;
            PacketPlayOutWindowData fuelBurnTimePacket = new PacketPlayOutWindowData(((CraftPlayer) player).getHandle().activeContainer.windowId, 0, this.fuelTime(contents[1]));
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(fuelBurnTimePacket);
        }

        public int getBurnTime(IInventory inventory) {
            FurnaceRecipe recipe = getRecipe(inventory);
            return (recipe != null) ? recipe.getCookingTime() : 200;
        }

        public FurnaceRecipe getRecipe(IInventory inventory) {
            if (world == null)
                return null;
            return world.getCraftingManager().craft(Recipes.SMELTING, inventory, world).orElse(null);
        }

        @Override
        public int getSize() {
            return contents.length;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void setItem(int i, ItemStack itemstack) {

            if (itemstack == ItemStack.b)
                itemstack = null;

            contents[i] = itemstack;
            int max = getMaxStackSize();

            if (itemstack != null && itemstack.getCount() > max)
                itemstack.setCount(max);

        }

        @Override
        public ItemStack getItem(int i) {
            return contents[i] == null ? ItemStack.b : contents[i];
        }

        @Override
        public ItemStack splitStack(int i, int i1) {

            if (contents[i] != null) {
                ItemStack item;
                if (contents[i].getCount() <= i1) {
                    item = contents[i1];
                    contents[i] = null;
                } else {
                    item = contents[i].cloneAndSubtract(i1);

                    if (contents[i].getCount() == 0) {
                        contents[i] = null;
                    }

                }
                return item == null ? ItemStack.b : item;
            } else
                return null;
        }

        @Override
        public ItemStack splitWithoutUpdate(int i) {
            if (contents[i] != null) {
                ItemStack itemstack = contents[i];
                contents[i] = null;
                return itemstack == null ? ItemStack.b : itemstack;
            } else {
                return null;
            }
        }

        @Override
        public int getMaxStackSize() {
            return 64;
        }

        @Override
        public void setMaxStackSize(int i) {

        }

        @Override
        public void update() {
            super.update();
        }

        @Override
        public boolean a(EntityHuman entityHuman) {
            return true;
        }

        @Override
        public List<ItemStack> getContents() {
            return Arrays.asList(contents);
        }

        @Override
        public void onOpen(CraftHumanEntity craftHumanEntity) {
            super.onOpen(craftHumanEntity);
        }

        @Override
        public void onClose(CraftHumanEntity craftHumanEntity) {
            super.onClose(craftHumanEntity);
        }

        @Override
        public List<HumanEntity> getViewers() {
            return new ArrayList<>();
        }

        @Override
        public InventoryHolder getOwner() {
            return null;
        }

        @Override
        public Location getLocation() {
            return null;
        }

        @Override
        public void clear() {

        }
    }

}
