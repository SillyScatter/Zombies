package me.sllly.zombies.mechanisms.setup.singulars;

import org.bukkit.Location;

import java.util.List;

public record SingularAbilityStation(String abilityId, int cost, List<String> hologramText, Location hologramLocation, String roomName) {
}
