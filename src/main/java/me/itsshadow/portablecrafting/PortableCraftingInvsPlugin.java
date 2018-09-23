package me.itsshadow.portablecrafting;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.itsshadow.libs.UpdateNotifications;
import me.itsshadow.libs.Utils;
import me.itsshadow.portablecrafting.commands.*;
import me.itsshadow.portablecrafting.configs.Messages;
import me.itsshadow.portablecrafting.configs.Settings;
import me.itsshadow.portablecrafting.events.PlayerJoinListener;
import org.bukkit.command.Command;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PortableCraftingInvsPlugin extends JavaPlugin {

    @Getter
    @Setter(value = AccessLevel.PRIVATE)
    private PortableCraftingInvsPlugin instance;

    private static PluginDescriptionFile pdf;

    @Override
    public void onEnable() {
        Utils.setInstance(this);
        setInstance(this);

        pdf = this.getDescription();

        registerThings();

        new UpdateNotifications(61045){
            @Override
            public void onUpdateAvailable() {
                Utils.log("There is an update available for the plugin! Current Version " + pdf.getVersion() + " New Version " + getLatestVersion() + " {https://spigotmc.org/resources/" + getProjectId() + ")");
            }
        }.runTaskAsynchronously(this);
    }

    @Override
    public void onDisable() {
        setInstance(null);
        Utils.setInstance(null);
    }

    private void registerThings() {

        Utils.log("",
                "&9+---------------------------------------------------+ ",
                "");

        registerCommands(new PortableCraftingInvsCommand(), new EnderChestCommand(), new CraftCommand(), new AnvilCommand(), new EnchanttableCommand());
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        Utils.log(" ");
        Messages.init();
        Settings.init();

        Utils.setPrefix(Messages.PREFIX);
        Utils.setNoPermsMessage(Messages.NO_PERMS);
        Utils.setUpdateAvailableMessage(Messages.UPDATE_AVALIABLE_MESSAGE);

        Utils.log("",
                "&9+---------------------------------------------------+ ",
                "");

    }

    private void registerCommands(Command... commands) {

        try{
            for (Command command : commands)
                Utils.registerCommand(command);

            Utils.log("&7All plugin commands have been loaded.");
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.log("Some error occurred.");
        }
    }


}
