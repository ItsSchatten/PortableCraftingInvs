package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.UniversalCommand;
import com.itsschatten.portablecrafting.Perms;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.utils.FakeContainers;
import net.minecraft.server.v1_14_R1.ChatMessage;
import net.minecraft.server.v1_14_R1.Containers;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.PacketPlayOutOpenWindow;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;

/**
 * Cartography Table
 */
public class CartographyCommand extends UniversalCommand {

    public CartographyCommand() {
        super("cartography");

        setAliases(Collections.singletonList("cartographytable"));
        setPermission(Perms.CARTOGRAPHY.getPermission());
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Perms.CARTOGRAPHY.getPermission()));

    }

    @Override
    protected void run(CommandSender commandSender, String[] args) {
        if (!Settings.USE_CARTOGRAPHY) returnTell(Messages.FEATURE_DISABLED);

        final String cartographyTableOpen = Settings.CARTOGRAPHY_OPEN_SOUND.toUpperCase();

        if (!(commandSender instanceof Player)) {
            checkArgs(1, Messages.NOTENOUGH_ARGS);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST.replace("{player}", args[0]));

            if (Settings.USE_CARTOGRAPHY_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(cartographyTableOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + cartographyTableOpen + " to " + target.getName());
            }

            openCartography(target);
            Utils.debugLog(Settings.DEBUG, "Opened the cartography table for " + target.getName());
            tellTarget(target, Messages.OPENED_CARTOGRAPHY);
            returnTell(Messages.OPENED_CARTOGRAPHY_OTHER.replace("{player}", target.getName()));
        }

        final Player player = (Player) commandSender;
        checkPerms(player, Perms.CARTOGRAPHY);

        if (args.length == 0) {
            if (Settings.USE_CARTOGRAPHY_SOUNDS) {
                player.playSound(player.getLocation(), Sound.valueOf(cartographyTableOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + cartographyTableOpen + " to " + player.getName());
            }

            openCartography(player);
            Utils.debugLog(Settings.DEBUG, "Opened the cartography table for " + player.getName());

            returnTell(Messages.OPENED_CARTOGRAPHY);

        }

        if (args.length == 1) {
            checkPerms(player, Perms.CARTOGRAPHY_OTHER);

            Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST.replace("{player}", args[0]));

            if (Settings.USE_CARTOGRAPHY_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(cartographyTableOpen), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + cartographyTableOpen + " to " + target.getName());
            }

            openCartography(player);

            tellTarget(target, Messages.OPENED_CARTOGRAPHY);
            returnTell(Messages.OPENED_CARTOGRAPHY_OTHER.replace("{player}", target.getName()));
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOOMANY_ARGS);
        }

    }

    private void openCartography(final Player player) {
        try {
            EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
            int containerID = ePlayer.nextContainerCounter();
            FakeContainers.FakeCartography fakeCartography = new FakeContainers.FakeCartography(containerID, player);

            ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.CARTOGRAPHY, fakeCartography.getTitle()));

            ePlayer.activeContainer = fakeCartography;
            ePlayer.activeContainer.addSlotListener(ePlayer);
            ePlayer.activeContainer = fakeCartography;
        } catch (UnsupportedOperationException ex) {
            // Logging this error normally spams console
            Utils.log("An error occurred while running the anvil command, make sure you have debug enabled to see this message.");
            Utils.debugLog(Settings.DEBUG, ex.getMessage());
            player.sendMessage("An error occurred, please contact an administrator.");

        }
    }

}
