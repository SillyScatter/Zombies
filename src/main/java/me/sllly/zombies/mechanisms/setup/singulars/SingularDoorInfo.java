package me.sllly.zombies.mechanisms.setup.singulars;

import me.sllly.zombies.objects.Area;
import org.bukkit.Location;

import java.util.List;

public record SingularDoorInfo(String room1, String room2, int cost, List<String> hologramText, Location hologram1Location, Location hologram2Location, Area doorArea) {


    /**
     * Used only in config saving, Door class has another method to get the ID
     * @return
     */

    public String getId() {
        return "door``"+room1 + "``" + room2;
    }
}
