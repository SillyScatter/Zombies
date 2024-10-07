package me.sllly.zombies.commands.arguments;

import com.octanepvp.splityosis.commandsystem.SYSArgument;
import com.octanepvp.splityosis.commandsystem.SYSCommand;
import me.sllly.zombies.Zombies;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class GameTemplateArgument extends SYSArgument {
    @Override
    public boolean isValid(String s) {
        return Zombies.gameTemplates.containsKey(s);
    }

    @Override
    public List<String> getInvalidInputMessage(String s) {
        return List.of("&cGame template &4" + s + " &cdoes not exist.");
    }

    @Override
    public @NonNull List<String> tabComplete(CommandSender sender, SYSCommand command, String input) {
        return Zombies.gameTemplates.keySet().stream()
                .filter(s -> s.toLowerCase().startsWith(input.toLowerCase()))
                .toList();
    }
}
