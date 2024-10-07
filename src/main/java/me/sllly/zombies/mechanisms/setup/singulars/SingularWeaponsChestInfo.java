package me.sllly.zombies.mechanisms.setup.singulars;

import org.bukkit.Location;

import java.util.List;

public record SingularWeaponsChestInfo(Location chestLocation1, Location chestLocation2, int cost, List<String> hologramText, Location hologramLocation, String roomName) {
}
