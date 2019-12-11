package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.UniversalCommand;
import com.itsschatten.portablecrafting.Perms;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.utils.FakeContainers;
import net.minecraft.server.v1_15_R1.Containers;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutOpenWindow;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Enchant table.
 */
public class EnchanttableCommand extends UniversalCommand {

    public EnchanttableCommand() {
        super("enchanttable"); // Command

        setAliases(Arrays.asList("enchtable", "ectable", "enchantmenttable"));
        setPermission(Perms.ENCHANTTABLE.getPermission());
        setPermissionMessage(Utils.getNoPermsMessage().replace("{prefix}", Messages.PREFIX).replace("{permission}", Perms.ENCHANTTABLE.getPermission()));
    }

    @Override
    protected void run(CommandSender sender, String[] args) {
        if (!Settings.USE_ENCHANTTABLE) returnTell(Messages.FEATURE_DISABLED); // Check if feature is disabled.

        final String openEnchanttableSound = Settings.ENCHANTTABLE_OPEN_SOUND.toUpperCase(); // Set sound.

        if (!(sender instanceof Player)) {
            if (args.length == 0)
                returnTell(Messages.NOTENOUGH_ARGS);

            final Player target = Bukkit.getPlayer(args[0]);
            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST.replace("{player}", args[0]));

            if (Settings.USE_ENCHANTTABLE_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(openEnchanttableSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + openEnchanttableSound + " to " + target.getName());
            }

            openFakeEnchant(target);
            tellTarget(target, Messages.OPENED_ANVIL);
            returnTell(Messages.OPENED_ANVIL_OTHER);

            return;
        }

        final Player player = (Player) sender;

        checkPerms(player, Perms.ENCHANTTABLE); // Check perms.

        if (args.length == 0) {

            if (Settings.USE_ENCHANTTABLE_SOUNDS) {
                player.playSound(player.getLocation(), Sound.valueOf(openEnchanttableSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + openEnchanttableSound + " to " + player.getName());
            }

            openFakeEnchant(player);
            Utils.debugLog(Settings.DEBUG, "Opened the enchantment table.");
            returnTell(Messages.OPENED_ENCHANTTABLE);
        }

        if (args.length == 1) {
            checkPerms(player, Perms.ENCHANTTABLE_OTHER); // Check perms.
            Player target = Bukkit.getPlayer(args[0]); // Set target
            checkNotNull(target, Messages.PLAYER_DOSENT_EXIST.replace("{player}", args[0])); // Make sure target isn't null.

            if (Settings.USE_ENCHANTTABLE_SOUNDS) {
                target.playSound(target.getLocation(), Sound.valueOf(openEnchanttableSound), 1.0f, Settings.USE_RANDOM_SOUND_PITCH ? (float) Math.random() : 1.0f);
                Utils.debugLog(Settings.DEBUG, "Playing sound " + openEnchanttableSound + " to " + target.getName());

            }

            openFakeEnchant(target);
            Utils.debugLog(Settings.DEBUG, "Opened the enchantment table.");


            tellTarget(target, Messages.OPENED_ENCHANTTABLE);
            returnTell(Messages.OPENED_ENCHANTTABLE_OTHER.replace("{player}", target.getName()));
        }

        if (args.length > 1 && Settings.USE_TOO_MANY_ARGS) {
            returnTell(Messages.TOOMANY_ARGS);
        }
    }

    private void openFakeEnchant(final Player player) {
        EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
        int containerID = ePlayer.nextContainerCounter();
        FakeContainers.FakeEnchant fakeEnchant = new FakeContainers.FakeEnchant(containerID, player);

        ePlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerID, Containers.ENCHANTMENT, fakeEnchant.getTitle()));

        ePlayer.activeContainer = fakeEnchant;
        ePlayer.activeContainer.addSlotListener(ePlayer);
        ePlayer.activeContainer = fakeEnchant;

    }
}
