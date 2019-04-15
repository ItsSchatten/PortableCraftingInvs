package com.itsshadow.portablecrafting.events;

import com.itsschatten.libs.UpdateNotifications;
import com.itsschatten.libs.Utils;
import com.itsshadow.portablecrafting.CheckForUpdate;
import com.itsshadow.portablecrafting.Perms;
import com.itsshadow.portablecrafting.configs.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * This class handles the player if an update is available for the plugin. We send the player a message saying that we've found an update.
 */
public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer(); // We set the player so we can call them later.

        if (player.hasPermission(Perms.UPDATE_NOTIFICATIONS.getPermission()) && Settings.USE_UPDATER && UpdateNotifications.isUpdateAvailable() || CheckForUpdate.isUpdateAvailable()) {
            // Check if an update is available, if the updater is used, and if the player has permission to see an update.

            PluginDescriptionFile pdf = Utils.getInstance().getDescription(); // So we can get the version.

            Utils.debugLog(Settings.DEBUG, "Found an update for the plugin, sending the message to the player.");

            Utils.tell(player, UpdateNotifications.getUpdateMessage().replace("{currentVer}", pdf.getVersion())
                    .replace("{newVer}", UpdateNotifications.getLatestVersion())
                    .replace("{link}", "https://spigotmc.org/resources/" + UpdateNotifications.getProjectId()));
        }
    }


}
