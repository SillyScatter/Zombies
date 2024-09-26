package me.sllly.zombies;

import me.sllly.zombies.files.configs.WeaponAbilityConfig;
import me.sllly.zombies.mechanisms.ability.weapon.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public final class Zombies extends JavaPlugin {

    public static Zombies plugin;
    public static WeaponAbilityConfig weaponAbilityConfig;
    public static Map<String, AbstractWeaponAbility> weaponAbilities;

    @Override
    public void onEnable() {
        plugin = this;

        reloadConfigs();
        registerWeaponAbilities();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadConfigs(){
        weaponAbilityConfig = new WeaponAbilityConfig(getDataFolder(), "weapon-abilities.yml");
        try {
            weaponAbilityConfig.reload();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void registerWeaponAbilities(){
        PoisonAbility poisonAbility = new PoisonAbility();
        weaponAbilities.put(poisonAbility.getId(), poisonAbility);
        RocketLaunchAbility rocketLaunchAbility = new RocketLaunchAbility();
        weaponAbilities.put(rocketLaunchAbility.getId(), rocketLaunchAbility);
        SetOnFireAbility setOnFireAbility = new SetOnFireAbility();
        weaponAbilities.put(setOnFireAbility.getId(), setOnFireAbility);
        SoakerAbility soakerAbility = new SoakerAbility();
        weaponAbilities.put(soakerAbility.getId(), soakerAbility);
        StunAbility stunAbility = new StunAbility();
        weaponAbilities.put(stunAbility.getId(), stunAbility);
    }
}
