package me.itsshadow.portablecrafting.commands;

import me.itsshadow.libs.commandutils.PlayerCommand;
import me.itsshadow.portablecrafting.configs.Messages;
import me.itsshadow.portablecrafting.configs.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CraftCommand extends PlayerCommand {

    public CraftCommand() {
        super("craft");

        setAliases(Arrays.asList("crafting", "craftingtable", "workbench"));
    }

    @Override
    protected void run(Player player, String[] args) {
        if (!Settings.USE_CRAFTING) returnTell(Messages.FEATURE_DISABLED);

        final String craftOpenSound = Settings.CRAFTING_OPEN_SOUND;

        checkPerms(player, Messages.NO_PERMS, "pci.craft");

        if (args.length == 0) {
            if (Settings.USE_CRAFTING_SOUNDS)
                player.playSound(player.getLocation(), Sound.valueOf(craftOpenSound), 1.0f, 1.0f);

            player.openWorkbench(player.getLocation(), true);

            returnTell(Messages.OPENED_CRAFTING);
        }

        if (args.length == 1) {
            checkPerms(player, Messages.NO_PERMS, "pci.craft.other");
            Player target = Bukkit.getPlayer(args[0]);

            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST.replace("{player}", args[0]));

            if (Settings.USE_CRAFTING_SOUNDS)
                target.playSound(target.getLocation(), Sound.valueOf(craftOpenSound), 1.0f, 1.0f);

            target.openWorkbench(target.getLocation(), true);
            tellTarget(target, Messages.OPENED_CRAFTING);
            returnTell(Messages.OPENED_CRAFTING_OTHER.replace("{player}", target.getName()));
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOOMANY_ARGS);
        }
    }
}
