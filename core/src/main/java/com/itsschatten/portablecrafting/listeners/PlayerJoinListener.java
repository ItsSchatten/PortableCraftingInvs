package com.itsschatten.portablecrafting.listeners;

import com.itsschatten.libs.UpdateNotifications;
import com.itsschatten.libs.Utils;
import com.itsschatten.libs.configutils.PlayerConfigManager;
import com.itsschatten.portablecrafting.CheckForUpdate;
import com.itsschatten.portablecrafting.Permissions;
import com.itsschatten.portablecrafting.configs.Settings;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
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
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer(); // We set the player so we can use them later.
        if (!Settings.USE_MYSQL) {
            if (!PlayerConfigManager.getConfig(player.getUniqueId()).exists()) {
                FileConfiguration playerConfig = PlayerConfigManager.getConfig(player.getUniqueId()).getConfig();
                playerConfig.set("furnaces", null);
                PlayerConfigManager.getConfig(player.getUniqueId()).saveConfig();
                Utils.debugLog(Settings.DEBUG, "Saved default player file.");
            }
        }

        if (player.hasPermission(Permissions.UPDATE_NOTIFICATIONS.getPermission()) && Settings.USE_UPDATER && (UpdateNotifications.isUpdateAvailable() || CheckForUpdate.isUpdateAvailable())) {
            // Check if an update is available, if the updater is used, and if the player has permission to see an update.
            PluginDescriptionFile pdf = Utils.getInstance().getDescription(); // So we can get the version.

            Utils.debugLog(Settings.DEBUG, "Found an update for the plugin, sending the message to the player.");

            Utils.tell(player, StringUtils.replaceEach(UpdateNotifications.getUpdateMessage(), new String[]{"{currentVer}", "{newVer}", "{link}"},
                    new String[]{pdf.getVersion(), UpdateNotifications.getLatestVersion(), "https://spigotmc.org/resources/" + UpdateNotifications.getProjectId()}));
        }
    }


}
