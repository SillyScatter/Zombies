package me.sllly.zombies.mechanisms.ability.weapon;

import me.sllly.zombies.Zombies;
import me.sllly.zombies.mechanisms.ZombiesPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonAbility implements AbstractWeaponAbility {

    private final PotionEffect poisonEffect;

    public PoisonAbility() {
        this.poisonEffect = new PotionEffect(PotionEffectType.POISON, Zombies.weaponAbilityConfig.poisonDuration, 1);
    }

    @Override
    public String getId() {
        return "poison";
    }

    @Override
    public void onHit(Player player, LivingEntity entity, double damage) {
        entity.addPotionEffect(poisonEffect);
    }
}
