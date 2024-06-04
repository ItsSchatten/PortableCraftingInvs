package com.itsschatten.portablecrafting.listeners;

import com.itsschatten.libs.UpdateNotifications;
import com.itsschatten.libs.Utils;
import com.itsschatten.libs.configutils.PlayerConfigManager;
import com.itsschatten.portablecrafting.Permissions;
import com.itsschatten.portablecrafting.configs.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

/**
 * This class handles the player if an update is available for the plugin. We send the player a message saying that we've found an update.
 */
public class PlayerJoinListener implements Listener {

    final UpdateNotifications notifications;

    public PlayerJoinListener(final UpdateNotifications notifications) {
        this.notifications = notifications;
    }

    @EventHandler
    public void onJoin(final @NotNull PlayerJoinEvent event) {
        // Store this so we don't have to get the player constantly.
        // Also, it looks nicer.
        final Player player = event.getPlayer();

        // Guard clauses to ensure we have an update and the player has valid permissions.
        if (!Settings.USE_UPDATER) return;
        if (!player.hasPermission(Permissions.UPDATE_NOTIFICATIONS.getPermission())) return;

        // Check if we are using the updater, and that there is an update available.
        if (Settings.USE_UPDATER && notifications != null && notifications.isUpdateAvailable()) {
            // Get the version.
            final PluginDescriptionFile pdf = Utils.getInstance().getDescription();

            Utils.debugLog("Found an update for the plugin, sending the message to the player.");
            Utils.tell(player, notifications.getUpdateMessage().replace("{currentVer}", pdf.getVersion()).replace("{newVer}", UpdateNotifications.getLatestVersion())
                    .replace("{link}", "https://spigotmc.org/resources/" + UpdateNotifications.getProjectId()));
        }
    }

    // Remove the player config from memory.
    @EventHandler
    public void onLeave(final @NotNull PlayerQuitEvent event) {
        PlayerConfigManager.removeConfig(event.getPlayer());
    }


}
