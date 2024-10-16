package me.sllly.zombies.files.logics;

import com.octanepvp.splityosis.configsystem.configsystem.ConfigTypeLogic;
import com.octanepvp.splityosis.configsystem.configsystem.logics.LocationConfigLogic;
import me.sllly.zombies.mechanisms.setup.info.PackAPunchInfo;
import org.bukkit.configuration.ConfigurationSection;

public class PackAPunchLogic extends ConfigTypeLogic<PackAPunchInfo> {
    @Override
    public PackAPunchInfo getFromConfig(ConfigurationSection configurationSection, String s) {
        LocationConfigLogic locationConfigLogic = new LocationConfigLogic();
        return new PackAPunchInfo(
                locationConfigLogic.getFromConfig(configurationSection, s+".pack-a-punch-location"),
                configurationSection.getInt(s+".cost"),
                configurationSection.getStringList(s+".hologram-text"),
                locationConfigLogic.getFromConfig(configurationSection, s+".hologram-location"),
                configurationSection.getString(s+".room")
        );
    }

    @Override
    public void setInConfig(PackAPunchInfo packAPunchInfo, ConfigurationSection configurationSection, String s) {
        LocationConfigLogic locationConfigLogic = new LocationConfigLogic();
        locationConfigLogic.setInConfig(packAPunchInfo.hologramLocation(), configurationSection, s+".hologram-location");
        configurationSection.set(s+".hologram-text", packAPunchInfo.hologramText());
        locationConfigLogic.setInConfig(packAPunchInfo.packAPunchLocation(), configurationSection, s+".pack-a-punch-location");
        configurationSection.set(s+".cost", packAPunchInfo.cost());
        configurationSection.set(s+".room", packAPunchInfo.roomName());
    }
}
