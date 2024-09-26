package me.sllly.zombies.mechanisms.ability.weapon;

import me.sllly.zombies.Zombies;
import me.sllly.zombies.mechanisms.ZombiesPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RocketLaunchAbility implements AbstractWeaponAbility{
    @Override
    public String getId() {
        return "rocket_launch";
    }

    @Override
    public void onHit(Player player, LivingEntity entity, double d) {
        // Get the location of the source entity
        Location sourceLocation = entity.getLocation();
        double radius = Zombies.weaponAbilityConfig.rocketLauncherExplosionRadius;
        double knockbackStrength = Zombies.weaponAbilityConfig.rocketLauncherExplosionKnockbackStrength;
        double damage = Zombies.weaponAbilityConfig.rocketLauncherExplosionDamage;

        // Get all nearby entities within the specified radius
        for (Entity entity1 : entity.getNearbyEntities(radius, radius, radius)) {

            // Check if the entity is a mob (LivingEntity and instance of Mob)
            if (!(entity1 instanceof Mob mob)) {
                continue;
            }
            // Apply damage to the mob
            mob.damage(damage, player);

            // Calculate knockback direction and apply force
            Vector knockbackDirection = mob.getLocation().toVector().subtract(sourceLocation.toVector()).normalize();
            knockbackDirection.multiply(knockbackStrength); // Scale by knockback strength
            knockbackDirection.setY(0.5); // Give some upward force so they are knocked into the air

            mob.setVelocity(knockbackDirection);

            // Optional: Create an explosion effect without breaking blocks
            entity.getWorld().createExplosion(sourceLocation, 0.0F, false, false); // no damage, no block break
        }
    }
}
