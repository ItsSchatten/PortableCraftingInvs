package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.UniversalCommand;
import com.itsschatten.portablecrafting.Permissions;
import com.itsschatten.portablecrafting.PortableCraftingInvsPlugin;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
        if (!(commandSender instanceof final Player player)) {
            checkArgs(1, Messages.NOT_ENOUGH_ARGS);

            openForTarget(args);
            return;
        }

        if (Settings.USE_PERMISSIONS)
            checkPerms(player, Permissions.FURNACE);

        if (args.length == 0) {
            switch (PortableCraftingInvsPlugin.getFakeContainers().openFurnace(player)) {
                case OPENED -> {
                    Utils.debugLog("Opened a virtual furnace for " + player.getName());
                    returnTell(Messages.OPENED_FURNACE);
                }
                case REACHED_MAXIMUM -> {
                    Utils.debugLog(player.getName() + " (may have) reached the furnace maximum!");
                    returnTell(Messages.REACHED_MAXIMUM_FEATURE
                            .replace("{player}", Messages.YOU)
                            .replace("{feature}", Messages.FURNACE)
                            .replace("{maximum}", Settings.getInstance().maximumFurnaces() + ""));
                }
                case EVENT_CANCELED ->
                        Utils.debugLog("An event listener canceled opening a furnace for " + player.getName());
            }
            return;
        }

        if (args.length == 1) {
            if (NumberUtils.isDigits(args[0])) {
                switch (PortableCraftingInvsPlugin.getFakeContainers().openFurnace(player, Math.max(0, getNumber(0, "") - 1))) {
                    case OPENED -> {
                        Utils.debugLog("Opened a virtual furnace for " + player.getName());
                        returnTell(Messages.OPENED_FURNACE);
                    }
                    case REACHED_MAXIMUM -> {
                        Utils.debugLog(player.getName() + " (may have) reached the furnace maximum!");
                        returnTell(Messages.REACHED_MAXIMUM_FEATURE
                                .replace("{player}", Messages.YOU)
                                .replace("{feature}", Messages.FURNACE)
                                .replace("{maximum}", Settings.getInstance().maximumFurnaces() + ""));
                    }
                    case EVENT_CANCELED ->
                            Utils.debugLog("An event listener canceled opening a furnace for " + player.getName());
                }
                return;
            }

            if (Settings.USE_PERMISSIONS)
                checkPerms(player, Permissions.FURNACE_OTHER);

            openForTarget(args);
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOO_MANY_ARGS);
        }
    }

    private void openForTarget(String @NotNull [] args) {
        final Player target = Bukkit.getPlayer(args[0]);
        checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));
        // We can safely assume that the target is not null due to checkNotNull.
        assert target != null;

        if (args.length > 1 && NumberUtils.isDigits(args[1])) {
            switch (PortableCraftingInvsPlugin.getFakeContainers().openFurnace(target, Math.max(0, getNumber(1, "") - 1))) {
                case OPENED -> {
                    Utils.debugLog("Opened a virtual furnace for " + target.getName());
                    tellTarget(target, Messages.OPENED_FURNACE);
                    returnTell(Messages.OPENED_FURNACE_OTHER.replace("{player}", target.getName()));
                }
                case REACHED_MAXIMUM -> {
                    Utils.debugLog(target.getName() + " (may have) reached the furnace maximum!");
                    returnTell(Messages.REACHED_MAXIMUM_FEATURE
                            .replace("{player}", target.getName())
                            .replace("{feature}", Messages.FURNACE)
                            .replace("{maximum}", Settings.getInstance().maximumFurnaces() + ""));
                }
                case EVENT_CANCELED ->
                        Utils.debugLog("An event listener canceled opening a furnace for " + target.getName());
            }
            return;
        }

        switch (PortableCraftingInvsPlugin.getFakeContainers().openFurnace(target)) {
            case OPENED -> {
                Utils.debugLog("Opened a virtual furnace for " + target.getName());
                tellTarget(target, Messages.OPENED_FURNACE);
                returnTell(Messages.OPENED_FURNACE_OTHER.replace("{player}", target.getName()));
            }
            case REACHED_MAXIMUM -> {
                Utils.debugLog(target.getName() + " (may have) reached the furnace maximum!");
                returnTell(Messages.REACHED_MAXIMUM_FEATURE
                        .replace("{player}", target.getName())
                        .replace("{feature}", Messages.FURNACE)
                        .replace("{maximum}", Settings.getInstance().maximumFurnaces() + ""));
            }
            case EVENT_CANCELED ->
                    Utils.debugLog("An event listener canceled opening a furnace for " + target.getName());
        }
    }
}
