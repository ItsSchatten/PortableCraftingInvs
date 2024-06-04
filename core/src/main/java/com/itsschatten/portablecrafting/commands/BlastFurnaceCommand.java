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

public class BlastFurnaceCommand extends UniversalCommand {

    public BlastFurnaceCommand() {
        super("blastfurnace");

        setPermission(Settings.USE_PERMISSIONS ? Permissions.BLAST_FURNACE.getPermission() : "");
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Permissions.BLAST_FURNACE.getPermission()));
        setDescription("Open a virtual blast furnace for you or another player!");
        setAliases(Collections.singletonList("blastfurn"));
    }

    @Override
    protected void run(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof final Player player)) {
            checkArgs(1, Messages.NOT_ENOUGH_ARGS);

            openBlastFurnaceForTarget(args);
            return;
        }

        if (Settings.USE_PERMISSIONS)
            checkPerms(player, Permissions.BLAST_FURNACE);

        if (args.length == 0) {
            switch (PortableCraftingInvsPlugin.getFakeContainers().openBlastFurnace(player)) {
                case OPENED -> {
                    Utils.debugLog("Opened a virtual furnace for " + player.getName());
                    returnTell(Messages.OPENED_BLAST_FURNACE);
                }
                case REACHED_MAXIMUM -> {
                    Utils.debugLog(player.getName() + " (may have) reached the furnace maximum!");
                    returnTell(Messages.REACHED_MAXIMUM_FEATURE
                            .replace("{player}", Messages.YOU)
                            .replace("{feature}", Messages.BLAST_FURNACE)
                            .replace("{maximum}", Settings.getInstance().maximumFurnaces() + ""));
                }
                case EVENT_CANCELED ->
                        Utils.debugLog("An event listener canceled opening a furnace for " + player.getName());
            }
            return;
        }

        if (args.length == 1) {
            if (NumberUtils.isDigits(args[0])) {
                switch (PortableCraftingInvsPlugin.getFakeContainers().openBlastFurnace(player, Math.max(0, getNumber(0, "") - 1))) {
                    case OPENED -> {
                        Utils.debugLog("Opened a virtual furnace for " + player.getName());
                        returnTell(Messages.OPENED_BLAST_FURNACE);
                    }
                    case REACHED_MAXIMUM -> {
                        Utils.debugLog(player.getName() + " (may have) reached the furnace maximum!");
                        returnTell(Messages.REACHED_MAXIMUM_FEATURE
                                .replace("{player}", Messages.YOU)
                                .replace("{feature}", Messages.BLAST_FURNACE)
                                .replace("{maximum}", Settings.getInstance().maximumFurnaces() + ""));
                    }
                    case EVENT_CANCELED ->
                            Utils.debugLog("An event listener canceled opening a furnace for " + player.getName());
                }
                return;
            }
            if (Settings.USE_PERMISSIONS)
                checkPerms(player, Permissions.BLAST_FURNACE_OTHER);

            openBlastFurnaceForTarget(args);
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOO_MANY_ARGS);
        }
    }

    private void openBlastFurnaceForTarget(final String @NotNull [] args) {
        final Player target = Bukkit.getPlayer(args[0]);
        checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));
        // We can safely assume that the target is not null due to checkNotNull.
        assert target != null;

        if (args.length > 1 && NumberUtils.isDigits(args[1])) {
            switch (PortableCraftingInvsPlugin.getFakeContainers().openBlastFurnace(target, Math.max(0, getNumber(1, "") - 1))) {
                case OPENED -> {
                    Utils.debugLog("Opened a virtual furnace for " + target.getName());
                    tellTarget(target, Messages.OPENED_BLAST_FURNACE);
                    returnTell(Messages.OPENED_BLAST_FURNACE_OTHER.replace("{player}", target.getName()));
                }
                case REACHED_MAXIMUM -> {
                    Utils.debugLog(target.getName() + " (may have) reached the furnace maximum!");
                    returnTell(Messages.REACHED_MAXIMUM_FEATURE
                            .replace("{player}", target.getName())
                            .replace("{feature}", Messages.BLAST_FURNACE)
                            .replace("{maximum}", Settings.getInstance().maximumFurnaces() + ""));
                }
                case EVENT_CANCELED ->
                        Utils.debugLog("An event listener canceled opening a furnace for " + target.getName());
            }
            return;
        }

        switch (PortableCraftingInvsPlugin.getFakeContainers().openBlastFurnace(target)) {
            case OPENED -> {
                Utils.debugLog("Opened a virtual furnace for " + target.getName());
                tellTarget(target, Messages.OPENED_BLAST_FURNACE);
                returnTell(Messages.OPENED_BLAST_FURNACE_OTHER.replace("{player}", target.getName()));
            }
            case REACHED_MAXIMUM -> {
                Utils.debugLog(target.getName() + " (may have) reached the furnace maximum!");
                returnTell(Messages.REACHED_MAXIMUM_FEATURE
                        .replace("{player}", target.getName())
                        .replace("{feature}", Messages.BLAST_FURNACE)
                        .replace("{maximum}", Settings.getInstance().maximumFurnaces() + ""));
            }
            case EVENT_CANCELED ->
                    Utils.debugLog("An event listener canceled opening a furnace for " + target.getName());
        }
    }
}
