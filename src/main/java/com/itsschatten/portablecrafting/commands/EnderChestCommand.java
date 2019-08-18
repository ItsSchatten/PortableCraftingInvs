package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.UniversalCommand;
import com.itsschatten.portablecrafting.Perms;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

/**
 * Ender chest.
 */
public class EnderChestCommand extends UniversalCommand {

    public EnderChestCommand() {
        super("enderchest");

        setAliases(Arrays.asList("ec", "echest"));
        setPermission(Perms.ENDERCHEST.getPermission());
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Perms.ENDERCHEST.getPermission()));
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        if (!Settings.USE_ENDERCHEST) returnTell(Messages.FEATURE_DISABLED); // Check if feature is enabled.

        final String sound = Settings.ENDER_CHEST_OPEN_SOUND.toUpperCase(); // Set the sound.

        if (!(sender instanceof Player)) {

            checkArgsStrict(1, Messages.NOTENOUGH_ARGS);

            final Player target = Bukkit.getPlayer(args[0]);

            if (Settings.USE_ENDERCHEST_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(sound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + sound + " to " + target.getName());
            }

            target.openInventory(target.getEnderChest());
            Utils.debugLog(Settings.DEBUG, "Opening " + target.getName() + "'s enderchest for themselves.");

            tellTarget(target, Messages.OPENED_ENDERCHEST);

            return;
        }

        final Player player = (Player) sender;

        checkPerms(player, Perms.ENDERCHEST); // Perms.


        if (args.length == 0) {
            Inventory eChest = player.getEnderChest(); // Get the players enderchest inventory.

            player.openInventory(eChest); // Open it.
            Utils.debugLog(Settings.DEBUG, "Opened inventory");

            if (Settings.USE_ENDERCHEST_SOUNDS) {
                player.playSound(player.getLocation(), Sound.valueOf(sound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + sound + " to " + player.getName());

            }

            returnTell(Messages.OPENED_ENDERCHEST);
        }

        if (args.length == 1) {
            checkPerms(player, Perms.ENDERCHEST_OTHER);

            Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST.replace("{player}", args[0]));

            Inventory targetEChest = target.getEnderChest();

            player.openInventory(targetEChest);
            Utils.debugLog(Settings.DEBUG, "Opening " + target.getName() + " enderchest for " + player.getName());

            if (Settings.USE_ENDERCHEST_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(sound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + sound + " to " + player.getName());
            }

            returnTell(Messages.OPEN_TARGET_ECHEST.replace("{player}", target.getName()).replace("{playerFormatted}",
                    target.getName().endsWith("s") ? target.getName() + "'" : target.getName() + "'s"));
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS)
            returnTell(Messages.TOOMANY_ARGS);

    }
}
