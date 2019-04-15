package com.itsshadow.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.PlayerCommand;
import com.itsshadow.portablecrafting.Perms;
import com.itsshadow.portablecrafting.configs.Messages;
import com.itsshadow.portablecrafting.configs.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * This class handles the crafting table.
 */
public class CraftCommand extends PlayerCommand {

    public CraftCommand() {
        super("craft"); // The command.

        setAliases(Arrays.asList("crafting", "craftingtable", "workbench"));
        setPermission(Perms.CRAFTING.getPermission());
        setPermissionMessage(Perms.CRAFTING.getNoPermission().replace("{prefix}", Messages.PREFIX).replace("{permission}", Perms.CRAFTING.getPermission()));
    }

    @Override
    protected void run(Player player, String[] args) {
        if (!Settings.USE_CRAFTING) returnTell(Messages.FEATURE_DISABLED); // Check if the feature is enabled.

        final String craftOpenSound = Settings.CRAFTING_OPEN_SOUND.toUpperCase(); // Sets the sound when the crafting table is opened. (even if not used.)

        checkPerms(player, Perms.CRAFTING); // Check for permission again.

        if (args.length == 0) { // If no arguments, open a crafting table for the sender.
            if (Settings.USE_CRAFTING_SOUNDS) {
                player.playSound(player.getLocation(), Sound.valueOf(craftOpenSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f); // If sounds enabled play a sound, if random pitch at random.
                Utils.debugLog(Settings.DEBUG, "Played the sound " + craftOpenSound + " to player " + player.getName());
            }

            player.openWorkbench(player.getLocation(), true); // Opens the workBench.
            Utils.debugLog(Settings.DEBUG, "Opened the crafting inventory.");

            returnTell(Messages.OPENED_CRAFTING); // Sends a message to the player.
        }

        if (args.length == 1) {
            checkPerms(player, Perms.CRAFTING_OTHER); // Check if the sender of the command has permission to run it as other.
            Player target = Bukkit.getPlayer(args[0]); // Sets the target.

            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST.replace("{player}", args[0])); // Make sure the player isn't null.

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
            returnTell(Messages.TOOMANY_ARGS);
        }
        return;
    }
}
