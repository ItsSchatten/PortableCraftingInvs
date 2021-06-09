package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.UniversalCommand;
import com.itsschatten.portablecrafting.Permissions;
import com.itsschatten.portablecrafting.PortableCraftingInvsPlugin;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Grindstone
 */
public class GrindStoneCommand extends UniversalCommand {

    public GrindStoneCommand() {
        super("grindstone");

        setAliases(Arrays.asList("grstone", "gstone"));
        setPermission(Settings.USE_PERMISSIONS ? Permissions.GRINDSTONE.getPermission() : "");
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Permissions.GRINDSTONE.getPermission()));
    }

    @Override
    protected void run(CommandSender commandSender, String[] args) {
        if (!Settings.USE_GRINDSTONE) // Check if the feature is enabled.
            returnTell(Messages.FEATURE_DISABLED);

        final String grindStoneOpen = Settings.GRINDSTONE_OPEN_SOUND.toUpperCase();

        if (!(commandSender instanceof Player)) {
            checkArgs(1, Messages.NOT_ENOUGH_ARGS);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

            if (PortableCraftingInvsPlugin.getFakeContainers().openGrindStone(target)) {
                if (Settings.USE_GRINDSTONE_SOUNDS) {
                    target.playSound(target.getLocation(), Sound.valueOf(grindStoneOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                    Utils.debugLog(Settings.DEBUG, "Playing sound " + grindStoneOpen + " to " + target.getName());
                }

                Utils.debugLog("Opened the grindstone for " + target.getName());
                tellTarget(target, Messages.OPENED_GRINDSTONE);
                returnTell(Messages.OPENED_GRINDSTONE_OTHER.replace("{target}", target.getName()));
            }
            return;
        }

        final Player player = (Player) commandSender;
        if (Settings.USE_PERMISSIONS) checkPerms(player, Permissions.GRINDSTONE);

        if (args.length == 0) {

            if (PortableCraftingInvsPlugin.getFakeContainers().openGrindStone(player)) {
                if (Settings.USE_GRINDSTONE_SOUNDS) {
                    player.playSound(player.getLocation(), Sound.valueOf(grindStoneOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                    Utils.debugLog(Settings.DEBUG, "Playing sound " + grindStoneOpen + " to " + player.getName());
                }

                Utils.debugLog(Settings.DEBUG, "Opened the grindstone for " + player.getName());
                returnTell(Messages.OPENED_GRINDSTONE);
            }
        }

        if (args.length == 1) {
            if (Settings.USE_PERMISSIONS) checkPerms(player, Permissions.GRINDSTONE_OTHER);

            Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{target}", args[0]));

            if (PortableCraftingInvsPlugin.getFakeContainers().openGrindStone(target)) {
                if (Settings.USE_GRINDSTONE_SOUNDS) {
                    target.playSound(target.getLocation(), Sound.valueOf(grindStoneOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                    Utils.debugLog(Settings.DEBUG, "Playing sound " + grindStoneOpen + " to " + target.getName());
                }

                Utils.debugLog("Opened the grindstone for " + target.getName());
                tellTarget(target, Messages.OPENED_GRINDSTONE);
                returnTell(Messages.OPENED_GRINDSTONE_OTHER.replace("{target}", target.getName()));
            }

        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOO_MANY_ARGS);
        }

    }
}
