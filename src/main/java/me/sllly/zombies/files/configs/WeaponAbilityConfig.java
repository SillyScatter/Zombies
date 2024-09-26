package me.sllly.zombies.files.configs;

import com.octanepvp.splityosis.configsystem.configsystem.AnnotatedConfig;
import com.octanepvp.splityosis.configsystem.configsystem.ConfigField;

import java.io.File;

public class WeaponAbilityConfig extends AnnotatedConfig {
    public WeaponAbilityConfig(File parentDirectory, String name) {
        super(parentDirectory, name);
    }

    @ConfigField(path = "stun.duration", comment = "Duration in ticks")
    public int stunDuration = 10;

    @ConfigField(path = "set-on-fire.duration", comment = "Duration in ticks")
    public int setOnFireDuration = 100;

    @ConfigField(path = "rocket-launcher.explosion-radius")
    public double rocketLauncherExplosionRadius = 5.0;

    @ConfigField(path = "rocket-launcher.explosion-damage")
    public double rocketLauncherExplosionDamage = 30.0;

    @ConfigField(path = "rocket-launcher.explosion-knockback-strength")
    public double rocketLauncherExplosionKnockbackStrength = 0.3;

    @ConfigField(path = "soaker.damage-multiplier", comment = "Use a value above 1.0")
    public double soakerDamageMultiplier = 4.0;

    @ConfigField(path = "poison.duration", comment = "Duration in ticks")
    public int poisonDuration = 100;
}
