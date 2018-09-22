package me.itsshadow.portablecrafting;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.itsshadow.libs.Utils;
import me.itsshadow.portablecrafting.commands.*;
import me.itsshadow.portablecrafting.configs.Messages;
import me.itsshadow.portablecrafting.configs.Settings;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

public class PortableCraftingInvsPlugin extends JavaPlugin {

    @Getter
    @Setter(value = AccessLevel.PRIVATE)
    private PortableCraftingInvsPlugin instance;

    @Override
    public void onEnable() {
        registerThings();
    }

    @Override
    public void onDisable() {
        setInstance(null);
        Utils.setInstance(null);
    }

    private void registerThings() {
        Utils.setInstance(this);
        setInstance(this);

        Utils.log("",
                "&9+---------------------------------------------------+ ",
                "");

        registerCommands(new PortableCraftingInvsCommand(), new EnderChestCommand(), new CraftCommand(), new AnvilCommand(), new EnchanttableCommand());

        Utils.log(" ");
        Messages.init();
        Settings.init();

        Utils.setPrefix(Messages.PREFIX);
        Utils.setNoPermsMessage(Messages.NO_PERMS);

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
