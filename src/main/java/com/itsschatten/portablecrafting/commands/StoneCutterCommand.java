package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.UniversalCommand;
import com.itsschatten.portablecrafting.Permissions;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.utils.FakeContainers;
import net.minecraft.server.v1_16_R1.Containers;
import net.minecraft.server.v1_16_R1.EntityPlayer;
import net.minecraft.server.v1_16_R1.PacketPlayOutOpenWindow;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class StoneCutterCommand extends UniversalCommand {

    public StoneCutterCommand() {
        super("stonecutter");

        setPermission(Permissions.STONE_CUTTER.getPermission());
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Permissions.STONE_CUTTER.getPermission()));

    }

    @Override
    protected void run(CommandSender commandSender, String[] args) {
        if (!Settings.USE_CARTOGRAPHY) returnTell(Messages.FEATURE_DISABLED);

        final String stoneCutterOpen = Settings.STONE_CUTTER_OPEN_SOUND.toUpperCase();

        if (!(commandSender instanceof Player)) {
            checkArgs(1, Messages.NOT_ENOUGH_ARGS);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

            if (Settings.USE_STONE_CUTTER_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(stoneCutterOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + stoneCutterOpen + " to " + target.getName());
            }

            openStoneCutter(target);
            Utils.debugLog(Settings.DEBUG, "Opened the stone cutter for " + target.getName());
            tellTarget(target, Messages.OPENED_STONE_CUTTER);
            returnTell(Messages.OPENED_STONE_CUTTER_OTHER.replace("{player}", target.getName()));
        }

        final Player player = (Player) commandSender;
        checkPerms(player, Permissions.STONE_CUTTER);

        if (args.length == 0) {
            if (Settings.USE_STONE_CUTTER_SOUNDS) {
                player.playSound(player.getLocation(), Sound.valueOf(stoneCutterOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + stoneCutterOpen + " to " + player.getName());
            }

            openStoneCutter(player);
            Utils.debugLog(Settings.DEBUG, "Opened the stone cutter for " + player.getName());

            returnTell(Messages.OPENED_STONE_CUTTER);

        }

        if (args.length == 1) {
            checkPerms(player, Permissions.STONE_CUTTER_OTHER);

            Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

            if (Settings.USE_STONE_CUTTER_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(stoneCutterOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + stoneCutterOpen + " to " + target.getName());
            }

            openStoneCutter(player);

            tellTarget(target, Messages.OPENED_STONE_CUTTER);
            returnTell(Messages.OPENED_STONE_CUTTER_OTHER.replace("{player}", target.getName()));
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOO_MANY_ARGS);
        }

    }


    private void openStoneCutter(final Player player) {
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
    }
}
