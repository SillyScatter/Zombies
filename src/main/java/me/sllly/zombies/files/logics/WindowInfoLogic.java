package me.sllly.zombies.files.logics;

import com.octane.sllly.octaneitemregistry.util.Util;
import com.octanepvp.splityosis.configsystem.configsystem.ConfigTypeLogic;
import com.octanepvp.splityosis.configsystem.configsystem.logics.LocationConfigLogic;
import me.sllly.zombies.mechanisms.setup.info.WindowInfo;
import me.sllly.zombies.mechanisms.setup.singulars.SingularWindowInfo;
import me.sllly.zombies.objects.Area;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class WindowInfoLogic extends ConfigTypeLogic<WindowInfo> {
    @Override
    public WindowInfo getFromConfig(ConfigurationSection configurationSection, String s) {
        ConfigurationSection windowSection = configurationSection.getConfigurationSection(s);
        if (windowSection == null) {
            Util.log("&cWindow section is null");
            return null;
        }
        List<SingularWindowInfo> windowInfos = new ArrayList<>();
        LocationConfigLogic locationConfigLogic = new LocationConfigLogic();
        AreaLogic areaLogic = new AreaLogic();
        for (String key : windowSection.getKeys(false)) {
            ConfigurationSection windowInfoSection = windowSection.getConfigurationSection(key);
            Area area = areaLogic.getFromConfig(windowInfoSection, "area");
            String room = windowInfoSection.getString("room");
            Location spawn = locationConfigLogic.getFromConfig(windowInfoSection, "spawn");
            windowInfos.add(new SingularWindowInfo(area, room, spawn));
        }
        return new WindowInfo(windowInfos);
    }

    @Override
    public void setInConfig(WindowInfo windowInfo, ConfigurationSection configurationSection, String s) {
        ConfigurationSection windowSection = configurationSection.createSection(s);
        LocationConfigLogic locationConfigLogic = new LocationConfigLogic();
        AreaLogic areaLogic = new AreaLogic();
        int count = 0;
        for (SingularWindowInfo window : windowInfo.windows()) {
            windowSection.set(count + ".room", window.roomName());
            areaLogic.setInConfig(window.windowArea(), windowSection, count + ".area");
            locationConfigLogic.setInConfig(window.windowSpawnLocation(), windowSection, count + ".spawn");
        }
    }
}
