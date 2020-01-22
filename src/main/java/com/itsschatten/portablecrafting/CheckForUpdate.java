package com.itsschatten.portablecrafting;

import com.itsschatten.libs.UpdateNotifications;
import com.itsschatten.libs.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This is a class that checks for an update when run.
 */
public class CheckForUpdate extends BukkitRunnable {

    @Getter
    @Setter
    private static boolean updateAvailable = false;

    @Override
    public void run() {
        Utils.log("Checking for update..."); // We notify the user that we are going to check for an update.

        new UpdateNotifications(61045) {
            @Override
            public void onUpdateAvailable() {
                if (isUpdateAvailable()) {
                    cancel();
                }

                Bukkit.getOnlinePlayers().forEach((admin) -> {
                    if (admin.hasPermission(Permissions.UPDATE_NOTIFICATIONS.getPermission())) // Check if there are any online players we can send the message to.
                        Utils.tell(admin, UpdateNotifications.getUpdateMessage().replace("{currentVer}", PortableCraftingInvsPlugin.getInstance().getDescription().getVersion())
                                .replace("{newVer}", UpdateNotifications.getLatestVersion())
                                .replace("{link}", "https://spigotmc.org/resources/" + UpdateNotifications.getProjectId()));
                });

                Utils.log(UpdateNotifications.getUpdateMessage().replace("{currentVer}", PortableCraftingInvsPlugin.getInstance().getDescription().getVersion())
                        .replace("{newVer}", UpdateNotifications.getLatestVersion())
                        .replace("{link}", "https://spigotmc.org/resources/" + UpdateNotifications.getProjectId())); // We log it to the console.

                setUpdateAvailable(true);
            }
        };
    }
}
