package me.itsshadow.portablecrafting.commands;

import me.itsshadow.libs.commandutils.PlayerCommand;
import me.itsshadow.portablecrafting.configs.Messages;
import me.itsshadow.portablecrafting.configs.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
        if (!Settings.USE_ANVIL) returnTell(Messages.FEATURE_DISABLED);

        final String anvilOpenSound = Settings.ANVIL_OPEN_SOUND;

        checkPerms(player, Messages.NO_PERMS, "pci.anvil");

        if (args.length == 0) {
            Inventory anvil = Bukkit.getServer().createInventory(null, InventoryType.ANVIL);
            player.openInventory(anvil);

            if (Settings.USE_ANVIL_SOUNDS)
                player.playSound(player.getLocation(), Sound.valueOf(anvilOpenSound), 1.0f, 1.0f);

            returnTell(Messages.OPENED_ANVIL);
        }

        if (args.length == 1) {
            checkPerms(player, Messages.NO_PERMS, "pci.anvil.other");
            Inventory anvilTarget = Bukkit.getServer().createInventory(null, InventoryType.ANVIL);

            Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST);

            if (Settings.USE_ANVIL_SOUNDS)
                target.playSound(target.getLocation(), Sound.valueOf(anvilOpenSound), 1.0f, 1.0f);

            target.openInventory(anvilTarget);

            tellTarget(target, Messages.OPENED_ANVIL);
            returnTell(Messages.OPENED_ANVIL_OTHER.replace("{player}", target.getName()));
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOOMANY_ARGS);
        }
    }
}
