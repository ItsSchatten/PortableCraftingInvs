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

public class BrewingStandCommand extends UniversalCommand {

    public BrewingStandCommand() {
        super("brewing");

        setAliases(Collections.singletonList("brew"));
        setPermission(Settings.USE_PERMISSIONS ? Permissions.BREWING.getPermission() : "");
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Permissions.BREWING.getPermission()));
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        if (!(sender instanceof final Player player)) {
            checkArgs(1, Messages.NOT_ENOUGH_ARGS);

            openBrewingForTarget(args);
            return;
        }

        if (Settings.USE_PERMISSIONS)
            checkPerms(player, Permissions.BREWING);

        if (args.length == 0) {
            switch (PortableCraftingInvsPlugin.getFakeContainers().openBrewingStand(player)) {
                case OPENED -> {
                    Utils.debugLog("Opened a virtual brewing stand for " + player.getName());
                    returnTell(Messages.OPENED_BREWING);
                }
                case REACHED_MAXIMUM -> {
                    Utils.debugLog(player.getName() + " (may have) reached the brewing stand maximum!");
                    returnTell(Messages.REACHED_MAXIMUM_FEATURE
                            .replace("{player}", Messages.YOU)
                            .replace("{feature}", Messages.BREWING_STAND)
                            .replace("{maximum}", Settings.getInstance().maximumFurnaces() + ""));
                }
                case EVENT_CANCELED ->
                        Utils.debugLog("An event listener canceled opening a brewing stand for " + player.getName());
            }
            return;
        }

        if (args.length == 1) {
            if (NumberUtils.isDigits(args[0])) {
                switch (PortableCraftingInvsPlugin.getFakeContainers().openBrewingStand(player, Math.max(0, getNumber(0, "") - 1))) {
                    case OPENED -> {
                        Utils.debugLog("Opened a virtual brewing stand for " + player.getName());
                        returnTell(Messages.OPENED_BREWING);
                    }
                    case REACHED_MAXIMUM -> {
                        Utils.debugLog(player.getName() + " (may have) reached the brewing stand maximum!");
                        returnTell(Messages.REACHED_MAXIMUM_FEATURE
                                .replace("{player}", Messages.YOU)
                                .replace("{feature}", Messages.BREWING_STAND)
                                .replace("{maximum}", Settings.getInstance().maximumFurnaces() + ""));
                    }
                    case EVENT_CANCELED ->
                            Utils.debugLog("An event listener canceled opening a brewing stand for " + player.getName());
                }
                return;
            }

            if (Settings.USE_PERMISSIONS)
                checkPerms(player, Permissions.BREWING_OTHER);

            openBrewingForTarget(args);
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOO_MANY_ARGS);
        }
    }

    private void openBrewingForTarget(String @NotNull [] args) {
        final Player target = Bukkit.getPlayer(args[0]);
        checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));
        // We can safely assume that a target is not null to checkNotNull.
        assert target != null;

        if (args.length > 1 && NumberUtils.isDigits(args[1])) {
            switch (PortableCraftingInvsPlugin.getFakeContainers().openBrewingStand(target, Math.max(0, getNumber(1, "") - 1))) {
                case OPENED -> {
                    Utils.debugLog("Opened a virtual brewing stand for " + target.getName());
                    tellTarget(target, Messages.OPENED_BREWING);
                    returnTell(Messages.OPENED_BREWING_OTHER.replace("{player}", target.getName()));
                }
                case REACHED_MAXIMUM -> {
                    Utils.debugLog(target.getName() + " (may have) reached the brewing stand maximum!");
                    returnTell(Messages.REACHED_MAXIMUM_FEATURE
                            .replace("{player}", target.getName())
                            .replace("{feature}", Messages.BREWING_STAND)
                            .replace("{maximum}", Settings.getInstance().maximumFurnaces() + ""));
                }
                case EVENT_CANCELED ->
                        Utils.debugLog("An event listener canceled opening a brewing stand for " + target.getName());
            }
            return;
        }

        switch (PortableCraftingInvsPlugin.getFakeContainers().openBrewingStand(target)) {
            case OPENED -> {
                Utils.debugLog("Opened a virtual brewing stand for " + target.getName());
                tellTarget(target, Messages.OPENED_BREWING);
                returnTell(Messages.OPENED_BREWING_OTHER.replace("{player}", target.getName()));
            }
            case REACHED_MAXIMUM -> {
                Utils.debugLog(target.getName() + " (may have) reached the brewing stand maximum!");
                returnTell(Messages.REACHED_MAXIMUM_FEATURE
                        .replace("{player}", target.getName())
                        .replace("{feature}", Messages.BREWING_STAND)
                        .replace("{maximum}", Settings.getInstance().maximumFurnaces() + ""));
            }
            case EVENT_CANCELED ->
                    Utils.debugLog("An event listener canceled opening a brewing stand for " + target.getName());
        }
    }
}
