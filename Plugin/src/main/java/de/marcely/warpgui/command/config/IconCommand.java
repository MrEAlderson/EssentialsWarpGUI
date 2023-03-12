package de.marcely.warpgui.command.config;

import de.marcely.warpgui.GUIWarp;
import de.marcely.warpgui.WarpGUIPlugin;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.command.Command;
import de.marcely.warpgui.util.ItemStackUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class IconCommand extends Command.Executor {

    public IconCommand(WarpGUIPlugin plugin) {
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

        final ItemStack icon = ItemStackUtil.parse(args[1]);

        if (icon == null) {
            Message.INVALID_MATERIAL
                    .placeholder("material", args[1])
                    .send(sender);
            return;
        }

        warp.setBaseIcon(icon);
        warp.updateDisplayedIcon();
        warp.save();

        Message.COMMAND_SET_ICON
                .placeholder("warp", warp.getName())
                .placeholder("icon", ItemStackUtil.serialize(warp.getBaseIcon()))
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