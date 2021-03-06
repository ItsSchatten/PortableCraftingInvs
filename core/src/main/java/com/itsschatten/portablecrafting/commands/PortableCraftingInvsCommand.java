package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.UniversalCommand;
import com.itsschatten.portablecrafting.Permissions;
import com.itsschatten.portablecrafting.PortableCraftingInvsPlugin;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.configs.SignsConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PortableCraftingInvsCommand extends UniversalCommand {

    private static final List<String> TABBABLE = Arrays.asList("help", "version", "rl", "reload"); // Tabbable lists for tab complete.
    private static final List<String> CONFIGS = Arrays.asList("settings", "messages", "signs");


    public PortableCraftingInvsCommand() {
        super("portablecraftinginvs");

        setAliases(Arrays.asList("portablecraftinginv", "pci", "pi", "portableinv"));
    }

    @Override
    protected void run(CommandSender commandSender, String[] args) {
        if (args.length > 2 && Settings.USE_TOO_MANY_ARGS)
            returnTell(Messages.TOO_MANY_ARGS);

        if (args.length == 0) {
            tell(Messages.NOT_ENOUGH_ARGS + Messages.HELP_MESSAGE); // If not args send help message.
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
                checkPerms(commandSender, Permissions.RELOAD);

                if (args.length == 1) { // Reload if no arguments are passed.
                    Messages.getInstance().reload();
                    Settings.getInstance().reload();

                    if (SignsConfig.getInstance() != null && Settings.USE_SIGNS) {
                        SignsConfig.getInstance().reload();
                    } else if (Settings.USE_SIGNS)
                        SignsConfig.init();

                    tell(Messages.RELOAD_MESSAGE);
                } else if (args.length == 2) {

                    final String param2 = args[1].toLowerCase(); // Set args[1]

                    switch (param2) { // Check if the argument is one of the below, if not send wrong args.
                        case "settings":
                            Settings.getInstance().reload();
                            tell(Messages.RELOAD_SPECIFIC.replace("{file}", "settings.yml"));
                            break;
                        case "messages":
                            Messages.getInstance().reload();
                            tell(Messages.RELOAD_SPECIFIC.replace("{file}", "messages.yml"));
                            break;
                        case "signs":
                            if (SignsConfig.getInstance() != null && Settings.USE_SIGNS) {
                                SignsConfig.getInstance().reload();
                            } else if (Settings.USE_SIGNS)
                                SignsConfig.init();
                            tell(Messages.RELOAD_SPECIFIC.replace("{file}", "signs.yml"));
                        default:
                            tell(Messages.WRONG_ARGS);
                            break;
                    }
                }

                if (Bukkit.getPluginManager().isPluginEnabled("Essentials") && !Settings.USE_CRAFTING) {
                    PortableCraftingInvsPlugin.getInstance().unregisterCommand(CraftCommand.getInstance());
                    CraftCommand.setInstance(null);
                    Utils.log("&c&lAttention:&c The craft command has been disabled, it cannot be re-enabled until a server restart is performed.");
                }

                if (Bukkit.getPluginManager().isPluginEnabled("Essentials") && !Settings.USE_ENDERCHEST) {
                    PortableCraftingInvsPlugin.getInstance().unregisterCommand(EnderChestCommand.getInstance());
                    EnderChestCommand.setInstance(null);
                    Utils.log("&c&lAttention:&c The enderchest command has been disabled, it cannot be re-enabled until a server restart is performed.");
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
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) throws IllegalArgumentException {

        if (args.length == 1) { // If args are one.
            final String arg = args[0];
            final List<String> list = new ArrayList<>();

            for (final String tab : TABBABLE) // Send the first list of tabbable things.
                if (tab.startsWith(arg.toLowerCase()))
                    list.add(tab.toLowerCase());

            return list;
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl"))) { // If args are 2 and arg 1 is equal to reload.
            final String arg2 = args[1];
            final List<String> configList = new ArrayList<>();

            for (final String tab : CONFIGS)
                if (tab.startsWith(arg2))
                    configList.add(tab);
            return configList;

        }
        return super.tabComplete(sender, alias, args);
    }
}
