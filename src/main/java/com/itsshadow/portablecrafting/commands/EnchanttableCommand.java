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
 * Enchant table.
 */
public class EnchanttableCommand extends PlayerCommand {

    public EnchanttableCommand() {
        super("enchanttable"); // Command

        setAliases(Arrays.asList("enchtable", "ectable", "enchantmenttable"));
        setPermission(Perms.ENCHANTTABLE.getPermission());
        setPermissionMessage(Perms.ENCHANTTABLE.getNoPermission().replace("{prefix}", Messages.PREFIX).replace("{permission}", Perms.ENCHANTTABLE.getPermission()));
    }

    @Override
    protected void run(Player player, String[] args) {
        if (!Settings.USE_ENCHANTTABLE) returnTell(Messages.FEATURE_DISABLED); // Check if feature is disabled.

        final String openEnchanttableSound = Settings.ENCHANTTABLE_OPEN_SOUND.toUpperCase(); // Set sound.

        checkPerms(player, Perms.ENCHANTTABLE); // Check perms.

        if (args.length == 0) {

            if (Settings.USE_ENCHANTTABLE_SOUNDS) {
                player.playSound(player.getLocation(), Sound.valueOf(openEnchanttableSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + openEnchanttableSound + " to " + player.getName());
            }

            player.openEnchanting(player.getLocation(), true);
            Utils.debugLog(Settings.DEBUG, "Opened the enchantment table.");
            returnTell(Messages.OPENED_ENCHANTTABLE);
        }

        if (args.length == 1) {
            checkPerms(player, Perms.ENCHANTTABLE_OTHER); // Check perms.
            Player target = Bukkit.getPlayer(args[0]); // Set target
            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST.replace("{player}", args[0])); // Make sure target isn't null.

            if (Settings.USE_ENCHANTTABLE_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(openEnchanttableSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + openEnchanttableSound + " to " + target.getName());

            }

            target.openEnchanting(player.getLocation(), true); // open
            Utils.debugLog(Settings.DEBUG, "Opened the enchantment table.");


            tellTarget(target, Messages.OPENED_ENCHANTTABLE);
            returnTell(Messages.OPENED_ENCHANTTABLE_OTHER.replace("{player}", target.getName()));
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOOMANY_ARGS);
        }
    }
}
