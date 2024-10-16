package me.sllly.zombies.mechanisms.setup.info;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public record PowerSwitchInfo(Location switchLocation, int cost, List<String> hologramText, Location hologramLocation, String roomName) {

    public static PowerSwitchInfo getDefaultPowerSwitchInfo() {
        Location switchLocation = new Location(Bukkit.getWorld("zombies"), 20, 102, 4);
        Location hologramLocation = new Location(Bukkit.getWorld("zombies"), 20.5, 103, 4.5);
        List<String> hologramText = List.of("&6Power Switch", "&eCost: &c100");
        return new PowerSwitchInfo(switchLocation, 100, hologramText, hologramLocation, "orange");
    }
}
