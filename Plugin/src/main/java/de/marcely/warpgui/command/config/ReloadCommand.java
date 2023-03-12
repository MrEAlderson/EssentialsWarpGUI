package de.marcely.warpgui.command.config;

import de.marcely.warpgui.WarpGUIPlugin;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends Command.Executor {

    public ReloadCommand(WarpGUIPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        this.plugin.reloadConfig();

        Message.COMMAND_RELOAD.send(sender);
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}