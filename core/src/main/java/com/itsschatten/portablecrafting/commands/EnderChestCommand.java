package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.UniversalCommand;
import com.itsschatten.portablecrafting.Permissions;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.events.EnderchestOpenEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Ender chest.
 */
public class EnderChestCommand extends UniversalCommand {

    @Getter
    private static final Set<UUID> players = new HashSet<>();

    @Getter
    @Setter
    static EnderChestCommand instance = null;

    public EnderChestCommand() {
        super("enderchest");

        setInstance(this);

        setAliases(Arrays.asList("ec", "echest"));
        setPermission(Settings.USE_PERMISSIONS ? Permissions.ENDERCHEST.getPermission() : "");
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Permissions.ENDERCHEST.getPermission()));
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        final String sound = Settings.ENDER_CHEST_OPEN_SOUND.toUpperCase(); // Set the sound.

        if (!(sender instanceof Player)) {

            checkArgsStrict(1, Messages.NOT_ENOUGH_ARGS);

            final Player target = Bukkit.getPlayer(args[0]);

            final EnderchestOpenEvent event = new EnderchestOpenEvent(target);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCanceled()) {
                if (Settings.USE_ENDERCHEST_SOUNDS) {
                    target.playSound(target.getLocation(), Sound.valueOf(sound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                    Utils.debugLog( "Playing sound " + sound + " to " + target.getName());
                }

                if (Settings.USE_ENDERCHEST_RESTRICTION) {
                    players.add(target.getUniqueId());
                }

                target.openInventory(target.getEnderChest());
                Utils.debugLog( "Opening " + target.getName() + "'s enderchest for themselves.");

                tellTarget(target, Messages.OPENED_ENDERCHEST);
            }

            return;
        }

        final Player player = (Player) sender;

        if (Settings.USE_PERMISSIONS) checkPerms(player, Permissions.ENDERCHEST); // Perms.

        if (args.length == 0) {
            Inventory eChest = player.getEnderChest(); // Get the players enderchest inventory.
            final EnderchestOpenEvent event = new EnderchestOpenEvent(player);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCanceled()) {
                player.openInventory(eChest); // Open it.
                Utils.debugLog( "Opened inventory");

                if (Settings.USE_ENDERCHEST_SOUNDS) {
                    player.playSound(player.getLocation(), Sound.valueOf(sound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                    Utils.debugLog( "Playing sound " + sound + " to " + player.getName());

                }

                if (Settings.USE_ENDERCHEST_RESTRICTION) {
                    players.add(player.getUniqueId());
                }

                returnTell(Messages.OPENED_ENDERCHEST);
            }
        }

        if (args.length == 1) {
            if (Settings.USE_PERMISSIONS) checkPerms(player, Permissions.ENDERCHEST_OTHER);

            Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0]));

            Inventory targetEChest = target.getEnderChest();

            if (Settings.USE_OLD_ENDERCHEST) {
                // Open the enderchest for the sender of the command.

                openEnderchestForPlayer(sound, player, target, targetEChest, player.getName());

                returnTell(Messages.OPEN_TARGET_ENDERCHEST_OLD.replace("{player}", target.getName()).replace("{playerFormatted}",
                        target.getName().endsWith("s") ? target.getName() + "'" : target.getName() + "'s"));
            }

            // Open the target's enderchest for themselves.
            final EnderchestOpenEvent event = new EnderchestOpenEvent(target);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCanceled()) {
                openEnderchestForPlayer(sound, target, target, targetEChest, player.getName());

                returnTell(Messages.OPEN_TARGET_ENDERCHEST.replace("{player}", target.getName()).replace("{playerFormatted}",
                        target.getName().endsWith("s") ? target.getName() + "'" : target.getName() + "'s"));
            }
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS)
            returnTell(Messages.TOO_MANY_ARGS);

    }

    private void openEnderchestForPlayer(String sound, Player player, Player target, Inventory targetEChest, String name) {
        if (Settings.USE_ENDERCHEST_RESTRICTION) {
            players.add(target.getUniqueId());
        }
        player.openInventory(targetEChest);
        Utils.debugLog( "Opening " + target.getName() + " enderchest for " + player.getName());

        if (Settings.USE_ENDERCHEST_SOUNDS) {
            player.playSound(player.getLocation(), Sound.valueOf(sound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
            Utils.debugLog( "Playing sound " + sound + " to " + name);
        }
    }
}
