package me.sllly.zombies.mechanisms.setup.singulars;

import me.sllly.zombies.objects.Area;
import org.bukkit.Location;

public record SingularWindowInfo(Area windowArea, String roomName, Location windowSpawnLocation) {
}
