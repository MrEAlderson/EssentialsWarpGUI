package de.marcely.warpgui.command.config;

import de.marcely.warpgui.GUIWarp;
import de.marcely.warpgui.WarpGUIPlugin;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListCommand extends Command.Executor {

    public ListCommand(WarpGUIPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        final int count = this.plugin.getProvider().getWarps().size();
        final String warps = count != 0 ?
                this.plugin.getContainer().getAll().stream()
                        .filter(GUIWarp::hasHook)
                        .map(GUIWarp::getName)
                        .sorted(String::compareTo)
                        .collect(Collectors.joining(", ")) :
                (ChatColor.RED + Message.NONE.get());

        Message.COMMAND_LIST
                .placeholder("amount", "" + count)
                .placeholder("warps", warps)
                .send(sender);
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}