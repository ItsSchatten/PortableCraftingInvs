package me.itsshadow.portablecrafting.commands;

import me.itsshadow.libs.Utils;
import me.itsshadow.libs.commandutils.UniversalCommand;
import me.itsshadow.portablecrafting.configs.Messages;
import me.itsshadow.portablecrafting.configs.Settings;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PortableCraftingInvsCommand extends UniversalCommand {

    private static List<String> TABBALE = Arrays.asList("help", "version", "rl", "reload");
    private static List<String> TABBALE2 = Arrays.asList("SETTINGS", "MESSAGES");


    public PortableCraftingInvsCommand() {
        super("portablecraftinginv");

        setAliases(Arrays.asList("pci", "pi", "portableinv"));
    }

    @Override
    protected void run(CommandSender commandSender, String[] args) {
        if (args.length == 0)
            returnTell(Messages.HELP_MESSAGE);

        if (args.length > 2 && Settings.USE_TOO_MANY_ARGS)
            returnTell(Messages.TOOMANY_ARGS.replace("{prefix}", Utils.getPrefix()));

        String param = args[0].toLowerCase();

        switch (param) {
            case "help":
                returnTell(Messages.HELP_MESSAGE);
                break;

            case "version":
                Utils.log("Someone named " + commandSender.getName() + " got the version of the plugin; which is " + Utils.getInstance().getDescription().getVersion());
                returnTell("&fThe plugin version is " + Utils.getInstance().getDescription().getVersion());
                break;

            case "reload":
                checkPerms(commandSender, Messages.NO_PERMS, "pci.reload");

                if (args.length == 1) {
                    Messages.getInstance().reload();
                    Settings.getInstance().reload();
                    returnTell(Messages.RELOAD_MESSAGE);
                }

                final String param2 = args[1].toUpperCase();

                switch (param2) {
                    case "SETTINGS":
                        Settings.getInstance().reload();
                        returnTell(Messages.RELOAD_SPECIFIC.replace("{file}", Settings.getFile()));
                        break;
                    case "MESSAGES":
                        Messages.getInstance().reload();
                        returnTell(Messages.RELOAD_SPECIFIC.replace("{file}", Messages.getFile()));
                        break;
                    default:
                        returnTell(Messages.WRONG_ARGS);
                        break;
                }
                break;

            case "rl":
                checkPerms(commandSender, Messages.NO_PERMS, "pci.reload");
                if (args.length == 1) {
                    Messages.getInstance().reload();
                    Settings.getInstance().reload();
                    returnTell(Messages.RELOAD_MESSAGE);
                }

                final String parm2 = args[1].toUpperCase();

                switch (parm2) {
                    case "SETTINGS":
                        Settings.getInstance().reload();
                        returnTell(Messages.RELOAD_SPECIFIC.replace("{file}", Settings.getFile()));
                        break;
                    case "MESSAGES":
                        Messages.getInstance().reload();
                        returnTell(Messages.RELOAD_SPECIFIC.replace("{file}", Messages.getFile()));
                        break;
                    default:
                        returnTell(Messages.WRONG_ARGS);
                        break;
                }
                break;

            default:
                returnTell(Messages.WRONG_ARGS.replace("{prefix}", Utils.getPrefix()));
                break;
        }


    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {

        if (args.length == 1) {
            final String arg = args[0];
            final List<String> list = new ArrayList<>();

            for (final String tab : TABBALE)
                if (tab.startsWith(arg.toLowerCase()))
                    list.add(tab.toLowerCase());

            return list;
        }

        if (args.length == 2) {
            final String arg2 = args[1];
            final List<String> list2 = new ArrayList<>();

            for (final String tab : TABBALE2)
                if (tab.startsWith(arg2.toUpperCase()))
                    list2.add(tab.toUpperCase());
            return list2;

        }

        if (args.length > 2) {
            return super.tabComplete(sender, alias, args);
        }

        return super.tabComplete(sender, alias, args);
    }
}
