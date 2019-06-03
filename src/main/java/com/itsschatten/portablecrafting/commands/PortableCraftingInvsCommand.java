package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.UniversalCommand;
import com.itsschatten.portablecrafting.Perms;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.configs.Messages;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PortableCraftingInvsCommand extends UniversalCommand {

    private static List<String> TABBALE = Arrays.asList("help", "version", "rl", "reload"); // Tabbable lists for tab complete.
    private static List<String> CONFIGS = Arrays.asList("settings", "messages");


    public PortableCraftingInvsCommand() {
        super("portablecraftinginv");

        setAliases(Arrays.asList("pci", "pi", "portableinv"));
    }

    @Override
    protected void run(CommandSender commandSender, String[] args) {

        if (args.length > 2 && Settings.USE_TOO_MANY_ARGS)
            returnTell(Messages.TOOMANY_ARGS);

        if (args.length == 0) {
            tell(Messages.NOTENOUGH_ARGS + Messages.HELP_MESSAGE); // If not args send help message.
            return;
        }
        String param = args[0].toLowerCase(); // Set param.

        switch (param) {
            case "help": { // The help message.
                returnTell(Messages.HELP_MESSAGE);
                break;
            }

            case "ver":
            case "version": {
                returnTell("&fThe plugin version is " + Utils.getInstance().getDescription().getVersion());
                break;
            }

            case "reload":
            case "rl": {
                checkPerms(commandSender, Perms.RELOAD);

                if (args.length == 1) { // Reload if no arguments are passed.
                    Messages.getInstance().reload();
                    Settings.getInstance().reload();
                    returnTell(Messages.RELOAD_MESSAGE);
                }

                final String param2 = args[1].toLowerCase(); // Set args[1]

                switch (param2) { // Check if the argument is one of the below, if not send wrong args.
                    case "settings":
                        Settings.getInstance().reload();
                        returnTell(Messages.RELOAD_SPECIFIC.replace("{file}", "settings.yml"));
                        break;
                    case "messages":
                        Messages.getInstance().reload();
                        returnTell(Messages.RELOAD_SPECIFIC.replace("{file}", "messages.yml"));
                        break;
                    default:
                        returnTell(Messages.WRONG_ARGS);
                        break;
                }
            }
            break;

            default: {
                if (Settings.USE_HELP_IF_WRONG_ARGS)
                    tell(Messages.HELP_MESSAGE);

                returnTell(Messages.WRONG_ARGS);
                break;
            }
        }


    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {

        if (args.length == 1) { // If args are one.
            final String arg = args[0];
            final List<String> list = new ArrayList<>();

            for (final String tab : TABBALE) // Send the first list of tabbable things.
                if (tab.startsWith(arg.toLowerCase()))
                    list.add(tab.toLowerCase());

            return list;
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl"))) { // If args are 2 and are equal to reload.
            final String arg2 = args[1];
            final List<String> configList = new ArrayList<>();

            for (final String tab : CONFIGS)
                if (tab.startsWith(arg2))
                    configList.add(tab);
            return configList;

        }

        if (args.length > 2) {
            return super.tabComplete(sender, alias, args);
        }

        return super.tabComplete(sender, alias, args);
    }
}
