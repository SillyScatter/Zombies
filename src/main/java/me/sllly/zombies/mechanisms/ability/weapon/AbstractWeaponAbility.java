package me.sllly.zombies.mechanisms.ability.weapon;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface AbstractWeaponAbility {

    String getId();

    /*
     * Called on every living entity that the weapon hits
     */
    void onHit(Player player, LivingEntity entity, double damage);
}