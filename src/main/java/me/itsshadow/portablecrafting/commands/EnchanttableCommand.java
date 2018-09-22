package me.itsshadow.portablecrafting.commands;

import me.itsshadow.libs.commandutils.PlayerCommand;
import me.itsshadow.portablecrafting.configs.Messages;
import me.itsshadow.portablecrafting.configs.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class EnchanttableCommand extends PlayerCommand {

    public EnchanttableCommand () {
        super("enchanttable");

        setAliases(Arrays.asList("enchtable", "ectable"));
    }

    @Override
    protected void run(Player player, String[] args) {
        checkPerms(player, Messages.NO_PERMS, "pci.enchanttable");

        if (args.length == 0) {
            player.openEnchanting(player.getLocation(), true);
            returnTell(Messages.OPENED_ENCHANTTABLE, false);
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST.replace("{player}", args[0]), false);

            target.openEnchanting(player.getLocation(), true);
            tellTarget(target, Messages.OPENED_ENCHANTTABLE, false);
            returnTell(Messages.OPENED_ENCHANTTABLE_OTHER.replace("{player}", target.getName()), false);
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOOMANY_ARGS, false);
        }
    }
}
