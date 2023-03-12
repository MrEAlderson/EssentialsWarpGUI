package de.marcely.warpgui.command.config;

import de.marcely.warpgui.WarpGUIPlugin;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class OpenGUIConfig extends Command.Executor {

    public OpenGUIConfig(WarpGUIPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (args.length == 0 && !(sender instanceof Player)) {
            Message.COMMAND_USAGE
                    .placeholder("usage", "/warpcfg opengui <player>")
                    .send(sender);
            return;
        }

        Player target = null;

        if (args.length >= 1) {
            target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                Message.UNKNOWN_PLAYER
                        .placeholder("player", args[0])
                        .send(sender);
                return;
            }

        } else
            target = (Player) sender;

        this.plugin.getRenderer().open(target);
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        switch (args.length) {
            case 0:
                return getPlayers("");
            case 1:
                return getPlayers(args[0]);
            default:
                return Collections.emptyList();
        }
    }
}