package me.itsshadow.portablecrafting.commands;

import me.itsshadow.libs.Utils;
import me.itsshadow.libs.commandutils.PlayerCommand;
import me.itsshadow.portablecrafting.configs.Messages;
import me.itsshadow.portablecrafting.configs.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CraftCommand extends PlayerCommand {

    public CraftCommand() {
        super("craft");

        setAliases(Arrays.asList("crafting", "craftingtable", "workbench"));
    }

    @Override
    protected void run(Player player, String[] args) {
        checkPerms(player, Messages.NO_PERMS, "pci.craft");

        if (args.length == 0) {
            player.openWorkbench(player.getLocation(), true);
            returnTell(Messages.OPENED_CRAFTING, false);
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST.replace("{player}", args[0]), false);

            target.openWorkbench(target.getLocation(), true);
            tellTarget(target, Messages.OPENED_CRAFTING, false);
            returnTell(Messages.OPENED_CRAFTING_OTHER.replace("{player}", target.getName()), false);
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOOMANY_ARGS, false);
        }
    }
}
