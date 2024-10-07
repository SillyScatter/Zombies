package me.sllly.zombies.mechanisms.setup.info;

import org.bukkit.Location;

import java.util.List;

public record PackAPunchInfo(Location packAPunchLocation, int cost, List<String> hologramText, Location hologramLocation, String roomName) {
}
