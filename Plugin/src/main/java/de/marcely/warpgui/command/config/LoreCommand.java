package de.marcely.warpgui.command.config;

import de.marcely.warpgui.GUIWarp;
import de.marcely.warpgui.WarpGUIPlugin;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.command.Command;
import de.marcely.warpgui.util.ChatColorUtil;
import de.marcely.warpgui.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LoreCommand extends Command.Executor {

    public LoreCommand(WarpGUIPlugin plugin) {
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

        final String command = "/warpcfg lore " + args[0];

        if (args.length == 1) {
            if (!warp.getLore().isEmpty()) {
                int num = 1;

                for (String line : warp.getLore()) {
                    sender.sendMessage("" + ChatColor.GRAY + ChatColor.BOLD + num + ": " + ChatColorUtil.translate(line));
                    num++;
                }

                sender.sendMessage("");
            }

            sender.sendMessage(ChatColor.YELLOW + command + " add <text>");

            if (!warp.getLore().isEmpty()) {
                sender.sendMessage(ChatColor.YELLOW + command + " set <line> <text>");
                sender.sendMessage(ChatColor.YELLOW + command + " remove <line>");
                sender.sendMessage(ChatColor.YELLOW + command + " clear");
            }

            return;
        }

        switch (args[1].toLowerCase()) {
            case "add": {
                if (args.length == 2) {
                    Message.COMMAND_USAGE
                            .placeholder("usage", command + " add <text>")
                            .send(sender);
                    return;
                }

                final List<String> lore = new ArrayList<>(warp.getLore());
                final String text = "&7" + String.join(" ", Arrays.copyOfRange(args, 2, args.length));

                lore.add(text);

                warp.setLore(lore);
                warp.updateDisplayedIcon();
                warp.save();

                Message.COMMAND_ADD_LORE
                        .placeholder("warp", warp.getName())
                        .placeholder("lore", ChatColorUtil.translate(text))
                        .send(sender);
            }
            break;

            case "set": {
                if (args.length <= 3) {
                    Message.COMMAND_USAGE
                            .placeholder("usage", command + " set <line> <text>")
                            .send(sender);
                    return;
                }

                final Integer line = StringUtil.parseInt(args[2]);
                final String text = "&7" + String.join(" ", Arrays.copyOfRange(args, 3, args.length));

                if (line == null) {
                    Message.NOT_NUMBER
                            .placeholder("number", args[2])
                            .send(sender);
                    return;
                }

                if (line < 1 || line > warp.getLore().size()) {
                    Message.LINE_OUT_OF_BOUNDS
                            .placeholder("line", "" + line)
                            .send(sender);
                    return;
                }

                final List<String> lore = new ArrayList<>(warp.getLore());

                lore.set(line-1, text);

                warp.setLore(lore);
                warp.updateDisplayedIcon();
                warp.save();

                Message.COMMAND_SET_LORE
                        .placeholder("warp", warp.getName())
                        .placeholder("pos", "" + line)
                        .placeholder("lore", ChatColorUtil.translate(text))
                        .send(sender);
            }
            break;

            case "remove": {
                if (args.length == 2) {
                    Message.COMMAND_USAGE
                            .placeholder("usage", command + " remove <line>")
                            .send(sender);
                    return;
                }

                final Integer line = StringUtil.parseInt(args[2]);

                if (line == null) {
                    Message.NOT_NUMBER
                            .placeholder("number", args[2])
                            .send(sender);
                    return;
                }

                if (line < 1 || line > warp.getLore().size()) {
                    Message.LINE_OUT_OF_BOUNDS
                            .placeholder("line", "" + line)
                            .send(sender);
                    return;
                }

                final List<String> lore = new ArrayList<>(warp.getLore());

                final String text = ChatColorUtil.translate(lore.remove(line-1));

                warp.setLore(lore);
                warp.updateDisplayedIcon();
                warp.save();

                Message.COMMAND_REMOVE_LORE
                        .placeholder("warp", warp.getName())
                        .placeholder("pos", "" + line)
                        .placeholder("lore", text)
                        .send(sender);
            }
            break;

            case "clear": {
                warp.setLore(Collections.emptyList());
                warp.updateDisplayedIcon();
                warp.save();

                Message.COMMAND_CLEAR_LORE
                        .placeholder("warp", warp.getName())
                        .send(sender);
            }
            break;

            default: {
                Message.UNKNOWN_COMMAND
                        .placeholder("command", args[1])
                        .send(sender);
            }
            break;
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        switch (args.length) {
            case 0:
                return getWarps("");
            case 1:
                return getWarps(args[0]);
            case 2:
                return getArray(args[1], "add", "set", "remove", "clear");
            default:
                return Collections.emptyList();
        }
    }
}