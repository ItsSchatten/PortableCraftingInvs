package me.itsshadow.portablecrafting.commands;

import me.itsshadow.libs.commandutils.PlayerCommand;
import me.itsshadow.portablecrafting.configs.Messages;
import me.itsshadow.portablecrafting.configs.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.Arrays;

public class EnchanttableCommand extends PlayerCommand {

    public EnchanttableCommand () {
        super("enchanttable");

        setAliases(Arrays.asList("enchtable", "ectable", "enchantmenttable"));
    }

    @Override
    protected void run(Player player, String[] args) {
        if (!Settings.USE_ENCHANTTABLE) returnTell(Messages.FEATURE_DISABLED, false);

        final String openEnchanttableSound = Settings.ENCHANTTABLE_OPEN_SOUND;

        checkPerms(player, Messages.NO_PERMS, "pci.enchanttable");

        if (args.length == 0) {

            if (Settings.USE_ENCHANTTABLE_SOUNDS)
                player.playSound(player.getLocation(), Sound.valueOf(openEnchanttableSound), 1.0f, 1.0f);

            player.openEnchanting(player.getLocation(), true);
            returnTell(Messages.OPENED_ENCHANTTABLE, false);
        }

        if (args.length == 1) {
            checkPerms(player, Messages.NO_PERMS, "pci.enchanttable.other");
            Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST.replace("{player}", args[0]), false);

            target.openEnchanting(player.getLocation(), true);

            if (Settings.USE_ENCHANTTABLE_SOUNDS)
                target.playSound(target.getLocation(), Sound.valueOf(openEnchanttableSound), 1.0f, 1.0f);

            tellTarget(target, Messages.OPENED_ENCHANTTABLE, false);
            returnTell(Messages.OPENED_ENCHANTTABLE_OTHER.replace("{player}", target.getName()), false);
        }

        if (args.length == 2) {
            Inventory inv = Bukkit.createInventory(null, InventoryType.ENCHANTING);
            InventoryView test;
        }

        if (args.length > 2 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOOMANY_ARGS, false);
        }
    }
}
