package com.itsschatten.portablecrafting.listeners;

import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.Perms;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.configs.SignsConfig;
import com.itsschatten.portablecrafting.utils.FakeContainers;
import net.minecraft.server.v1_14_R1.Containers;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.PacketPlayOutOpenWindow;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {

    @EventHandler
    public void onSign(SignChangeEvent event) {
        if (!event.getPlayer().hasPermission(Perms.SIGN_CREATE.getPermission())) return;

        if (event.getLine(0) == null) {
            return;
        }

        switch (event.getLine(0).toLowerCase()) {
            case "[anvil]": {
                if (!event.getPlayer().hasPermission(Perms.SIGN_CREATE_ANVIL.getPermission())) return;
                event.setLine(0, Utils.colorize(Messages.ANVIL_SIGN));

                makeSign(event, SignTypes.ANVIL);

                Utils.tell(event.getPlayer(), Messages.ANVIL_SIGN_CREATED);
                break;
            }

            case "[cartography]": {
                if (!event.getPlayer().hasPermission(Perms.SIGN_CREATE_CATROGRAPHY.getPermission())) return;
                event.setLine(0, Utils.colorize(Messages.CARTOGRAPHY_SIGN));

                makeSign(event, SignTypes.CARTOGRAPHY);

                Utils.tell(event.getPlayer(), Messages.CARTOGRAPHY_SIGN_CREATED);
                break;
            }

            case "[crafting]": {
                if (!event.getPlayer().hasPermission(Perms.SIGN_CREATE_CRAFTING.getPermission())) return;
                event.setLine(0, Utils.colorize(Messages.CRAFTING_SIGN));

                makeSign(event, SignTypes.CRAFTING_TABLE);

                Utils.tell(event.getPlayer(), Messages.CRAFTING_SIGN_CREATED);
                break;
            }

            case "[enchanttable]": {
                if (!event.getPlayer().hasPermission(Perms.SIGN_CREATE_ENCHANTTABLE.getPermission())) return;
                event.setLine(0, Utils.colorize(Messages.ENCHANTTABLE_SIGN));

                makeSign(event, SignTypes.ENCHANTMENT_TABLE);

                Utils.tell(event.getPlayer(), Messages.ENCHANTTABLE_SIGN_CREATED);
                break;
            }

            case "[enderchest]": {
                if (!event.getPlayer().hasPermission(Perms.SIGN_CREATE_ENDERCHEST.getPermission())) return;
                event.setLine(0, Utils.colorize(Messages.ENDERCHEST_SIGN));

                makeSign(event, SignTypes.ENDER_CHEST);

                Utils.tell(event.getPlayer(), Messages.ENDERCHEST_SIGN_CREATED);
                break;
            }

            case "[grindstone]": {
                if (!event.getPlayer().hasPermission(Perms.SIGN_CREATE_GRINDSTONE.getPermission())) return;
                event.setLine(0, Utils.colorize(Messages.GRINDSTONE_SIGN));

                makeSign(event, SignTypes.GRINDSTONE);

                Utils.tell(event.getPlayer(), Messages.GRINDSTONE_SIGN_CREATED);
                break;
            }

            case "[loom]": {
                if (!event.getPlayer().hasPermission(Perms.SIGN_CREATE_LOOM.getPermission())) return;
                event.setLine(0, Utils.colorize(Messages.LOOM_SIGN));

                makeSign(event, SignTypes.LOOM);

                Utils.tell(event.getPlayer(), Messages.LOOM_SIGN_CREATED);
                break;
            }

            case "[stonecutter]": {
                if (!event.getPlayer().hasPermission(Perms.SIGN_CREATE_STONECUTTER.getPermission())) return;
                event.setLine(0, Utils.colorize(Messages.STONECUTTER_SIGN));

                makeSign(event, SignTypes.STONECUTTER);

                Utils.tell(event.getPlayer(), Messages.STONECUTTER_SIGN_CREATED);
                break;
            }

            default: {
                break;
            }
        }
    }

    @EventHandler
    public void onSignInteract(PlayerInteractEvent event) {

        if (isSign(event)) {
            final SignsConfig signsConfig = SignsConfig.getInstance();

            if (Settings.REQUIRE_SIGHT_CLICK_BREAK_SIGN && event.getAction() == Action.LEFT_CLICK_BLOCK && event.getPlayer().isSneaking()) {
                String path;

                for (String key : signsConfig.getConfigurationSection("signs").getKeys(false)) {
                    if (signsConfig.get("signs." + key + ".where.world") == event.getPlayer().getLocation().getWorld().getName()
                            && signsConfig.getInt("signs." + key + ".where.x") == event.getClickedBlock().getX()
                            && signsConfig.getInt("signs." + key + ".where.y") == event.getClickedBlock().getY()
                            && signsConfig.getInt("signs." + key + ".where.z") == event.getClickedBlock().getZ()) {
                        path = "signs." + key;
                        signsConfig.set(path, null);
                        signsConfig.set("sign-amount", SignsConfig.getInstance().getInt("sign-amount") - 1);
                        signsConfig.saveConfig();
                    }
                }
                return;
            }

            if (Settings.REQUIRE_SIGHT_CLICK_BREAK_SIGN && event.getAction() == Action.LEFT_CLICK_BLOCK && event.getPlayer().hasPermission(Perms.SIGN_CREATE.getPermission())) {
                Utils.tell(event.getPlayer(), Messages.MUST_SHIFT_CLICK_TO_BREAK_SIGN);
                event.setCancelled(true);
            }

            for (String key : signsConfig.getConfigurationSection("signs").getKeys(false)) {
                if (signsConfig.get("signs." + key + ".where.world") == event.getPlayer().getLocation().getWorld().getName()
                        && signsConfig.getInt("signs." + key + ".where.x") == event.getClickedBlock().getX()
                        && signsConfig.getInt("signs." + key + ".where.y") == event.getClickedBlock().getY()
                        && signsConfig.getInt("signs." + key + ".where.z") == event.getClickedBlock().getZ()) {

                    getSign(event, SignTypes.valueOf(signsConfig.getString("signs." + key + ".type")));

                }
            }
        }

    }

    private boolean isSign(PlayerInteractEvent event) {
        final SignsConfig signsConfig = SignsConfig.getInstance();

        if (event.getClickedBlock() == null) {
            return false;
        }

        if (event.getClickedBlock().getType().name().contains("SIGN") || event.getClickedBlock().getType().name().contains("WALL_SIGN")) {

            if (signsConfig.getConfigurationSection("signs") == null || signsConfig.getConfigurationSection("signs").getKeys(false).isEmpty()) {
                return false;
            }

            for (String key : signsConfig.getConfigurationSection("signs").getKeys(false)) {
                if (signsConfig.get("signs." + key + ".where.world") == event.getPlayer().getLocation().getWorld().getName()
                        && signsConfig.getInt("signs." + key + ".where.x") == event.getClickedBlock().getX()
                        && signsConfig.getInt("signs." + key + ".where.y") == event.getClickedBlock().getY()
                        && signsConfig.getInt("signs." + key + ".where.z") == event.getClickedBlock().getZ()) {

                    return true;
                }
            }
        }

        return false;
    }

    private void makeSign(SignChangeEvent event, SignTypes signType) {

        final SignsConfig signs = SignsConfig.getInstance();
        signs.set("sign-amount", signs.getInt("sign-amount") + 1);

        signs.set("signs." + signs.getInt("sign-amount") + ".where.world", event.getBlock().getWorld().getName());
        signs.set("signs." + signs.getInt("sign-amount") + ".where.x", event.getBlock().getX());
        signs.set("signs." + signs.getInt("sign-amount") + ".where.y", event.getBlock().getY());
        signs.set("signs." + signs.getInt("sign-amount") + ".where.z", event.getBlock().getZ());
        signs.set("signs." + signs.getInt("sign-amount") + ".type", signType.name());
        signs.set("signs." + signs.getInt("sign-amount") + ".created-by", event.getPlayer().getName());

        signs.saveConfig();

    }

    private void getSign(PlayerInteractEvent event, SignTypes signTypes) {
        final Player player = event.getPlayer();

        switch (signTypes) {
            case ANVIL: {
                if (!event.getPlayer().hasPermission(Perms.USE_SIGN_ANVIL.getPermission())) return;
                try {
                    EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
                    int containerID = ePlayer.nextContainerCounter();
                    FakeContainers.FakeAnvil fakeAnvil = new FakeContainers.FakeAnvil(containerID, player);

                    ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.ANVIL, fakeAnvil.getTitle()));


                    ePlayer.activeContainer = fakeAnvil;
                    ePlayer.activeContainer.addSlotListener(ePlayer);
                    ePlayer.activeContainer = fakeAnvil;
                } catch (UnsupportedOperationException ex) {
                    // Logging this error normally spams console
                    Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
                    Utils.debugLog(Settings.DEBUG, ex.getMessage());
                    player.sendMessage("An error occurred, please contact an administrator.");

                }
                break;
            }

            case CARTOGRAPHY: {
                if (!event.getPlayer().hasPermission(Perms.USE_SIGN_CATROGRAPHY.getPermission())) return;
                try {
                    EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
                    int containerID = ePlayer.nextContainerCounter();
                    FakeContainers.FakeCartography fakeCartography = new FakeContainers.FakeCartography(containerID, player);

                    ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.CARTOGRAPHY, fakeCartography.getTitle()));

                    ePlayer.activeContainer = fakeCartography;
                    ePlayer.activeContainer.addSlotListener(ePlayer);
                    ePlayer.activeContainer = fakeCartography;
                } catch (UnsupportedOperationException ex) {
                    // Logging this error normally spams console
                    Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
                    Utils.debugLog(Settings.DEBUG, ex.getMessage());
                    player.sendMessage("An error occurred, please contact an administrator.");

                }
                break;
            }

            case CRAFTING_TABLE: {
                if (!event.getPlayer().hasPermission(Perms.USE_SIGN_CRAFTING.getPermission())) return;
                player.openWorkbench(player.getLocation(), true);
                break;
            }

            case ENCHANTMENT_TABLE: {
                if (!event.getPlayer().hasPermission(Perms.USE_SIGN_ENCHANTTABLE.getPermission())) return;
                try {
                    EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
                    int containerID = ePlayer.nextContainerCounter();
                    FakeContainers.FakeEnchant fakeEnchant = new FakeContainers.FakeEnchant(containerID, player);

                    ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.ENCHANTMENT, fakeEnchant.getTitle()));

                    ePlayer.activeContainer = fakeEnchant;
                    ePlayer.activeContainer.addSlotListener(ePlayer);
                    ePlayer.activeContainer = fakeEnchant;
                } catch (UnsupportedOperationException ex) {
                    // Logging this error normally spams console
                    Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
                    Utils.debugLog(Settings.DEBUG, ex.getMessage());
                    player.sendMessage("An error occurred, please contact an administrator.");

                }
                break;
            }

            case ENDER_CHEST: {
                if (!event.getPlayer().hasPermission(Perms.USE_SIGN_ENDERCHEST.getPermission())) return;
                player.openInventory(player.getEnderChest());
                break;
            }

            case GRINDSTONE: {
                if (!event.getPlayer().hasPermission(Perms.USE_SIGN_GRINDSTONE.getPermission())) return;
                try {
                    EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
                    int containerId = ePlayer.nextContainerCounter();
                    FakeContainers.FakeGrindstone fakeGrindstone = new FakeContainers.FakeGrindstone(containerId, player);

                    ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId, Containers.GRINDSTONE, fakeGrindstone.getTitle()));

                    ePlayer.activeContainer = fakeGrindstone;
                    ePlayer.activeContainer.addSlotListener(ePlayer);
                    ePlayer.activeContainer = fakeGrindstone;

                } catch (UnsupportedOperationException ex) {
                    Utils.debugLog(Settings.DEBUG, ex.getMessage());
                    Utils.log("An error occurred while running the grindstone command, make sure you have debug enabled to see this message.");
                    player.sendMessage("An error occurred, please contact an administrator.");
                }
                break;
            }

            case LOOM: {
                if (!event.getPlayer().hasPermission(Perms.USE_SIGN_LOOM.getPermission())) return;
                try {
                    EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
                    int containerID = ePlayer.nextContainerCounter();
                    FakeContainers.FakeLoom fakeLoom = new FakeContainers.FakeLoom(containerID, player);

                    ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.LOOM, fakeLoom.getTitle()));

                    ePlayer.activeContainer = fakeLoom;
                    ePlayer.activeContainer.addSlotListener(ePlayer);
                    ePlayer.activeContainer = fakeLoom;
                } catch (UnsupportedOperationException ex) {
                    // Logging this error normally spams console
                    Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
                    Utils.debugLog(Settings.DEBUG, ex.getMessage());
                    player.sendMessage("An error occurred, please contact an administrator.");

                }
                break;
            }

            case STONECUTTER: {
                if (!event.getPlayer().hasPermission(Perms.USE_SIGN_STONECUTTER.getPermission())) return;
                try {
                    EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
                    int containerID = ePlayer.nextContainerCounter();
                    FakeContainers.FakeStoneCutter fakeStoneCutter = new FakeContainers.FakeStoneCutter(containerID, player);

                    ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.STONECUTTER, fakeStoneCutter.getTitle()));

                    ePlayer.activeContainer = fakeStoneCutter;
                    ePlayer.activeContainer.addSlotListener(ePlayer);
                    ePlayer.activeContainer = fakeStoneCutter;
                } catch (UnsupportedOperationException ex) {
                    // Logging this error normally spams console
                    Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
                    Utils.debugLog(Settings.DEBUG, ex.getMessage());
                    player.sendMessage("An error occurred, please contact an administrator.");

                }
                break;
            }
            default:
                break;
        }

    }

    enum SignTypes {
        ANVIL,
        CARTOGRAPHY,
        CRAFTING_TABLE,
        ENCHANTMENT_TABLE,
        ENDER_CHEST,
        GRINDSTONE,
        LOOM,
        STONECUTTER
    }

}
