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
 * Anvil
 */
public class AnvilCommand extends UniversalCommand {

    public AnvilCommand() {
        super("anvil");

        setAliases(Arrays.asList("openanvil", "anv"));
        setPermission(Permissions.ANVIL.getPermission());
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Permissions.ANVIL.getPermission()));
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        if (!Settings.USE_ANVIL) returnTell(Messages.FEATURE_DISABLED); // Check if feature is enabled.

        final String anvilOpenSound = Settings.ANVIL_OPEN_SOUND.toUpperCase(); // Set sound.

        if (!(sender instanceof Player)) {
            checkArgs(1, Messages.NOT_ENOUGH_ARGS);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

            if (Settings.USE_ANVIL_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(anvilOpenSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + anvilOpenSound + " to " + target.getName());
            }

            PortableCraftingInvsPlugin.getFakeContainers().openAnvil(target);
            Utils.debugLog(Settings.DEBUG, "Opened the anvil for " + target.getName());
            tellTarget(target, Messages.OPENED_ANVIL);
            returnTell(Messages.OPENED_ANVIL_OTHER.replace("{player}", target.getName()));
            return;
        }

        final Player player = (Player) sender;

        checkPerms(player, Permissions.ANVIL); // Check perms again.

        if (args.length == 0) {

            if (Settings.USE_ANVIL_SOUNDS) {
                player.playSound(player.getLocation(), Sound.valueOf(anvilOpenSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + anvilOpenSound + " to " + player.getName());
            }

            PortableCraftingInvsPlugin.getFakeContainers().openAnvil(player);

            Utils.debugLog(Settings.DEBUG, "Opened the anvil for " + player.getName());

            returnTell(Messages.OPENED_ANVIL);
        }

        if (args.length == 1) {
            checkPerms(player, Permissions.ANVIL_OTHER); // Check perms.

            Player target = Bukkit.getPlayer(args[0]); // Set target.
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0])); // Make sure not null.


            if (Settings.USE_ANVIL_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(anvilOpenSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + anvilOpenSound + " to " + target.getName());
            }

            PortableCraftingInvsPlugin.getFakeContainers().openAnvil(target);
            Utils.debugLog(Settings.DEBUG, "Opened the anvil for " + target.getName());

            tellTarget(target, Messages.OPENED_ANVIL);
            returnTell(Messages.OPENED_ANVIL_OTHER.replace("{player}", target.getName()));
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOO_MANY_ARGS);
        }
    }


}
