package me.sllly.zombies.files.configs;

import com.octanepvp.splityosis.configsystem.configsystem.AnnotatedConfig;
import com.octanepvp.splityosis.configsystem.configsystem.ConfigField;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;

public class SettingsConfig extends AnnotatedConfig {
    public SettingsConfig(File parentDirectory, String name) {
        super(parentDirectory, name);
    }

    @ConfigField(path = "window.repair.distance", comment = "The distance a player can be from the centre of a window to repair it.")
    public double windowRepairDistance = 2.5;

    @ConfigField(path = "window.repair.time", comment = "How many gameticks to wait beteween each repair tick.")
    public int windowRepairTime = 20;

    @ConfigField(path = "window.damage.distance", comment = "The distance the nearest zombie must be to a window for it to take damage.")
    public double windowDamageDistance = 2.5;

    @ConfigField(path = "window.damage.time", comment = "How many gameticks to wait between each damage tick.")
    public int windowDamageTime = 20;

    @ConfigField(path = "fallback-location")
    public Location fallbackLocation = new Location(Bukkit.getWorld("world"), 0, 150, 0);

    @ConfigField(path = "reload-time", comment = "How many gameticks it takes to reload a gun.")
    public int reloadTime = 20;

}
