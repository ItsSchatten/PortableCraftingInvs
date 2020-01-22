package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.UniversalCommand;
import com.itsschatten.portablecrafting.Permissions;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.utils.FakeContainers;
import net.minecraft.server.v1_15_R1.Containers;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutOpenWindow;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class LoomCommand extends UniversalCommand {

    public LoomCommand() {
        super("loom");

        setPermission(Permissions.LOOM.getPermission());
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Permissions.LOOM.getPermission()));
    }

    @Override
    protected void run(CommandSender commandSender, String[] args) {
        if (!Settings.USE_LOOM) returnTell(Messages.FEATURE_DISABLED);

        final String loomSoundOpen = Settings.LOOM_OPEN_SOUND.toUpperCase();

        if (!(commandSender instanceof Player)) {
            checkArgs(1, Messages.NOT_ENOUGH_ARGS);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

            if (Settings.USE_LOOM_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(loomSoundOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + loomSoundOpen + " to " + target.getName());
            }

            openLoom(target);
            Utils.debugLog(Settings.DEBUG, "Opened the loom for " + target.getName());
            tellTarget(target, Messages.OPENED_LOOM);
            returnTell(Messages.OPENED_LOOM_OTHER.replace("{player}", target.getName()));
        }

        final Player player = (Player) commandSender;
        checkPerms(player, Permissions.LOOM);

        if (args.length == 0) {
            if (Settings.USE_LOOM_SOUNDS) {
                player.playSound(player.getLocation(), Sound.valueOf(loomSoundOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + loomSoundOpen + " to " + player.getName());
            }

            openLoom(player);
            Utils.debugLog(Settings.DEBUG, "Opened the loom for " + player.getName());

            returnTell(Messages.OPENED_LOOM);

        }

        if (args.length == 1) {
            checkPerms(player, Permissions.LOOM_OTHER);

            Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

            if (Settings.USE_LOOM_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(loomSoundOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + loomSoundOpen + " to " + target.getName());
            }

            openLoom(player);

            Utils.debugLog(Settings.DEBUG, "Opened the loom for " + target.getName());

            tellTarget(target, Messages.OPENED_LOOM);
            returnTell(Messages.OPENED_LOOM_OTHER.replace("{player}", target.getName()));
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOO_MANY_ARGS);
        }

    }

    private void openLoom(final Player player) {
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
    }
}
