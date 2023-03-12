package de.marcely.warpgui.command.config;

import de.marcely.warpgui.GUIWarp;
import de.marcely.warpgui.WarpGUIPlugin;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.command.Command;
import de.marcely.warpgui.util.StringUtil;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class SlotCommand extends Command.Executor {

    public SlotCommand(WarpGUIPlugin plugin) {
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

        if (args[1].equalsIgnoreCase("RESET")) {
            warp.setSlotX(-1);
            warp.setSlotY(-1);
            warp.save();

            Message.COMMAND_RESET_SLOT
                    .placeholder("warp", warp.getName())
                    .send(sender);

            return;
        }

        if (args.length == 2) {
            Message.COMMAND_USAGE
                    .placeholder("usage", "/warpcfg slot " + warp.getName() + " <x> <y>")
                    .send(sender);
            return;
        }

        final Integer x = StringUtil.parseInt(args[1]);
        final Integer y = StringUtil.parseInt(args[2]);

        if (x == null) {
            Message.NOT_NUMBER
                    .placeholder("number", args[1])
                    .send(sender);
            return;
        }

        if (y == null) {
            Message.NOT_NUMBER
                    .placeholder("number", args[2])
                    .send(sender);
            return;
        }

        if (x < 0 || x > 8 || y < 0) {
            Message.COORD_OUT_OF_BOUNDS
                    .placeholder("x", "" + x)
                    .placeholder("y", "" + y)
                    .send(sender);
            return;
        }

        warp.setSlotX(x);
        warp.setSlotY(y);
        warp.save();

        Message.COMMAND_SET_SLOT
                .placeholder("warp", warp.getName())
                .placeholder("x", "" + x)
                .placeholder("y", "" + y)
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