package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.UniversalCommand;
import com.itsschatten.portablecrafting.Permissions;
import com.itsschatten.portablecrafting.PortableCraftingInvsPlugin;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Enchant table.
 */
public class EnchanttableCommand extends UniversalCommand {

    @Getter
    private static final Map<UUID, Integer> openEnchantTables = new HashMap<>();


    public EnchanttableCommand() {
        super("enchanttable"); // Command

        setAliases(Arrays.asList("enchtable", "ectable", "enchantmenttable"));
        setPermission(Settings.USE_PERMISSIONS ? Permissions.ENCHANT_TABLE.getPermission() : "");
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Permissions.ENCHANT_TABLE.getPermission()));
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        if (!Settings.USE_ENCHANT_TABLE) returnTell(Messages.FEATURE_DISABLED); // Check if feature is disabled.

        final String openEnchanttableSound = Settings.ENCHANT_TABLE_OPEN_SOUND.toUpperCase(); // Set sound.

        if (!(sender instanceof Player)) {
            if (args.length == 0)
                returnTell(Messages.NOT_ENOUGH_ARGS);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

            if (Settings.USE_ENCHANT_TABLE_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(openEnchanttableSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + openEnchanttableSound + " to " + target.getName());
            }

            PortableCraftingInvsPlugin.getFakeContainers().openEnchant(target);
            tellTarget(target, Messages.OPENED_ANVIL);
            returnTell(Messages.OPENED_ANVIL_OTHER);

            return;
        }

        final Player player = (Player) sender;

        if (Settings.USE_PERMISSIONS) checkPerms(player, Permissions.ENCHANT_TABLE); // Check perms.

        if (args.length == 0) {

            if (Settings.USE_ENCHANT_TABLE_SOUNDS) {
                player.playSound(player.getLocation(), Sound.valueOf(openEnchanttableSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + openEnchanttableSound + " to " + player.getName());
            }

            PortableCraftingInvsPlugin.getFakeContainers().openEnchant(player);
            Utils.debugLog(Settings.DEBUG, "Opened the enchantment table.");
            returnTell(Messages.OPENED_ENCHANT_TABLE);
        }

        if (args.length == 1) {

            if (NumberUtils.isNumber(args[0])) {
                if (Settings.USE_PERMISSIONS) checkPerms(player, Permissions.ENCHANT_USE_MAX_LEVEL);
                if (Settings.USE_ENCHANT_TABLE_SOUNDS) {
                    player.playSound(player.getLocation(), Sound.valueOf(openEnchanttableSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                    Utils.debugLog(Settings.DEBUG, "Playing sound " + openEnchanttableSound + " to " + player.getName());
                }

                int maxLevel = getNumber(0, 1, Settings.ENCHANT_MAX_LEVEL, StringUtils.replaceEach(Messages.MUST_BE_IN_RANGE, new String[]{"{max}", "{min}"}, new String[]{Settings.ENCHANT_MAX_LEVEL + "", "1"}));

                PortableCraftingInvsPlugin.getFakeContainers().openEnchant(player, maxLevel);
                openEnchantTables.put(player.getUniqueId(), maxLevel);
                Utils.debugLog(Settings.DEBUG, "Opened the enchantment table with max level " + maxLevel);


                returnTell(StringUtils.replace(Messages.OPENED_ENCHANT_WITH_MAX_LEVEL, "{level}", maxLevel + ""));
                return;
            }

            if (Settings.USE_PERMISSIONS) checkPerms(player, Permissions.ENCHANT_TABLE_OTHER); // Check perms.
            Player target = Bukkit.getPlayer(args[0]); // Set target
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0])); // Make sure target isn't null.
            if (Settings.USE_ENCHANT_TABLE_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(openEnchanttableSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + openEnchanttableSound + " to " + target.getName());

            }

            PortableCraftingInvsPlugin.getFakeContainers().openEnchant(target);
            Utils.debugLog(Settings.DEBUG, "Opened the enchantment table.");

            tellTarget(target, Messages.OPENED_ENCHANT_TABLE);
            returnTell(Messages.OPENED_ENCHANT_TABLE_OTHER.replace("{player}", target.getName()));
        }


        if (args.length > 2 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOO_MANY_ARGS);
        }
    }

}
