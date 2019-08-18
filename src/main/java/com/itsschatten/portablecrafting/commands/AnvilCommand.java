package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.UniversalCommand;
import com.itsschatten.portablecrafting.Perms;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.Arrays;

/**
 * Anvil
 */
public class AnvilCommand extends UniversalCommand {

    public AnvilCommand() {
        super("anvil");

        setAliases(Arrays.asList("openanvil", "anv"));
        setPermission(Perms.ANVIL.getPermission());
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Perms.ANVIL.getPermission()));
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        if (!Settings.USE_ANVIL) returnTell(Messages.FEATURE_DISABLED); // Check if feature is enabled.

        final String anvilOpenSound = Settings.ANVIL_OPEN_SOUND.toUpperCase(); // Set sound.

        if (!(sender instanceof Player)) {
            if (args.length == 0)
                returnTell(Messages.NOTENOUGH_ARGS);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST.replace("{player}", args[0]));

            if (Settings.USE_ANVIL_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(anvilOpenSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + anvilOpenSound + " to " + target.getName());
            }

            openAnvil(target);
            Utils.debugLog(Settings.DEBUG, "Opened the anvil for " + target.getName());
            tellTarget(target, Messages.OPENED_ANVIL);
            returnTell(Messages.OPENED_ANVIL_OTHER.replace("{player}", target.getName()));

            return;
        }

        final Player player = (Player) sender;

        checkPerms(player, Perms.ANVIL); // Check perms again.

        if (args.length == 0) {

            if (Settings.USE_ANVIL_SOUNDS) {
                player.playSound(player.getLocation(), Sound.valueOf(anvilOpenSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + anvilOpenSound + " to " + player.getName());
            }

            player.openInventory(Bukkit.createInventory(player, InventoryType.ANVIL));

            openAnvil(player);

            Utils.debugLog(Settings.DEBUG, "Opened the anvil for " + player.getName());

            returnTell(Messages.OPENED_ANVIL);
        }

        if (args.length == 1) {
            checkPerms(player, Perms.ANVIL_OTHER); // Check perms.

            Player target = Bukkit.getPlayer(args[0]); // Set target.
            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST); // Make sure not null.


            if (Settings.USE_ANVIL_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(anvilOpenSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + anvilOpenSound + " to " + target.getName());
            }

            openAnvil(target);
            Utils.debugLog(Settings.DEBUG, "Opened the anvil for " + target.getName());

            tellTarget(target, Messages.OPENED_ANVIL);
            returnTell(Messages.OPENED_ANVIL_OTHER.replace("{player}", target.getName()));
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOOMANY_ARGS);
        }
    }

    private void openAnvil(final Player player) { // Using NMS create an anvil.
        try {
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            FakeAnvil fakeAnvil = new FakeAnvil(containerID, player);

            ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.ANVIL, new ChatMessage("Repairing")));

            ePlayer.activeContainer = fakeAnvil;
            ePlayer.activeContainer.addSlotListener(ePlayer);
            ePlayer.activeContainer = fakeAnvil;
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.debugLog(Settings.DEBUG, ex.getStackTrace().toString());

        }
    }

    class FakeAnvil extends ContainerAnvil {
        public FakeAnvil(int containerID, Player player) {
            super(containerID, ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(0, 0, 0)));
            this.checkReachable = false; // ignore if the block is reachable, otherwise open regardless of distance.
            setTitle(new ChatMessage("Repair & Name"));
        }

    }

}
