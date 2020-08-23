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

public class FurnaceCommand extends UniversalCommand {

    public FurnaceCommand() {
        super("furnace");

        setPermission(Settings.USE_PERMISSIONS ? Permissions.FURNACE.getPermission() : "");
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Permissions.FURNACE.getPermission()));
        setDescription("Open a virtual furnace for you or another player!");
        setAliases(Collections.singletonList("furn"));
    }

    @Override
    protected void run(CommandSender commandSender, String[] args) {
        if (!Settings.USE_FURNACES) returnTell(Messages.FEATURE_DISABLED);

        if (!(commandSender instanceof Player)) {
            checkArgs(1, Messages.NOT_ENOUGH_ARGS);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

            PortableCraftingInvsPlugin.getFakeContainers().openFurnace(target);
            Utils.debugLog(Settings.DEBUG, "Opened a virtual furnace for " + target.getName());
            tellTarget(target, Messages.OPENED_FURNACE);
            returnTell(Messages.OPENED_FURNACE_OTHER.replace("{player}", target.getName()));
            return;
        }

        final Player player = (Player) commandSender;
        if (Settings.USE_PERMISSIONS)
            checkPerms(player, Permissions.FURNACE);

        if (args.length == 0) {
            PortableCraftingInvsPlugin.getFakeContainers().openFurnace(player);
            Utils.debugLog(Settings.DEBUG, "Opened a virtual furnace for " + player.getName());
            returnTell( Messages.OPENED_FURNACE);
        }

        if (args.length == 1) {
            if (Settings.USE_PERMISSIONS)
                checkPerms(player, Permissions.FURNACE_OTHER);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

            PortableCraftingInvsPlugin.getFakeContainers().openFurnace(target);
            Utils.debugLog(Settings.DEBUG, "Opened a virtual furnace for " + target.getName());
            tellTarget(target, Messages.OPENED_FURNACE);
            returnTell(Messages.OPENED_FURNACE_OTHER.replace("{player}", target.getName()));
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOO_MANY_ARGS);
        }
    }
}
