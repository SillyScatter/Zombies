package me.sllly.zombies.files.logics;

import com.octanepvp.splityosis.configsystem.configsystem.ConfigTypeLogic;
import me.sllly.zombies.mechanisms.setup.info.MobInfo;
import me.sllly.zombies.mechanisms.setup.singulars.SingularMobInfo;
import me.sllly.zombies.utils.Util;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class MobInfoLogic extends ConfigTypeLogic<MobInfo> {
    @Override
    public MobInfo getFromConfig(ConfigurationSection configurationSection, String s) {
        ConfigurationSection config = configurationSection.getConfigurationSection(s);
        Map<String, SingularMobInfo> mobInfoMap = new HashMap<>();
        if (config == null) {
            Util.log("&cError, no mob info found in config.");
            return null;
        }
        for (String key : config.getKeys(false)) {
            int normalMoney = config.getInt(key + ".normalMoney");
            int headShotMoney = config.getInt(key + ".headShotMoney");
            SingularMobInfo singularMobInfo = new SingularMobInfo(key, normalMoney, headShotMoney);
            mobInfoMap.put(key, singularMobInfo);
        }
        return new MobInfo(mobInfoMap);
    }

    @Override
    public void setInConfig(MobInfo mobInfo, ConfigurationSection configurationSection, String s) {
        ConfigurationSection config = configurationSection.createSection(s);
        for (String string : mobInfo.mobInfoHashMap().keySet()) {
            config.set(string + ".normalMoney", mobInfo.mobInfoHashMap().get(string).normalMoney());
            config.set(string + ".headShotMoney", mobInfo.mobInfoHashMap().get(string).headShotMoney());
        }
    }
}
