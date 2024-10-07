package me.sllly.zombies.mechanisms.setup.singulars;

import me.sllly.zombies.mechanisms.purchasables.Purchasable;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

public record PurchasableStation(Purchasable purchasable, int cost, List<String> hologramText, Location hologramLocation, Material displayMaterial, String roomName) {
}
