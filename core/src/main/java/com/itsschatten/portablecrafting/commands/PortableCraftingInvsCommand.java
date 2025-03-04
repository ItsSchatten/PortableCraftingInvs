package com.itsschatten.portablecrafting.commands;

import com.itsschatten.libs.InteractiveMessages;
import com.itsschatten.libs.Utils;
import com.itsschatten.libs.commandutils.UniversalCommand;
import com.itsschatten.portablecrafting.MigrationTask;
import com.itsschatten.portablecrafting.Permissions;
import com.itsschatten.portablecrafting.PortableCraftingInvsPlugin;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.configs.SignsConfig;
import com.itsschatten.portablecrafting.storage.StorageMedium;
import com.itsschatten.portablecrafting.virtual.VirtualManager;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class PortableCraftingInvsCommand extends UniversalCommand {

    // Tabbable lists for tab complete.
    // TODO: Dump command.
    private static final List<String> TABBABLE = Arrays.asList("help", "version", "rl", "reload", /*"dump",*/ "migrate");
    private static final List<String> CONFIGS = Arrays.asList("settings", "messages", "signs");

    public PortableCraftingInvsCommand() {
        super("portablecraftinginvs");

        setAliases(Arrays.asList("portablecraftinginv", "pci", "pi", "portableinv"));
    }

    @Override
    protected void run(CommandSender commandSender, String @NotNull [] args) {
        if (args.length > 2 && Settings.USE_TOO_MANY_ARGS)
            returnTell(Messages.TOO_MANY_ARGS);

        if (args.length == 0) {
            tell(Messages.NOT_ENOUGH_ARGS + Messages.HELP_MESSAGE); // If no args send a help message.
            return;
        }

        final String param = args[0].toLowerCase(); // Set param.

        switch (param) {
            // The help message.
            case "help" -> returnTell(Messages.HELP_MESSAGE);

            // TODO: Dump command.
            // Dump debug information into a file, so it can be uploaded.
            //case "dump" -> returnTell("&cYou've found a command that doesn't function at all yet!");

            case "migrate" -> {
                if (!commandSender.hasPermission("pci.migrate")) {
                    returnTell("&cYou do not have permission to migrate data.");
                    return;
                }

                if (args.length >= 2) {
                    try {
                        final StorageMedium medium = StorageMedium.valueOf(args[1].toUpperCase());

                        Bukkit.getScheduler().runTaskAsynchronously(Utils.getInstance(), new MigrationTask(medium));
                        tell("&aAttempting to migrate your data!");
                        tell("&7&oYou will not be notified when it completes! Check the console for when it will complete.");
                    } catch (IllegalArgumentException ex) {
                        returnTell("&cInvalid medium! Must be one of the following: &e" + String.join(", ", Stream.of(StorageMedium.values()).map(Enum::name).toList()));
                    }
                } else {
                    returnTell("&cPlease provide which medium you want to migrate to.");
                }
            }

            case "info", "ver", "version" -> {
                if (VirtualManager.getInstance() == null || !VirtualManager.getInstance().isLoaded()) {
                    tell("""
                            &7&oPortableCraftingInvs by ItsSchatten&r
                            &8&oThis information may be used to debug issues and may be requested when reporting a bug report.
                            &r
                            &c&lThis server is not using Virtual tiles!
                            &eYou are currently running version &b{ver}&e of PCI.
                            &r
                            &cThis server is running &3{version}&c.
                            &cThis server is using {medium}&c as its storage solution.
                            &7(&oUnused due to not utilizing virtual tiles.&7)"""
                            .replace("{ver}", Utils.getInstance().getDescription().getVersion())
                            .replace("{version}", Bukkit.getVersion())
                            .replace("{medium}", VirtualManager.getInstance().getStorage().implementationName()));
                } else {
                    tell("""
                            &7&oPortableCraftingInvs by ItsSchatten&r
                            &8&oThis information may be used to debug issues and may be requested when reporting a bug report.
                            &r
                            &a&lThis server is using Virtual tiles!
                            &eYou are currently running version &b{ver}&e of PCI.
                            &eThis server currently has &b{total_players} total players&e that have created at least one virtual tile entity.
                            &r
                            &cThis server is running &3{version}&c.
                            &cThis server has &3{total_active_ticking}&c ticking virtual tiles.
                            &cThis server is using {medium}&c as its storage solution."""
                            .replace("{ver}", Utils.getInstance().getDescription().getVersion())
                            .replace("{total_players}", VirtualManager.getInstance().allUniquePlayers() + "")
                            .replace("{version}", Bukkit.getVersion())
                            .replace("{total_active_ticking}", VirtualManager.getInstance().totalTickingVirtualTiles() + "")
                            .replace("{medium}", "&a" + VirtualManager.getInstance().getStorage().implementationName()));
                }

                final String url = "https://github.com/ItsSchatten/PortableCraftingInvs/issues/new" +
                        "?labels=type%3A+bug%2Cpriority%3A+normal%2Cstatus%3A+awaiting+confirmation" +
                        "&template=bug_report.yml" +
                        "&version=" + getVersionInfoForURL();

                if (commandSender instanceof Player player) {
                    InteractiveMessages.builder("&6[Open a Bug Report]").onHover("&7Click to open a bug report on PortableCraftingInvs Github.")
                            .onClick(ClickEvent.Action.OPEN_URL, url).send(player);
                } else {
                    tell("&6Click the below link to open a bug report for PortableCraftingInvs.");
                    tell(url);
                }
            }

            case "reload", "rl" -> {
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
                    Utils.logWarning("&c&lAttention:&c The craft command has been disabled, it cannot be re-enabled until a server restart is performed.");
                }

                if (Bukkit.getPluginManager().isPluginEnabled("Essentials") && !Settings.USE_ENDERCHEST) {
                    PortableCraftingInvsPlugin.getInstance().unregisterCommand(EnderChestCommand.getInstance());
                    EnderChestCommand.setInstance(null);
                    Utils.logWarning("&c&lAttention:&c The enderchest command has been disabled, it cannot be re-enabled until a server restart is performed.");
                }
            }

            default -> {
                if (Settings.USE_HELP_IF_WRONG_ARGS)
                    tell(Messages.HELP_MESSAGE);

                returnTell(Messages.WRONG_ARGS);
            }
        }
    }

    @Contract(pure = true)
    private @NotNull String getVersionInfoForURL() {
        return URLEncoder.encode("""
                        Server Version: {server}
                        PCI Version: {ver}
                        \s
                        Storage Medium: {medium}"""
                        .replace("{server}", Bukkit.getVersion())
                        .replace("{ver}", Utils.getInstance().getDescription().getVersion())
                        .replace("{medium}", Settings.STORAGE_MEDIUM)
                , StandardCharsets.UTF_8);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String @NotNull [] args) throws IllegalArgumentException {
        if (args.length == 1) { // If args are one.
            final String arg = args[0];
            final List<String> list = new ArrayList<>();

            for (final String tab : TABBABLE) // Send the first list of tabbable things.
                if (tab.startsWith(arg.toLowerCase()))
                    list.add(tab.toLowerCase());

            return list;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("migrate") && sender.hasPermission("pci.migrate")) {
            return Stream.of(StorageMedium.values()).map(Enum::name).filter((name) -> name.toLowerCase().contains(args[1].toLowerCase())).toList();
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
