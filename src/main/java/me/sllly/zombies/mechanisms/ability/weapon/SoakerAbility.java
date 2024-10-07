package me.sllly.zombies.mechanisms.ability.weapon;

import com.octane.sllly.octaneitemregistry.util.Util;
import me.sllly.zombies.Zombies;
import org.bukkit.entity.*;

public class SoakerAbility implements AbstractWeaponAbility {
    @Override
    public String getId() {
        return "soaker";
    }
    @Override
    public void onHit(Player player, LivingEntity entity, double damage) {
        double additionalDamageMultiplier = Zombies.weaponAbilityConfig.soakerDamageMultiplier-1;
        if (additionalDamageMultiplier < 0){
            Util.log("&cError: Soaker damage multiplier is less than 1, so no additional damage will be dealt.");
            return;
        }
        double damageToDeal = damage * additionalDamageMultiplier;

        if (isNetherMob(entity)  || entity.isVisualFire()){
            entity.setVisualFire(false);
            entity.setFireTicks(0);

            Mob mob = (Mob) entity;
            mob.damage(damageToDeal, player);
        }
    }

    public boolean isNetherMob(Entity entity) {
        EntityType type = entity.getType();

        return switch (type) {
            case ZOMBIFIED_PIGLIN, GHAST, MAGMA_CUBE, BLAZE, WITHER_SKELETON, PIGLIN, PIGLIN_BRUTE, HOGLIN, STRIDER ->
                    true;
            default -> false;
        };
    }
}
