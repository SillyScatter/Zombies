package me.sllly.zombies.commands.arguments;

import com.octanepvp.splityosis.commandsystem.SYSArgument;
import com.octanepvp.splityosis.commandsystem.SYSCommand;
import me.sllly.zombies.Zombies;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.UUID;

public class GameArgument extends SYSArgument {
    @Override
    public boolean isValid(String s) {
        try {
            return Zombies.activeGames.containsKey(UUID.fromString(s));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public List<String> getInvalidInputMessage(String s) {
        return List.of("&cGame &4" + s + " &cdoes not exist.");
    }

    @Override
    public @NonNull List<String> tabComplete(CommandSender sender, SYSCommand command, String input) {
        return Zombies.activeGames.keySet().stream()
                .map(UUID::toString) // Convert each UUID to a String
                .filter(uuidStr -> uuidStr.toLowerCase().startsWith(input.toLowerCase())) // Apply the filter on the string version
                .toList(); // Collect as a list
    }
}
