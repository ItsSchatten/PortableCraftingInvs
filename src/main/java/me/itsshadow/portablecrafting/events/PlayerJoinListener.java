package me.itsshadow.portablecrafting.events;

import me.itsshadow.libs.UpdateNotifications;
import me.itsshadow.libs.Utils;
import me.itsshadow.portablecrafting.configs.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("pci.update") && Settings.USE_UPDATER && UpdateNotifications.isUpdateAvailable()) {
            PluginDescriptionFile pdf = Utils.getInstance().getDescription();

            Utils.tell(player, UpdateNotifications.getUpdateMessage().replace("{currentVer}", pdf.getVersion())
                    .replace("{newVer}", UpdateNotifications.getLatestVersion())
                    .replace("{link}", "https://spigotmc.org/resources/" + UpdateNotifications.getProjectId()));
        }
    }


}
