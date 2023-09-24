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

import java.util.Collections;

/**
 * Cartography Table
 */
public class CartographyCommand extends UniversalCommand {

    public CartographyCommand() {
        super("cartography");

        setAliases(Collections.singletonList("cartographytable"));
        setPermission(Settings.USE_PERMISSIONS ? Permissions.CARTOGRAPHY.getPermission() : "");
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Permissions.CARTOGRAPHY.getPermission()));

    }

    @Override
    protected void run(CommandSender commandSender, String[] args) {
        if (!Settings.USE_CARTOGRAPHY) returnTell(Messages.FEATURE_DISABLED);

        final String cartographyTableOpen = Settings.CARTOGRAPHY_OPEN_SOUND.toUpperCase();

        if (!(commandSender instanceof Player)) {
            checkArgs(1, Messages.NOT_ENOUGH_ARGS);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

            if (PortableCraftingInvsPlugin.getFakeContainers().openCartography(target)) {
                if (Settings.USE_CARTOGRAPHY_SOUNDS) {
                    target.playSound(target.getLocation(), Sound.valueOf(cartographyTableOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                    Utils.debugLog( "Playing sound " + cartographyTableOpen + " to " + target.getName());
                }

                Utils.debugLog( "Opened the cartography table for " + target.getName());
                tellTarget(target, Messages.OPENED_CARTOGRAPHY);
                returnTell(Messages.OPENED_CARTOGRAPHY_OTHER.replace("{player}", target.getName()));
            }
            return;
        }

        final Player player = (Player) commandSender;
        if (Settings.USE_PERMISSIONS) checkPerms(player, Permissions.CARTOGRAPHY);

        if (args.length == 0) {
            if (PortableCraftingInvsPlugin.getFakeContainers().openCartography(player)) {
                if (Settings.USE_CARTOGRAPHY_SOUNDS) {
                    player.playSound(player.getLocation(), Sound.valueOf(cartographyTableOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                    Utils.debugLog( "Playing sound " + cartographyTableOpen + " to " + player.getName());
                }

                Utils.debugLog( "Opened the cartography table for " + player.getName());

                returnTell(Messages.OPENED_CARTOGRAPHY);
            }
        }

        if (args.length == 1) {
            if (Settings.USE_PERMISSIONS) checkPerms(player, Permissions.CARTOGRAPHY_OTHER);

            Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

            if (PortableCraftingInvsPlugin.getFakeContainers().openCartography(target)) {
                if (Settings.USE_CARTOGRAPHY_SOUNDS) {
                    target.playSound(target.getLocation(), Sound.valueOf(cartographyTableOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                    Utils.debugLog( "Playing sound " + cartographyTableOpen + " to " + target.getName());
                }

                Utils.debugLog( "Opened the cartography table for " + target.getName());
                tellTarget(target, Messages.OPENED_CARTOGRAPHY);
                returnTell(Messages.OPENED_CARTOGRAPHY_OTHER.replace("{player}", target.getName()));
            }
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOO_MANY_ARGS);
        }

    }

}
