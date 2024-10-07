package me.sllly.zombies.files.logics;

import com.octanepvp.splityosis.configsystem.configsystem.ConfigTypeLogic;
import me.sllly.zombies.collections.GunMap;
import me.sllly.zombies.collections.WeaponUpgradeInfoList;
import me.sllly.zombies.mechanisms.weapons.Gun;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class GunMapConfigLogic extends ConfigTypeLogic<GunMap> {
    @Override
    public GunMap getFromConfig(ConfigurationSection configurationSection, String s) {
        ConfigurationSection config = configurationSection.getConfigurationSection(s);
        GunMap gunMap = new GunMap();
        if (config == null) {
            return gunMap;
        }
        WeaponUpgradeInfoListLogic weaponUpgradeInfoListLogic = new WeaponUpgradeInfoListLogic();
        for (String key : config.getKeys(false)) {
            WeaponUpgradeInfoList weaponUpgradeInfoList = weaponUpgradeInfoListLogic.getFromConfig(config, key+".upgrades");
            List<String> abilities = config.getStringList(key+".abilities");

            Gun gun = new Gun(key, weaponUpgradeInfoList, abilities);
            gunMap.put(key, gun);
        }
        return gunMap;
    }

    @Override
    public void setInConfig(GunMap gunMap, ConfigurationSection configurationSection, String s) {
        WeaponUpgradeInfoListLogic weaponUpgradeInfoListLogic = new WeaponUpgradeInfoListLogic();
        for (String string : gunMap.keySet()) {
            Gun gun = gunMap.get(string);
            ConfigurationSection section = configurationSection.createSection(s + "." + string);
            weaponUpgradeInfoListLogic.setInConfig(gun.getUpgradeInfo(), section, "upgrades");
            section.set("abilities", gun.getRawInputAbilities());
        }
    }
}
