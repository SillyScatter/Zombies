package me.sllly.zombies.files.logics;

import com.octanepvp.splityosis.configsystem.configsystem.ConfigTypeLogic;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.sllly.zombies.mechanisms.setup.info.RoundInfo;
import me.sllly.zombies.mechanisms.setup.singulars.SingularRoundInfo;
import me.sllly.zombies.mechanisms.setup.singulars.TickSpawnInfo;
import me.sllly.zombies.utils.Util;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoundInfoLogic extends ConfigTypeLogic<RoundInfo> {
    @Override
    public RoundInfo getFromConfig(ConfigurationSection configurationSection, String s) {
        RoundInfo roundInfo = new RoundInfo(new ArrayList<>());
        ConfigurationSection roundInfoSection = configurationSection.getConfigurationSection(s);
        if (roundInfoSection == null) {
            return roundInfo;
        }
        for (String roundNumber : roundInfoSection.getKeys(false)) {
            SingularRoundInfo singularRoundInfo = new SingularRoundInfo(Integer.parseInt(roundNumber), new HashMap<>());
            ConfigurationSection singularRoundInfoSection = roundInfoSection.getConfigurationSection(roundNumber);
            if (singularRoundInfoSection == null) {
                continue;
            }
            for (String tick : singularRoundInfoSection.getKeys(false)) {
                TickSpawnInfo tickSpawnInfo = new TickSpawnInfo(new HashMap<>());
                ConfigurationSection tickSpawnInfoSection = singularRoundInfoSection.getConfigurationSection(tick);
                if (tickSpawnInfoSection == null) {
                    continue;
                }
                for (String mobId : tickSpawnInfoSection.getKeys(false)) {
                    MythicMob mob = MythicBukkit.inst().getMobManager().getMythicMob(mobId).orElse(null);
                    if (mob == null) {
                        Util.log("&cCould not find mob with id " + mobId);
                        continue;
                    }
                    int amount = tickSpawnInfoSection.getInt(mobId);
                    tickSpawnInfo.mobs().put(mobId, amount);
                }
                singularRoundInfo.tickSpawns().put(Integer.parseInt(tick), tickSpawnInfo);
            }
            roundInfo.rounds().add(singularRoundInfo);
        }
        return roundInfo;
    }

    @Override
    public void setInConfig(RoundInfo roundInfo, ConfigurationSection configurationSection, String s) {
        ConfigurationSection roundInfoSection = configurationSection.createSection(s);
        List<SingularRoundInfo> rounds = roundInfo.rounds();
        int roundNumber = 0;
        for (SingularRoundInfo singularRoundInfo : rounds) {
            roundNumber++;
            ConfigurationSection singularRoundInfoSection = roundInfoSection.createSection(roundNumber+"");
            for (Integer tick : singularRoundInfo.tickSpawns().keySet()) {
                TickSpawnInfo tickSpawnInfo = singularRoundInfo.tickSpawns().get(tick);
                ConfigurationSection tickSpawnInfoSection = singularRoundInfoSection.createSection(tick.toString());
                for (String mobId : tickSpawnInfo.mobs().keySet()) {
                    int amount = tickSpawnInfo.mobs().get(mobId);
                    tickSpawnInfoSection.set(mobId, amount);
                }
            }
        }
    }
}
