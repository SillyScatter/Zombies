package me.sllly.zombies.mechanisms.setup.info;

import me.sllly.zombies.mechanisms.setup.singulars.SingularDoorInfo;
import me.sllly.zombies.objects.Area;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public record DoorInfo(List<SingularDoorInfo> doors) {

    public static DoorInfo getDefaultDoorInfo(){
        Location holLoc1 = new Location(Bukkit.getWorld("zombies"), 8.5, 103, 0.5);
        Location holLoc2 = new Location(Bukkit.getWorld("zombies"), 12.5, 103, 0.5);
        Area doorArea = new Area("zombies", 10, 101, 3, 10, 103, -3);
        SingularDoorInfo door1 = new SingularDoorInfo("green", "orange", 10, List.of("&e&l%room-name%", "&6Cost: 10"), holLoc1, holLoc2, doorArea);
        return new DoorInfo(List.of(door1));
    }
}
