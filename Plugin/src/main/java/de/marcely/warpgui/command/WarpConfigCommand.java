package de.marcely.warpgui.command;

import de.marcely.warpgui.WarpGUIPlugin;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.command.config.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class WarpConfigCommand implements CommandExecutor, TabExecutor {

    private final Map<String, Command> registeredCommands = new LinkedHashMap<>();

    public void registerDefaultCommands(WarpGUIPlugin plugin) {
        registerCommand(
                "list",
                new ListCommand(plugin),
                "",
                0,
                false
        );
        registerCommand(
                "opengui",
                new OpenGUIConfig(plugin),
                "[player]",
                0,
                false
        );
        registerCommand(
                "displayname",
                new DisplayNameCommand(plugin),
                "<warp> <name or RESET>",
                2,
                false
        );
        registerCommand(
                "icon",
                new IconCommand(plugin),
                "<warp> <material>",
                2,
                false
        );
        registerCommand(
                "lore",
                new LoreCommand(plugin),
                "<warp> [..]",
                1,
                false
        );
        registerCommand(
                "slot",
                new SlotCommand(plugin),
                "<warp> <RESET or \"<x> <y>\">",
                2,
                false
        );
        registerCommand(
                "reload",
                new ReloadCommand(plugin),
                "",
                0,
                false
        );
        /*registerCommand(
                "debug",
                new DebugCommand(plugin),
                "",
                0,
                false
        );*/
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command arg0, @NotNull String label, @NotNull String[] args) {
        final boolean isPlayer = sender instanceof Player;

        if (!sender.hasPermission("warpcfg.cfg")) {
            Message.INSUFFICIENT_PERMISSIONS.send(sender);
            return true;
        }

        // help
        if (args.length == 0) {
            sender.sendMessage("" + ChatColor.GOLD + ChatColor.BOLD + "Commands:");

            for (Command cmd : this.registeredCommands.values()){
                sender.sendMessage(" " + cmd.getUsage(label, isPlayer));
            }

            return true;
        }

        // try to exectue command
        final Command cmd = getByName(args[0]);

        if (cmd == null) {
            Message.UNKNOWN_COMMAND
                            .placeholder("command", args[0])
                            .send(sender);
            return true;
        }

        if (!isPlayer && cmd.isPlayerOnly()) {
            Message.PLAYERS_ONLY.send(sender);
            return true;
        }

        args = Arrays.copyOfRange(args, 1, args.length);

        if (args.length < (isPlayer ? cmd.getMinArgsPlayer() : cmd.getMinArgsConsole())) {
            Message.COMMAND_USAGE
                    .placeholder("usage", cmd.getUsage(label, isPlayer))
                    .send(sender);
            return true;
        }

        // yay :)
        cmd.getExecutor().onExecute(sender, args);

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args){
        if (args.length == 0)
            return Command.Executor.getCommands(this.registeredCommands.values(), "");

        if (args.length == 1)
            return Command.Executor.getCommands(this.registeredCommands.values(), args[0]);

        final Command cmd = getByName(args[0]);

        if (cmd == null || cmd.isPlayerOnly() && !(sender instanceof Player))
            return Collections.emptyList();

        return cmd.getExecutor().onTab(
                sender,
                Arrays.copyOfRange(args, 1, args.length)
        );
    }

    @Nullable
    public Command getByName(String name) {
        return this.registeredCommands.get(name.toLowerCase());
    }

    private void registerCommand(
            String name,
            Command.Executor executor,
            String usageConsole,
            String usagePlayer,
            int minArgsConsole,
            int minArgsPlayer,
            boolean playerOnly){

        final Command command = new Command(
                name,
                executor,
                usageConsole,
                usagePlayer,
                minArgsConsole,
                minArgsPlayer,
                playerOnly
        );

        this.registeredCommands.put(name.toLowerCase(), command);
    }

    private void registerCommand(
            String name,
            Command.Executor executor,
            String usage,
            int minArgs,
            boolean playerOnly) {

        registerCommand(name, executor, usage, usage, minArgs, minArgs, playerOnly);
    }
}
