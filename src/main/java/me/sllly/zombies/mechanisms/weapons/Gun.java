package me.sllly.zombies.mechanisms.weapons;

import com.octane.sllly.octaneitemregistry.objects.tools.SimpleTool;
import com.octane.sllly.octaneitemregistry.util.Util;
import me.sllly.zombies.Zombies;
import me.sllly.zombies.collections.WeaponUpgradeInfoList;
import me.sllly.zombies.mechanisms.ability.weapon.AbstractWeaponAbility;
import org.bukkit.FluidCollisionMode;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;

import java.util.HashSet;
import java.util.List;

public class Gun extends SimpleTool {

    public static NamespacedKey GUN_UPGRADE_LEVEL = new NamespacedKey(Zombies.plugin, "gun_upgrade_level");
    public static NamespacedKey GUN_CLIP_AMMO = new NamespacedKey(Zombies.plugin, "gun_clip_ammo");
    public static NamespacedKey GUN_CURRENT_AVAILABLE_AMMO = new NamespacedKey(Zombies.plugin, "gun_current_available_ammo");
    public static NamespacedKey GUN_RELOADING = new NamespacedKey(Zombies.plugin, "gun_reloading");

    private final String name;
    private final WeaponUpgradeInfoList upgradeInfo;
    private final List<String> rawInputAbilities;
    private final List<AbstractWeaponAbility> abilities;

    public Gun(String name, WeaponUpgradeInfoList upgradeInfo, List<String> rawInputAbilities, List<AbstractWeaponAbility> abilities) {
        super(name, upgradeInfo.get(0).itemStack());
        this.name = name;
        this.upgradeInfo = upgradeInfo;
        this.rawInputAbilities = rawInputAbilities;
        this.abilities = abilities;
    }

    @Override
    public void onInteract(Player player, ItemStack itemStack) {
        boolean reloading = isReloading(itemStack);
        if (reloading) {
            return;
        }
        int clipAmmo = getClipAmmo(itemStack);
        if (clipAmmo <= 0) {
            reloadGun(itemStack);
            return;
        }
        setClipAmmo(itemStack, getClipAmmo(itemStack) - 1);
        setCurrentAvailableAmmo(itemStack, getCurrentAvailableAmmo(itemStack) - 1);

        int gunUpgradeLevel = getUpgradeLevel(itemStack);
        if (gunUpgradeLevel >= upgradeInfo.size()) {
            Util.log("&cError: Gun upgrade level is higher than the max upgrade level.");
            return;
        }
        WeaponUpgradeInfo weaponUpgradeInfo = upgradeInfo.get(gunUpgradeLevel);
        int penetration = weaponUpgradeInfo.penetration();
        HashSet<LivingEntity> hitEntities = getHitEntities(player, penetration);
        for (LivingEntity entity : hitEntities) {
            double distance = player.getLocation().distance(entity.getLocation());
            double damage = weaponUpgradeInfo.distanceDamageFunction().setVariable("distance", distance).evaluate();
            for (AbstractWeaponAbility ability : abilities) {
                ability.onHit(player, entity, damage);
            }
            entity.damage(damage, player);
        }
    }

    @Override
    public boolean canInteract(Player player, ItemStack itemStack) {
        return true;
    }

    public int getUpgradeLevel(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return 0;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || !itemMeta.getPersistentDataContainer().has(GUN_UPGRADE_LEVEL, PersistentDataType.INTEGER)) {
            return itemMeta.getPersistentDataContainer().get(GUN_UPGRADE_LEVEL, PersistentDataType.INTEGER);
        }
        return 0;
    }

    public void setUpradeLevel(ItemStack itemStack, int level) {
        if (itemStack!=null && itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta!=null) {
                itemMeta.getPersistentDataContainer().set(GUN_UPGRADE_LEVEL, PersistentDataType.INTEGER, level);
                itemStack.setItemMeta(itemMeta);
            }
        }
    }

    public int getClipAmmo(ItemStack itemStack) {
//todo
        return 0;
    }

    public void setClipAmmo(ItemStack itemStack, int ammo) {
//todo
    }

    public int getCurrentAvailableAmmo(ItemStack itemStack) {
//todo
        return 0;
    }

    public void setCurrentAvailableAmmo(ItemStack itemStack, int ammo) {
//todo
    }

    public void reloadGun(ItemStack itemStack) {
//todo
    }

    public boolean isReloading(ItemStack itemStack) {
//todo
        return false;
    }

    public void setReloading(ItemStack itemStack) {
//todo
    }

    public HashSet<LivingEntity> getHitEntities(Player player, int penetration) {
        HashSet<LivingEntity> hitEntities = new HashSet<>();
        for (int i = 0; i < penetration; i++) {
            World world = player.getWorld();
            RayTraceResult result = world
                    .rayTrace(player.getEyeLocation(), player.getEyeLocation().getDirection(), 5,
                            FluidCollisionMode.NEVER, true, 0.05, (entity) -> {
                                if (!(entity instanceof LivingEntity)){
                                    return false;
                                }
                                return !hitEntities.contains(entity);
                            });
            if (result == null) {
                break;
            }
            Entity entity = result.getHitEntity();
            if (entity == null) {
                break;
            }
            hitEntities.add((LivingEntity) entity);
        }
        return hitEntities;
    }

    public String getName() {
        return name;
    }

    public WeaponUpgradeInfoList getUpgradeInfo() {
        return upgradeInfo;
    }

    public List<AbstractWeaponAbility> getAbilities() {
        return abilities;
    }

    public List<String> getRawInputAbilities() {
        return rawInputAbilities;
    }
}
