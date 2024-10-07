package me.sllly.zombies.commands;

import com.octane.sllly.octaneitemregistry.util.Util;
import com.octanepvp.splityosis.commandsystem.SYSCommand;
import com.octanepvp.splityosis.commandsystem.SYSCommandBranch;
import com.octanepvp.splityosis.commandsystem.arguments.PlayerArgument;
import com.octanepvp.splityosis.commandsystem.arguments.StringArgument;
import me.sllly.zombies.Zombies;
import me.sllly.zombies.commands.arguments.GameArgument;
import me.sllly.zombies.commands.arguments.GameTemplateArgument;
import me.sllly.zombies.mechanisms.Game;
import org.bukkit.Bukkit;

import java.util.UUID;

public class ZombiesCommandSystem extends SYSCommandBranch {
    public ZombiesCommandSystem(String... names) {
        super(names);
        setPermission("zombies.admin");
        addCommand(new SYSCommand("reload")
                .setArguments()
                .executes((commandSender, strings) -> {
                    Zombies.plugin.reload();
                    Util.sendMessage(commandSender, "&aReloaded.");
                }));

        SYSCommandBranch gameBranch = new SYSCommandBranch("game");
        addBranch(gameBranch);

        gameBranch.addCommand(new SYSCommand("start")
                .setArguments(new GameTemplateArgument())
                .executes((commandSender, strings) -> {
                    Game game = new Game(Zombies.gameTemplates.get(strings[0]));
                    Util.sendMessage(commandSender, "&aGame started.");
                }));

        gameBranch.addCommand(new SYSCommand("join")
                .setArguments(new GameArgument(), new PlayerArgument())
                .executes((commandSender, strings) -> {
                    Game game = Zombies.activeGames.get(UUID.fromString(strings[0]));
                    game.addPlayer(Bukkit.getPlayer(strings[1]));
                    Util.sendMessage(commandSender, "&aJoined game.");
                }));
    }
}
