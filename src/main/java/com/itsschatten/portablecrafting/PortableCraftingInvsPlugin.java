package com.itsschatten.portablecrafting;

import com.itsschatten.libs.UpdateNotifications;
import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.commands.*;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.events.PlayerJoinListener;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class PortableCraftingInvsPlugin extends JavaPlugin {

    private static PluginDescriptionFile pdf; // So we can access the pdf in this file.

    @Getter
    @Setter(value = AccessLevel.PRIVATE)
    private static PortableCraftingInvsPlugin instance; // The instance stuffs.

    @Override
    public void onEnable() { // We all know what this does right?
        Utils.setInstance(this);
        setInstance(this);

        pdf = this.getDescription();

        Utils.log("",
                "&9+---------------------------------------------------+ ",
                "",
                "&9 _____   _____ _____ ",
                "&9|  __ \\ / ____|_   _|",
                "&3| |__) | |      | |  ",
                "&3|  ___/| |      | |  ",
                "&b| |    | |____ _| |_ ",
                "&b|_|     \\_____|_____|",
                "",
                "&7Developed by " + String.join(",", pdf.getAuthors()),
                "&7Version " + pdf.getVersion());

        // Register configs.
        Settings.init();
        Messages.init();

        if (Settings.USE_UPDATER) {
            new UpdateNotifications(61045) {
                @Override
                public void onUpdateAvailable() {
                    Utils.log("There is an update available for the plugin! Current Version " + pdf.getVersion() + " New Version " + getLatestVersion() + " {https://spigotmc.org/resources/" + getProjectId() + ")");
                }
            }.runTaskAsynchronously(this);

            new CheckForUpdate().runTaskTimerAsynchronously(this, 30 * 60 * 20, 30 * 60 * 20); // Wait 30 minutes and check for another update.
            Utils.debugLog(Settings.DEBUG, "Checked for update, and set timer running.");
        }
        // Register commands, and JoinListener.
        registerCommands(new AnvilCommand(), new CraftCommand(), new EnchanttableCommand(), new EnderChestCommand(), new PortableCraftingInvsCommand());
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Utils.debugLog(Settings.DEBUG, "Loaded the PlayerJoinListener.");


        Utils.log("&9+---------------------------------------------------+ ",
                "");
    }

    @Override
    public void onDisable() { // Remove the instance of the plugin, to help prevent memory leaks, and set it to null in the Utils so we can get a new instance of it when it's reloaded.
        setInstance(null);
        Utils.setInstance(null);
    }

    // A utility method so I can register commands on one line.
    private void registerCommands(Command... commands) {

        try {
            for (Command command : commands) {
                Utils.registerCommand(command);
                Utils.debugLog(Settings.DEBUG, "&7Command " + command + " has been registered.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.log("Some error occurred.");
        }
    }


}
