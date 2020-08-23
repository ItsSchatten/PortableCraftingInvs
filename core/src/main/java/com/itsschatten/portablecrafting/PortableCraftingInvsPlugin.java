package com.itsschatten.portablecrafting;

import com.itsschatten.libs.UpdateNotifications;
import com.itsschatten.libs.Utils;
import com.itsschatten.libs.configutils.PlayerConfigManager;
import com.itsschatten.portablecrafting.commands.*;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.configs.SignsConfig;
import com.itsschatten.portablecrafting.listeners.EnchantmentListener;
import com.itsschatten.portablecrafting.listeners.EnderchestListener;
import com.itsschatten.portablecrafting.listeners.PlayerJoinListener;
import com.itsschatten.portablecrafting.listeners.SignListener;
import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class PortableCraftingInvsPlugin extends JavaPlugin {

    @Getter(AccessLevel.PUBLIC)
    private static FakeContainers fakeContainers;

    @Getter
    @Setter(value = AccessLevel.PRIVATE)
    private static PortableCraftingInvsPlugin instance; // The instance stuffs.

    @Getter
    private static String serverVersion;

    @Override
    public void onEnable() { // We all know what this does right? Right!?
        Utils.setInstance(this);
        setInstance(this);

        serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        switch (serverVersion) {
            case "v1_16_R2": {
                fakeContainers = new FakeContainers_v1_16_R2(this);
                break;
            }

            case "v1_16_R1": {
                fakeContainers = new FakeContainers_v1_16_R1(this);
                break;
            }

            case "v1_15_R1": {
                fakeContainers = new FakeContainers_v1_15_R1(this);
                break;
            }
            default: {
                Utils.log("&4&l! Attention ! &cVersion " + serverVersion + " of Spigot is not supported by this plugin, to avoid issues the plugin will be disabled.");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }

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
                "&7Version " + pdf.getVersion(),
                "&7Using Minecraft version " + serverVersion);

        // Register configs.
        Settings.init();
        Messages.init();

        if (Settings.USE_METRICS) {
            Utils.log("&7Metrics are enabled! You can see the information collect at the following link: &chttps://bstats.org/plugin/bukkit/PortableCraftingInvss&7", "If you don't wish for this information to be collected you can disable it in the settings.yml.");
            new MetricsLite(this, 5752);
        }

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
        registerCommands(new AnvilCommand(), new EnchanttableCommand(), new PortableCraftingInvsCommand(), new GrindStoneCommand(),
                new LoomCommand(), new StoneCutterCommand(), new CartographyCommand(), new SmithingCommand(), new FurnaceCommand(),
                new SmokerCommand(), new BlastFurnaceCommand());

        if (Bukkit.getPluginManager().isPluginEnabled("Essentials") && !Settings.USE_CRAFTING) {
            Utils.debugLog(Settings.DEBUG, "Crafting features have been disabled, and Essentials has been installed. To avoid causing issues we are not going to register the command.");
        } else {
            registerCommand(new CraftCommand());
        }

        if (Bukkit.getPluginManager().isPluginEnabled("Essentials") && !Settings.USE_ENDERCHEST) {
            Utils.debugLog(Settings.DEBUG, "Enderchest features have been disabled, and Essentials has been installed. To avoid causing issues we are not going to register the command.");
        } else {
            registerCommand(new EnderChestCommand());
        }

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new EnchantmentListener(), this);
        Utils.debugLog(Settings.DEBUG, "Loaded the PlayerJoinListener.");


        Utils.log("&9+---------------------------------------------------+ ",
                "");
    }

    @Override
    public void onDisable() { // Remove the instance of the plugin, to help prevent memory leaks, and set it to null in the Utils so we can get a new instance of it when it's reloaded.
        if (Settings.USE_FURNACES)
            VirtualFurnaceAPI.getInstance().disableAPI();

        CraftCommand.setInstance(null);
        EnderChestCommand.setInstance(null);
        setInstance(null);
        Utils.setInstance(null);
    }

    // A utility method so I can register commands on one line.
    private void registerCommand(Command command) {
        try {
            Utils.registerCommand(command);
            Utils.debugLog(Settings.DEBUG, "&7Command " + command.getName() + " has been registered.");
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.log("Some error occurred, please report this immediately to ItsSchatten on Spigot or Github. \n(https://github.com/ItsSchatten/PortableCraftingInvs/issues)");
        }
    }

    private void registerCommands(Command... commands) {
        try {
            for (Command command : commands) {
                Utils.registerCommand(command);
                Utils.debugLog(Settings.DEBUG, "&7Command " + command.getName() + " has been registered.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.log("Some error occurred, please report this immediately to ItsSchatten on Spigot or Github. \n(https://github.com/ItsSchatten/PortableCraftingInvs/issues)");
        }
    }

    public void unregisterCommand(Command command) {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            command.unregister(commandMap);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
