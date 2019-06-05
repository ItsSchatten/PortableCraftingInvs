package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.PlayerCommand;
import com.itsschatten.portablecrafting.Perms;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

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

            if (Settings.USE_ANVIL_SOUNDS) {
                player.playSound(player.getLocation(), Sound.valueOf(anvilOpenSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + anvilOpenSound + " to " + player.getName());
            }

            openAnvil(player);
            Utils.debugLog(Settings.DEBUG, "Opened the anvil for " + player.getName());

            returnTell(Messages.OPENED_ANVIL);
        }

        if (args.length == 1) {
            checkPerms(player, Perms.ANVIL_OTHER); // Check perms.

            Player target = Bukkit.getPlayer(args[0]); // Set target.
            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST); // Make sure not null.


            if (Settings.USE_ANVIL_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(anvilOpenSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + anvilOpenSound + " to " + target.getName());
            }

            openAnvil(target);

            Utils.debugLog(Settings.DEBUG, "Opened the anvil for " + target.getName());

            tellTarget(target, Messages.OPENED_ANVIL);
            returnTell(Messages.OPENED_ANVIL_OTHER.replace("{player}", target.getName()));
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOOMANY_ARGS);
        }
    }

    private void openAnvil(final Player player) { // Using NMS create an anvil.
        EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
        FakeAnvil fakeAnvil = new FakeAnvil(ePlayer);
        int containerID = ePlayer.nextContainerCounter();

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, "minecraft:anvil", new ChatMessage("Repairing", new Object[]{}), 0));

        ePlayer.activeContainer = fakeAnvil;
        ePlayer.activeContainer.windowId = containerID;
        ePlayer.activeContainer.addSlotListener(ePlayer);
        ePlayer.activeContainer = fakeAnvil;
        ePlayer.activeContainer.windowId = containerID;
    }

    class FakeAnvil extends ContainerAnvil {
        public FakeAnvil(EntityHuman entityhuman) {
            super(entityhuman.inventory, entityhuman.world, new BlockPosition(0, 0, 0), entityhuman);
            this.checkReachable = false; // ignore if the block is reachable, otherwise open regardless of distance.
        }

    }

}
