package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.UniversalCommand;
import com.itsschatten.portablecrafting.Permissions;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * This class handles the crafting table.
 */
public class CraftCommand extends UniversalCommand {
    @Getter
    @Setter
    static CraftCommand instance;

    public CraftCommand() {
        super("craft"); // The command.

        setInstance(this);

        setAliases(Arrays.asList("crafting", "craftingtable", "workbench"));
        setPermission(Settings.USE_PERMISSIONS ? Permissions.CRAFTING.getPermission() : "");
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Permissions.CRAFTING.getPermission()));
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        if (!Settings.USE_CRAFTING) returnTell(Messages.FEATURE_DISABLED); // Check if the feature is enabled.

        final String craftOpenSound = Settings.CRAFTING_OPEN_SOUND.toUpperCase(); // Sets the sound when the crafting table is opened. (even if not used.)

        if (!(sender instanceof Player)) {

            checkArgs(1, Messages.NOT_ENOUGH_ARGS);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

            if (Settings.USE_CRAFTING_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(craftOpenSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f); // If sounds are enabled play a sound, if random pitch at random.
                Utils.debugLog(Settings.DEBUG, "Played the sound " + craftOpenSound + " to player " + target.getName());
            }

            target.openWorkbench(target.getLocation(), true);
            Utils.debugLog(Settings.DEBUG, "Opened the crafting inventory for" + target.getName() + ".");

            if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
                returnTell(Messages.TOO_MANY_ARGS);
            }

            return;
        }

        final Player player = (Player) sender;

        if (Settings.USE_PERMISSIONS)checkPerms(player, Permissions.CRAFTING); // Check for permission again.

        if (args.length == 0) { // If no arguments, open a crafting table for the sender.
            if (Settings.USE_CRAFTING_SOUNDS) {
                player.playSound(player.getLocation(), Sound.valueOf(craftOpenSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f); // If sounds are enabled play a sound, if random pitch at random.
                Utils.debugLog(Settings.DEBUG, "Played the sound " + craftOpenSound + " to player " + player.getName());
            }

            player.openWorkbench(player.getLocation(), true); // Opens the workBench.
            Utils.debugLog(Settings.DEBUG, "Opened the crafting inventory.");

            returnTell(Messages.OPENED_CRAFTING); // Sends a message to the player.
        }

        if (args.length == 1) {
            if (Settings.USE_PERMISSIONS)
                checkPerms(player, Permissions.CRAFTING_OTHER); // Check if the sender of the command has permission to run it as other.
            Player target = Bukkit.getPlayer(args[0]); // Sets the target.

            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0])); // Make sure the player isn't null.

            if (Settings.USE_CRAFTING_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(craftOpenSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Played sound " + craftOpenSound + " to player " + target.getName());
            }

            target.openWorkbench(target.getLocation(), true);
            Utils.debugLog(Settings.DEBUG, "Opened the crafting inventory for" + target.getName() + ".");

            tellTarget(target, Messages.OPENED_CRAFTING);
            returnTell(Messages.OPENED_CRAFTING_OTHER.replace("{player}", target.getName()));
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOO_MANY_ARGS);
        }
        return;
    }
}
