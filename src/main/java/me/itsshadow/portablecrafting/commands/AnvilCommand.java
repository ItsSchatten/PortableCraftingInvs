package me.itsshadow.portablecrafting.commands;

import me.itsshadow.libs.commandutils.PlayerCommand;
import me.itsshadow.portablecrafting.configs.Messages;
import me.itsshadow.portablecrafting.configs.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class AnvilCommand extends PlayerCommand {

    public AnvilCommand() {
        super("anvil");

        setAliases(Arrays.asList("openanvil", "anv"));
    }

    @Override
    protected void run(Player player, String[] args) {
        checkPerms(player, Messages.NO_PERMS, "pci.anvil");

        if (args.length == 0) {
            Inventory anvil = Bukkit.getServer().createInventory(null, InventoryType.ANVIL);
            player.openInventory(anvil);
            returnTell(Messages.OPENED_ANVIL, false);
        }

        if (args.length == 1) {
            Inventory anvilTarget = Bukkit.getServer().createInventory(null, InventoryType.ANVIL);

            Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST, false);

            target.openInventory(anvilTarget);

            tellTarget(target, Messages.OPENED_ANVIL, false);
            returnTell(Messages.OPENED_ANVIL_OTHER.replace("{player}", target.getName()), false);
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOOMANY_ARGS, false);
        }
    }
}
