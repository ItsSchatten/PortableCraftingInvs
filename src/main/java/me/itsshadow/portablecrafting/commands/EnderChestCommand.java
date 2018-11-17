package me.itsshadow.portablecrafting.commands;

import me.itsshadow.libs.commandutils.PlayerCommand;
import me.itsshadow.portablecrafting.configs.Messages;
import me.itsshadow.portablecrafting.configs.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class EnderChestCommand extends PlayerCommand {

    public EnderChestCommand() {
        super("enderchest");

        setAliases(Arrays.asList("ec", "echest"));
    }

    @Override
    protected void run(Player player, String[] args) {
        if (!Settings.USE_ENDERCHEST) returnTell(Messages.FEATURE_DISABLED);

        checkPerms(player, Messages.NO_PERMS, "pci.enderchest");

        final String sound = Settings.ENDER_CHEST_OPEN_SOUND;

        if (args.length == 0) {
            Inventory eChest = player.getEnderChest();

            player.openInventory(eChest);

            if (Settings.USE_ENDERCHEST_SOUNDS) {
                player.playSound(player.getLocation(), Sound.valueOf(sound), 1.0f, 1.0f);
            }

            returnTell(Messages.OPENED_ENDERCHEST);
        }

        if (args.length == 1) {
            checkPerms(player, Messages.NO_PERMS, "pci.enderchest.other");
            Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST.replace("{player}", args[0]));

            Inventory targetEChest = target.getEnderChest();

            player.openInventory(targetEChest);

            if (Settings.USE_ENDERCHEST_SOUNDS)
                target.playSound(target.getLocation(), Sound.valueOf(sound), 1.0f, 1.0f);

            returnTell(Messages.OPEN_TARGET_ECHEST.replace("{player}", target.getName()).replace("{playerFormatted}",
                    target.getName().endsWith("s") ? target.getName() + "'" : target.getName() + "'s"));
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS)
            returnTell(Messages.TOOMANY_ARGS);

    }
}
