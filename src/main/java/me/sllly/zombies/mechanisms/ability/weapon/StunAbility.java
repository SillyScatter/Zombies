package me.sllly.zombies.mechanisms.ability.weapon;

import me.sllly.zombies.Zombies;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StunAbility implements AbstractWeaponAbility{

    @Override
    public String getId() {
        return "stun";
    }

    @Override
    public void onHit(Player player, LivingEntity entity, double damage) {
        Location location = entity.getLocation();
        int count = 0;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (count >= Zombies.weaponAbilityConfig.stunDuration) {
                    cancel();
                    return;
                }
                entity.teleport(location);
            }
        }.runTaskTimer(Zombies.plugin, 0, 1);
    }
}
