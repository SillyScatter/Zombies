package me.sllly.zombies.mechanisms.setup.singulars;

import me.sllly.zombies.objects.Area;
import org.bukkit.Location;

import java.util.List;

public record SingularDoorInfo(String room1, String room2, int cost, List<String> hologramText, Location hologramLocation, Area doorArea) {

    public String getId() {
        return room1 + "``" + room2;
    }
}
