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

public class SmithingCommand extends UniversalCommand {

    public SmithingCommand() {
        super("smithing");

        setPermission(Settings.USE_PERMISSIONS ? Permissions.SMITHING_TABLE.getPermission() : "");
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Permissions.SMITHING_TABLE.getPermission()));
        setAliases(Arrays.asList("smithing-table", "smithingtable"));
    }

    @Override
    protected void run(CommandSender commandSender, String[] args) {
        if (PortableCraftingInvsPlugin.getServerVersion().equals("v1_15_R1"))
            returnTell(Messages.CANT_USE_SMITHING_IN_1_15);
        if (!Settings.USE_SMITHING_TABLE) returnTell(Messages.FEATURE_DISABLED);

        final String smithingTableOpen = Settings.SMITHING_TABLE_OPEN_SOUND;

        if (!(commandSender instanceof Player)) {
            checkArgs(1, Messages.NOT_ENOUGH_ARGS);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[1]));

            if (PortableCraftingInvsPlugin.getFakeContainers().openSmithing(target)) {
                if (Settings.USE_SMITHING_TABLE_SOUNDS) {
                    target.playSound(target.getLocation(), Sound.valueOf(smithingTableOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                    Utils.debugLog(Settings.DEBUG, "Playing sound " + smithingTableOpen + " to " + target.getName());
                }


                Utils.debugLog(Settings.DEBUG, "Opened the smithing table for " + target.getName());
                tellTarget(target, Messages.OPENED_SMITHING_TABLE);
                returnTell(Messages.OPENED_SMITHING_TABLE_OTHER.replace("{player}", target.getName()));
            }
            return;
        }

        final Player player = (Player) commandSender;
        if (Settings.USE_PERMISSIONS)
            checkPerms(player, Permissions.SMITHING_TABLE);

        if (args.length == 0) {
            if (Settings.USE_SMITHING_TABLE_SOUNDS) {
                player.playSound(player.getLocation(), Sound.valueOf(smithingTableOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + smithingTableOpen + " to " + player.getName());
            }

            PortableCraftingInvsPlugin.getFakeContainers().openSmithing(player);
            Utils.debugLog(Settings.DEBUG, "Opened the smithing table for " + player.getName());
            returnTell(Messages.OPENED_SMITHING_TABLE);
        }

        if (args.length == 1) {
            if (Settings.USE_PERMISSIONS)
                checkPerms(player, Permissions.SMITHING_TABLE_OTHER);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

            if (PortableCraftingInvsPlugin.getFakeContainers().openSmithing(target)) {
                if (Settings.USE_SMITHING_TABLE_SOUNDS) {
                    target.playSound(target.getLocation(), Sound.valueOf(smithingTableOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                    Utils.debugLog(Settings.DEBUG, "Playing sound " + smithingTableOpen + " to " + target.getName());
                }


                Utils.debugLog(Settings.DEBUG, "Opened the smithing table for " + target.getName());
                tellTarget(target, Messages.OPENED_SMITHING_TABLE);
                returnTell(Messages.OPENED_SMITHING_TABLE_OTHER.replace("{player}", target.getName()));
            }
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOO_MANY_ARGS);
        }

    }
}
