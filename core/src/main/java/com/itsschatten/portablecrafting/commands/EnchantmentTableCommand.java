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
 * Enchant table.
 */
public class EnchantmentTableCommand extends UniversalCommand {

    public EnchantmentTableCommand() {
        super("enchanttable"); // Command

        setAliases(Arrays.asList("enchtable", "ectable", "enchantmenttable"));
        setPermission(Settings.USE_PERMISSIONS ? Permissions.ENCHANT_TABLE.getPermission() : "");
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Permissions.ENCHANT_TABLE.getPermission()));
    }


    @Override
    protected void run(CommandSender sender, String[] args) {
        final String sound = Settings.ENCHANT_TABLE_OPEN_SOUND.toUpperCase(); // Set sound.

        if (!(sender instanceof final Player player)) {
            if (args.length == 0)
                returnTell(Messages.NOT_ENOUGH_ARGS);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));


            openEnchantingForTarget(sound, target);
            return;
        }

        if (Settings.USE_PERMISSIONS) checkPerms(player, Permissions.ENCHANT_TABLE); // Check perms.

        if (args.length == 0) {
            if (PortableCraftingInvsPlugin.getFakeContainers().openEnchant(player)) {
                if (Settings.USE_ENCHANT_TABLE_SOUNDS) {
                    player.playSound(player.getLocation(), Sound.valueOf(sound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                    Utils.debugLog("Playing sound " + sound + " to " + player.getName());
                }

                Utils.debugLog("Opened the enchantment table.");
                returnTell(Messages.OPENED_ENCHANT_TABLE);
            }
        }

        if (args.length == 1) {
            try {
                if (Settings.USE_PERMISSIONS) checkPerms(player, Permissions.ENCHANT_USE_MAX_LEVEL);

                Integer.parseInt(args[0]); // Hack to throw our error.
                int maxLevel = getNumber(0, 1, Settings.ENCHANT_MAX_LEVEL, Messages.MUST_BE_IN_RANGE.replace("{max}", Settings.ENCHANT_MAX_LEVEL + "").replace("{min}", "1"));
                if (PortableCraftingInvsPlugin.getFakeContainers().openEnchant(player, maxLevel)) {
                    if (Settings.USE_ENCHANT_TABLE_SOUNDS) {
                        player.playSound(player.getLocation(), Sound.valueOf(sound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                        Utils.debugLog("Playing sound " + sound + " to " + player.getName());
                    }
                    Utils.debugLog("Opened the enchantment table with max level " + maxLevel);
                    returnTell(Messages.OPENED_ENCHANT_WITH_MAX_LEVEL.replace("{level}", maxLevel + ""));
                }
            } catch (NumberFormatException ex) {
                if (Settings.USE_PERMISSIONS) checkPerms(player, Permissions.ENCHANT_TABLE_OTHER); // Check perms.
                Player target = Bukkit.getPlayer(args[0]); // Set target
                checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0])); // Make sure target isn't null.
                openEnchantingForTarget(sound, target);
            }

        }
        if (args.length > 2 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOO_MANY_ARGS);
        }
    }

    private void openEnchantingForTarget(String sound, Player target) {
        if (PortableCraftingInvsPlugin.getFakeContainers().openEnchant(target)) {
            if (Settings.USE_ENCHANT_TABLE_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(sound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog("Playing sound " + sound + " to " + target.getName());
            }

            Utils.debugLog("Opened the enchantment table.");
            tellTarget(target, Messages.OPENED_ENCHANT_TABLE);
            returnTell(Messages.OPENED_ENCHANT_TABLE_OTHER);
        }
    }

}
