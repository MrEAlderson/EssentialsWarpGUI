package de.marcely.warpgui.command.config;

import de.marcely.warpgui.WarpGUIPlugin;
import de.marcely.warpgui.command.Command;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DebugCommand extends Command.Executor {

    public DebugCommand(WarpGUIPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        try {
            final IEssentials ess = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");

            for (String key : new ArrayList<>(ess.getWarps().getList()))
                ess.getWarps().removeWarp(key);


            for (int i = 0; i < Integer.parseInt(args[0]); i++) {
                ess.getWarps().setWarp("warp" + i, ((Player) sender).getLocation());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}