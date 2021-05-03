package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.UniversalCommand;
import com.itsschatten.portablecrafting.Permissions;
import com.itsschatten.portablecrafting.PortableCraftingInvsPlugin;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class BrewingStandCommand extends UniversalCommand {

    public BrewingStandCommand() {
        super("brewing");

        setAliases(Collections.singletonList("brew"));
        setPermission(Settings.USE_PERMISSIONS ? Permissions.BREWING.getPermission() : "");
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Permissions.BREWING.getPermission()));

    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        if (!Settings.USE_BREWING) returnTell(Messages.FEATURE_DISABLED);

        if (!(sender instanceof Player)) {
            checkArgs(1, Messages.NOT_ENOUGH_ARGS);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

            PortableCraftingInvsPlugin.getFakeContainers().openBrewingStand(target);
            Utils.debugLog(Settings.DEBUG, "Opened a virtual brewing stand for " + target.getName());
            tellTarget(target, Messages.OPENED_BREWING);
            returnTell(Messages.OPENED_BREWING_OTHER.replace("{player}", target.getName()));
            return;
        }

        final Player player = (Player) sender;
        if (Settings.USE_PERMISSIONS)
            checkPerms(player, Permissions.BREWING);

        if (args.length == 0) {
            PortableCraftingInvsPlugin.getFakeContainers().openBrewingStand(player);
            Utils.debugLog(Settings.DEBUG, "Opened a virtual brewing stand for " + player.getName());
            returnTell(Messages.OPENED_BREWING);
            return;
        }

        if (args.length == 1) {
            if (Settings.USE_PERMISSIONS)
                checkPerms(player, Permissions.BREWING_OTHER);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

            PortableCraftingInvsPlugin.getFakeContainers().openBrewingStand(target);
            Utils.debugLog(Settings.DEBUG, "Opened a virtual brewing stand for " + target.getName());
            tellTarget(target, Messages.OPENED_BREWING);
            returnTell(Messages.OPENED_BREWING_OTHER.replace("{player}", target.getName()));
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOO_MANY_ARGS);
        }
    }
}
