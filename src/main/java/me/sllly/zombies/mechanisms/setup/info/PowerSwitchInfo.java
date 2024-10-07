package me.sllly.zombies.mechanisms.setup.info;

import org.bukkit.Location;

import java.util.List;

public record PowerSwitchInfo(Location switchLocation, int cost, List<String> hologramText, Location hologramLocation, String roomName) {
}
