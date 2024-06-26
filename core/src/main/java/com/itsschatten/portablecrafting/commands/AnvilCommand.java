package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.UniversalCommand;
import com.itsschatten.portablecrafting.Permissions;
import com.itsschatten.portablecrafting.PortableCraftingInvsPlugin;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Anvil
 */
public class AnvilCommand extends UniversalCommand {

    public AnvilCommand() {
        super("anvil");

        setAliases(Arrays.asList("openanvil", "anv"));
        setPermission(Settings.USE_PERMISSIONS ? Permissions.ANVIL.getPermission() : "");
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Permissions.ANVIL.getPermission()));
    }

    @Override
    protected void run(CommandSender sender, String[] args) {

        if (!(sender instanceof final Player player)) {
            checkArgs(1, Messages.NOT_ENOUGH_ARGS);
            openAnvil(args);
            return;
        }

        if (Settings.USE_PERMISSIONS) checkPerms(player, Permissions.ANVIL); // Check perms again.
        if (args.length == 0) {
            if (PortableCraftingInvsPlugin.getFakeContainers().openAnvil(player)) {
                sendSoundAndAddToSet(player);
                returnTell(Messages.OPENED_ANVIL);
            }
        }

        if (args.length == 1) {
            if (Settings.USE_PERMISSIONS) checkPerms(player, Permissions.ANVIL_OTHER); // Check perms.
            openAnvil(args);
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOO_MANY_ARGS);
        }
    }

    private void openAnvil(String @NotNull [] args) {
        final Player target = Bukkit.getPlayer(args[0]); // Set target.
        checkNotNull(target, Messages.PLAYER_DOES_NOT_EXIST.replace("{player}", args[0])); // Make sure not null.
        assert target != null;

        if (PortableCraftingInvsPlugin.getFakeContainers().openAnvil(target)) {
            sendSoundAndAddToSet(target);
            tellTarget(target, Messages.OPENED_ANVIL);
            returnTell(Messages.OPENED_ANVIL_OTHER.replace("{player}", target.getName()));
        }
    }

    private void sendSoundAndAddToSet(Player target) {
        final String anvilOpenSound = Settings.ANVIL_OPEN_SOUND;

        if (Settings.USE_ANVIL_SOUNDS) {
            target.playSound(target.getLocation(), Sound.valueOf(anvilOpenSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
            Utils.debugLog("Playing sound " + anvilOpenSound + " to " + target.getName());
        }
        Utils.debugLog("Opened the anvil for " + target.getName());
    }


}
