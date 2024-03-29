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
        if (!Settings.USE_BLAST_FURNACE) returnTell(Messages.FEATURE_DISABLED);

        if (!(commandSender instanceof Player)) {
            checkArgs(1, Messages.NOT_ENOUGH_ARGS);

            openBlastFurnaceForTarget(args);
            return;
        }

        final Player player = (Player) commandSender;
        if (Settings.USE_PERMISSIONS)
            checkPerms(player, Permissions.BLAST_FURNACE);

        if (args.length == 0) {
            if (PortableCraftingInvsPlugin.getFakeContainers().openBlastFurnace(player)) {
                Utils.debugLog( "Opened a virtual furnace for " + player.getName());
                returnTell(Messages.OPENED_BLAST_FURNACE);
            }
        }

        if (args.length == 1) {
            if (Settings.USE_PERMISSIONS)
                checkPerms(player, Permissions.BLAST_FURNACE_OTHER);

            openBlastFurnaceForTarget(args);
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOO_MANY_ARGS);
        }
    }

    private void openBlastFurnaceForTarget(final String[] args) {
        final Player target = Bukkit.getPlayer(args[0]);
        checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

        if (PortableCraftingInvsPlugin.getFakeContainers().openBlastFurnace(target)) {
            Utils.debugLog( "Opened a virtual furnace for " + target.getName());
            tellTarget(target, Messages.OPENED_BLAST_FURNACE);
            returnTell(Messages.OPENED_BLAST_FURNACE_OTHER.replace("{player}", target.getName()));
        }
    }
}
