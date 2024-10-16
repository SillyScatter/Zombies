package me.sllly.zombies.files.logics;

import com.octanepvp.splityosis.configsystem.configsystem.ConfigTypeLogic;
import com.octanepvp.splityosis.configsystem.configsystem.logics.LocationConfigLogic;
import me.sllly.zombies.mechanisms.setup.info.PowerSwitchInfo;
import org.bukkit.configuration.ConfigurationSection;

public class PowerSwitchLogic extends ConfigTypeLogic<PowerSwitchInfo> {
    @Override
    public PowerSwitchInfo getFromConfig(ConfigurationSection configurationSection, String s) {
        LocationConfigLogic locationConfigLogic = new LocationConfigLogic();
        return new PowerSwitchInfo(
                locationConfigLogic.getFromConfig(configurationSection, s+".switch-location"),
                configurationSection.getInt(s+".cost"),
                configurationSection.getStringList(s+".hologram-text"),
                locationConfigLogic.getFromConfig(configurationSection, s+".hologram-location"),
                configurationSection.getString(s+".room")
        );
    }

    @Override
    public void setInConfig(PowerSwitchInfo powerSwitchInfo, ConfigurationSection configurationSection, String s) {
        LocationConfigLogic locationConfigLogic = new LocationConfigLogic();
        locationConfigLogic.setInConfig(powerSwitchInfo.hologramLocation(), configurationSection, s+".hologram-location");
        configurationSection.set(s+".hologram-text", powerSwitchInfo.hologramText());
        locationConfigLogic.setInConfig(powerSwitchInfo.switchLocation(), configurationSection, s+".switch-location");
        configurationSection.set(s+".cost", powerSwitchInfo.cost());
        configurationSection.set(s+".room", powerSwitchInfo.roomName());
    }
}
