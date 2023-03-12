package de.marcely.warpgui.command;

import de.marcely.warpgui.GUIWarp;
import de.marcely.warpgui.WarpGUIPlugin;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Command {

    private final String name;
    private final Executor executor;
    private final String consoleUsage, playerUsage;
    private final int minArgsConsole, minArgsPlayer;
    private final boolean isPlayerOnly;

    public String getUsage(String label, boolean isPlayer) {
        return "/" + label+ " " +
                this.name + " " +
                (isPlayer ? this.playerUsage : this.consoleUsage);
    }


    public static abstract class Executor {

        protected final WarpGUIPlugin plugin;

        public Executor(WarpGUIPlugin plugin) {
            this.plugin = plugin;
        }

        public abstract void onExecute(CommandSender sender, String[] args);

        public abstract List<String> onTab(CommandSender sender, String[] args);


        // helpers for tab
        public static List<String> getCommands(Collection<Command> cmds, String written) {
            return cmds.stream()
                    .map(Command::getName)
                    .filter(cmd -> cmd.toLowerCase().startsWith(written.toLowerCase()))
                    .collect(Collectors.toList());
        }

        protected List<String> getPlayers(String written) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(p -> p.toLowerCase().startsWith(written.toLowerCase()))
                    .collect(Collectors.toList());
        }

        protected List<String> getWarps(String written) {
            return this.plugin.getContainer().getAll().stream()
                    .map(GUIWarp::getName)
                    .filter(k -> k.toLowerCase().startsWith(written.toLowerCase()))
                    .collect(Collectors.toList());
        }

        protected List<String> getArray(String written, String... entries) {
            return Arrays.stream(entries)
                    .filter(k -> k.toLowerCase().startsWith(written.toLowerCase()))
                    .collect(Collectors.toList());
        }
    }
}
