package de.marcely.warpgui.command.config;

import de.marcely.warpgui.GUIWarp;
import de.marcely.warpgui.WarpGUIPlugin;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.command.Command;
import de.marcely.warpgui.util.ChatColorUtil;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DisplayNameCommand extends Command.Executor {

    public DisplayNameCommand(WarpGUIPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        final GUIWarp warp = this.plugin.getContainer().getHooked(args[0]);

        if (warp == null) {
            Message.UNKNOWN_WARP
                    .placeholder("warp", args[0])
                    .send(sender);

            return;
        }

        if (args[1].equals("RESET"))
            warp.setDisplayName("&f" + warp.getHook().getName());
        else
            warp.setDisplayName("&f" + String.join(" ", Arrays.copyOfRange(args, 1, args.length)));

        warp.updateDisplayedIcon();
        warp.save();

        Message.COMMAND_SET_DISPLAYNAME
                .placeholder("warp", warp.getName())
                .placeholder("display-name", ChatColorUtil.translate(warp.getDisplayName()))
                .send(sender);
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        switch (args.length) {
            case 0:
                return getWarps("");
            case 1:
                return getWarps(args[0]);
            default:
                return Collections.emptyList();
        }
    }
}