package com.itsschatten.portablecrafting;

import com.itsschatten.libs.UpdateNotifications;
import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.commands.*;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.configs.SignsConfig;
import com.itsschatten.portablecrafting.listeners.EnderchestListener;
import com.itsschatten.portablecrafting.listeners.PlayerJoinListener;
import com.itsschatten.portablecrafting.listeners.SignListener;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class PortableCraftingInvsPlugin extends JavaPlugin {


    @Getter
    @Setter(value = AccessLevel.PRIVATE)
    private static PortableCraftingInvsPlugin instance; // The instance stuffs.

    @Override
    public void onEnable() { // We all know what this does right? Right!?
        Utils.setInstance(this);
        setInstance(this);

        final PluginDescriptionFile pdf = this.getDescription();

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

        if (Settings.USE_METRICS) {
            Utils.log("&7Metrics are enabled! You can see the information collect at the following link: &chttps://bstats.org/plugin/bukkit/PortableCraftingInvss&7", "If you don't wish for this information to be collected you can disable it in the settings.yml.");
            new Metrics(this);
        }

        Utils.log(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);

        if (Settings.USE_UPDATER) {
            new UpdateNotifications(61045) {
                @Override
                public void onUpdateAvailable() {
                    if (Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_15_R1")) {
                        Utils.log("There is an update available for the plugin! Current Version " + pdf.getVersion() + " New Version " + getLatestVersion() + " {https://spigotmc.org/resources/" + getProjectId() + ")");
                    }
                    Utils.debugLog(Settings.DEBUG, "There is an update to the plugin available but the version is not the latest supported version. To ensure that we don't spam the user's console we won't send a message.");
                    Utils.log("&4&l[WARNING]&c Hey! Just wanted to let you know that you are using an older version of the plugin on an unsupported version of Minecraft. If you don't wish to see this message you can disable update checking in the settings.yml.");
                }
            }.runTaskAsynchronously(this);

            if (Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_15_R1")) {
                new CheckForUpdate().runTaskTimerAsynchronously(this, 30 * 60 * 20, 30 * 60 * 20); // Wait 30 minutes and check for another update.
                Utils.debugLog(Settings.DEBUG, "Checked for update, and set timer running.");
            }
        }

        if (Settings.USE_ENDERCHEST_RESTRICTION) {
            this.getServer().getPluginManager().registerEvents(new EnderchestListener(), this);
            Utils.debugLog(Settings.DEBUG, "USE_ENDERCHEST_RESTRICTIONS is true; EnderchestListener has been initialized.");
        }

        if (Settings.USE_SIGNS) {
            SignsConfig.init();
            this.getServer().getPluginManager().registerEvents(new SignListener(), this);
            Utils.debugLog(Settings.DEBUG, "Signs have been enabled.");
        }

        // Register commands, and JoinListener.
        registerCommands(new AnvilCommand(), new CraftCommand(), new EnchanttableCommand(), new EnderChestCommand(), new PortableCraftingInvsCommand(), new GrindStoneCommand(), new LoomCommand(), new StoneCutterCommand(), new CartographyCommand());
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
                Utils.debugLog(Settings.DEBUG, "&7Command " + command.getName() + " has been registered.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.log("Some error occurred, please report this immediately to ItsSchatten on Spigot or BitBucket. \n(https://bitbucket.org/ItsSchatten/PortableCraftingInvs/issues)");
        }
    }


}
