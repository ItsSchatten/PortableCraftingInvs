package com.itsshadow.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.PlayerCommand;
import com.itsshadow.portablecrafting.Perms;
import com.itsshadow.portablecrafting.configs.Messages;
import com.itsshadow.portablecrafting.configs.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

/**
 * Anvil
 */
public class AnvilCommand extends PlayerCommand {

    public AnvilCommand() {
        super("anvil");

        setAliases(Arrays.asList("openanvil", "anv"));
        setPermission(Perms.ANVIL.getPermission());
        setPermissionMessage(Perms.ANVIL.getNoPermission().replace("{prefix}", Messages.PREFIX).replace("{permission}", Perms.ANVIL.getPermission()));
    }

    @Override
    protected void run(Player player, String[] args) {
        if (!Settings.USE_ANVIL) returnTell(Messages.FEATURE_DISABLED); // Check if feature is enabled.

        final String anvilOpenSound = Settings.ANVIL_OPEN_SOUND.toUpperCase(); // Set sound.

        checkPerms(player, Perms.ANVIL); // Check perms again.

        if (args.length == 0) {
            Inventory anvil = Bukkit.getServer().createInventory(player, InventoryType.ANVIL); // Create anvil inventory.
            player.openInventory(anvil); // Open inv.
            Utils.debugLog(Settings.DEBUG, "Opened inventory.");
            if (Settings.USE_ANVIL_SOUNDS) {
                player.playSound(player.getLocation(), Sound.valueOf(anvilOpenSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + anvilOpenSound + " to " + player.getName());
            }

            returnTell(Messages.OPENED_ANVIL);
        }

        if (args.length == 1) {
            checkPerms(player, Perms.ANVIL_OTHER); // Check perms.

            Player target = Bukkit.getPlayer(args[0]); // Set target.
            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST); // Make sure not null.

            Inventory anvilTarget = Bukkit.getServer().createInventory(target, InventoryType.ANVIL); // Make inventory

            if (Settings.USE_ANVIL_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(anvilOpenSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + anvilOpenSound + " to " + target.getName());
            }

            target.openInventory(anvilTarget);
            Utils.debugLog(Settings.DEBUG, "Opened the anvil for " + target.getName());

            tellTarget(target, Messages.OPENED_ANVIL);
            returnTell(Messages.OPENED_ANVIL_OTHER.replace("{player}", target.getName()));
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOOMANY_ARGS);
        }
    }
}
