package me.sllly.zombies.mechanisms.ability.weapon;

import me.sllly.zombies.Zombies;
import me.sllly.zombies.mechanisms.ZombiesPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class SetOnFireAbility implements AbstractWeaponAbility{
    @Override
    public String getId() {
        return "set_on_fire";
    }
    @Override
    public void onHit(Player player, LivingEntity entity, double damage) {
        entity.setVisualFire(true);
        entity.setFireTicks(Zombies.weaponAbilityConfig.setOnFireDuration);
    }
}
