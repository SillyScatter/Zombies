package me.sllly.zombies.files.logics;

import com.octanepvp.splityosis.configsystem.configsystem.ConfigTypeLogic;
import com.octanepvp.splityosis.configsystem.configsystem.logics.ItemStackConfigLogic;
import com.octanepvp.splityosis.octaneengine.function.Function;
import com.octanepvp.splityosis.octaneengine.function.logics.FunctionConfigLogic;
import me.sllly.zombies.collections.WeaponUpgradeInfoList;
import me.sllly.zombies.mechanisms.weapons.WeaponUpgradeInfo;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class WeaponUpgradeInfoListLogic extends ConfigTypeLogic<WeaponUpgradeInfoList> {
    @Override
    public WeaponUpgradeInfoList getFromConfig(ConfigurationSection configurationSection, String s) {
        WeaponUpgradeInfoList weaponUpgradeInfos = new WeaponUpgradeInfoList();
        ItemStackConfigLogic itemStackConfigLogic = new ItemStackConfigLogic();
        FunctionConfigLogic functionConfigLogic = new FunctionConfigLogic();

        if (!configurationSection.contains(s)) {
            return weaponUpgradeInfos;
        }
        ConfigurationSection config = configurationSection.getConfigurationSection(s);

        for (String key : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(key);
            Function distanceDamageFunction = functionConfigLogic.getFromConfig(section, "distance-damage-function");
            int penetration = section.getInt("penetration");
            int clipAmmo = section.getInt("clip-ammo");
            int maxAmmo = section.getInt("max-ammo");
            int cooldown = section.getInt("cooldown");
            ItemStack itemStack = itemStackConfigLogic.getFromConfig(section, "item-stack");

            weaponUpgradeInfos.add(new WeaponUpgradeInfo(distanceDamageFunction, penetration, clipAmmo, maxAmmo, cooldown, itemStack));
        }
        return weaponUpgradeInfos;
    }

    @Override
    public void setInConfig(WeaponUpgradeInfoList weaponUpgradeInfos, ConfigurationSection configurationSection, String s) {
        configurationSection.createSection(s);
        ItemStackConfigLogic itemStackConfigLogic = new ItemStackConfigLogic();
        FunctionConfigLogic functionConfigLogic = new FunctionConfigLogic();
        int count = 0;

        for (WeaponUpgradeInfo weaponUpgradeInfo : weaponUpgradeInfos) {
            ConfigurationSection section = configurationSection.createSection(s + "." + pathSection(count));
            functionConfigLogic.setInConfig(weaponUpgradeInfo.distanceDamageFunction(), section, "distance-damage-function");
            section.set("penetration", weaponUpgradeInfo.penetration());
            section.set("clip-ammo", weaponUpgradeInfo.clipAmmo());
            section.set("max-ammo", weaponUpgradeInfo.maxAmmo());
            section.set("cooldown", weaponUpgradeInfo.cooldown());
            itemStackConfigLogic.setInConfig(weaponUpgradeInfo.itemStack(), section, "item-stack");

            count++;
        }
    }

    public String pathSection(int index){
        if (index == 0) {
            return "no-upgrades";
        }
        return "upgrade-" + index;
    }
}
