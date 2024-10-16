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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
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

    public Gun(String name, WeaponUpgradeInfoList upgradeInfo, List<String> rawInputAbilities) {
        super(name, upgradeInfo.get(0).itemStack());
        this.name = name;
        this.upgradeInfo = upgradeInfo;
        if (this.upgradeInfo.isEmpty()){
          this.upgradeInfo.add(WeaponUpgradeInfo.getDefault());
        }
        this.rawInputAbilities = rawInputAbilities;
        abilities = findAbilities();
    }

    private List<AbstractWeaponAbility> findAbilities() {
        List<AbstractWeaponAbility> abilities = new ArrayList<>();
        for (String abilityId : rawInputAbilities) {
            AbstractWeaponAbility ability = Zombies.weaponAbilities.get(abilityId);
            if (ability == null) {
                Util.log("&cError: Ability with id " + abilityId + " not found.");
                continue;
            }
            abilities.add(ability);
        }
        return abilities;
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
        int availableAmmo = getCurrentAvailableAmmo(itemStack);
        if (availableAmmo <= 0) {
            Zombies.actionsConfig.outOfAmmo.perform(player, null);
            return;
        }
        availableAmmo--;
        setClipAmmo(itemStack, getClipAmmo(itemStack) - 1);
        clipAmmo--;
        setCurrentAvailableAmmo(itemStack, availableAmmo);
        player.setLevel(availableAmmo);
        itemStack.setAmount(clipAmmo);

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
        if (clipAmmo == 0) {
            reloadGun(itemStack);
        }
    }

    @Override
    public boolean canInteract(Player player, ItemStack itemStack) {
        return true;
    }

    @Override
    public ItemStack getFunctionalItemStack() {
        ItemStack itemStack = super.getFunctionalItemStack();
        setUpradeLevel(itemStack, 0);
        setClipAmmo(itemStack, upgradeInfo.get(0).clipAmmo());
        setCurrentAvailableAmmo(itemStack, upgradeInfo.get(0).maxAmmo());
        itemStack.setAmount(upgradeInfo.get(0).clipAmmo());
        return itemStack;
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
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return 0;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || !itemMeta.getPersistentDataContainer().has(GUN_CLIP_AMMO, PersistentDataType.INTEGER)) {
            return 0;
        }
        return itemMeta.getPersistentDataContainer().get(GUN_CLIP_AMMO, PersistentDataType.INTEGER);
    }

    public void setClipAmmo(ItemStack itemStack, int ammo) {
        if (itemStack != null && itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.getPersistentDataContainer().set(GUN_CLIP_AMMO, PersistentDataType.INTEGER, ammo);
                itemStack.setItemMeta(itemMeta);
            }
        }
    }

    public int getCurrentAvailableAmmo(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return 0;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || !itemMeta.getPersistentDataContainer().has(GUN_CURRENT_AVAILABLE_AMMO, PersistentDataType.INTEGER)) {
            return 0;
        }
        return itemMeta.getPersistentDataContainer().get(GUN_CURRENT_AVAILABLE_AMMO, PersistentDataType.INTEGER);
    }

    public void setCurrentAvailableAmmo(ItemStack itemStack, int ammo) {
        if (itemStack != null && itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.getPersistentDataContainer().set(GUN_CURRENT_AVAILABLE_AMMO, PersistentDataType.INTEGER, ammo);
                itemStack.setItemMeta(itemMeta);
            }
        }
    }

    @Override
    public ItemStack getNonFunctionalItemStack() {
        ItemStack itemStack = super.getNonFunctionalItemStack();
        itemStack.setAmount(upgradeInfo.get(0).clipAmmo());
        return itemStack;
    }

    public void reloadGun(ItemStack itemStack) {
        setReloading(itemStack, true);
        itemStack.setAmount(1);
        int upgradeLevel = getUpgradeLevel(itemStack);
        if (upgradeLevel >= upgradeInfo.size()) {
            Util.log("&cError: Gun upgrade level is higher than the max upgrade level.");
            return;
        }
        WeaponUpgradeInfo weaponUpgradeInfo = upgradeInfo.get(upgradeLevel);
        int clipSize = weaponUpgradeInfo.clipAmmo();
        int reloadTicks = Zombies.settingsConfig.reloadTime;
        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                itemStack.setAmount(getStackSizeAtTick(count, reloadTicks, clipSize));
                count++;
                if (count == (reloadTicks)) {
                    setClipAmmo(itemStack, clipSize);
                    setReloading(itemStack, false);
                    cancel();
                }
            }
        }.runTaskTimer(Zombies.plugin, 0, 1);
    }

    public int getStackSizeAtTick(int tick, int reloadTicks, int clipSize) {
        return (int) Math.ceil((double) clipSize / reloadTicks * tick);
    }

    public boolean isReloading(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || !itemMeta.getPersistentDataContainer().has(GUN_RELOADING, PersistentDataType.BOOLEAN)) {
            return false;
        }
        return itemMeta.getPersistentDataContainer().get(GUN_RELOADING, PersistentDataType.BOOLEAN);
    }

    public void setReloading(ItemStack itemStack, boolean reloading) {
        if (itemStack != null && itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.getPersistentDataContainer().set(GUN_RELOADING, PersistentDataType.BOOLEAN, reloading);
                itemStack.setItemMeta(itemMeta);
            }
        }
    }

    public HashSet<LivingEntity> getHitEntities(Player player, int penetration) {
        HashSet<LivingEntity> hitEntities = new HashSet<>();
        for (int i = 0; i < penetration; i++) {
            World world = player.getWorld();
            RayTraceResult result = world
                    .rayTrace(player.getEyeLocation(), player.getEyeLocation().getDirection(), 100,
                            FluidCollisionMode.NEVER, true, 0.05, (entity) -> {
                                if (!(entity instanceof LivingEntity)){
                                    return false;
                                }
                                if (entity instanceof Player){
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
