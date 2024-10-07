package me.sllly.zombies.mechanisms.setup.info;

import me.sllly.zombies.mechanisms.setup.singulars.SingularWindowInfo;
import me.sllly.zombies.objects.Area;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public record WindowInfo(List<SingularWindowInfo> windows) {

    public static WindowInfo getDefaultWindowInfo(){
        Location c1 = new Location(Bukkit.getWorld("zombies"), -1, 102, 10);
        Location c2 = new Location(Bukkit.getWorld("zombies"), 1, 103, 10);
        Location windowSpawn = new Location(Bukkit.getWorld("zombies"), 0.5, 102, 14.5);
        return new WindowInfo(List.of(new SingularWindowInfo(new Area(c1, c2), "main", windowSpawn)));
    }
}
