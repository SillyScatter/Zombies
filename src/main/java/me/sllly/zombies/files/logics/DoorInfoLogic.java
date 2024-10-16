package me.sllly.zombies.files.logics;

import com.octanepvp.splityosis.configsystem.configsystem.ConfigTypeLogic;
import com.octanepvp.splityosis.configsystem.configsystem.logics.LocationConfigLogic;
import me.sllly.zombies.mechanisms.setup.info.DoorInfo;
import me.sllly.zombies.mechanisms.setup.singulars.SingularDoorInfo;
import me.sllly.zombies.objects.Area;
import me.sllly.zombies.utils.Util;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class DoorInfoLogic extends ConfigTypeLogic<DoorInfo> {
    @Override
    public DoorInfo getFromConfig(ConfigurationSection configurationSection, String s) {
        LocationConfigLogic locationConfigLogic = new LocationConfigLogic();
        AreaLogic areaLogic = new AreaLogic();
        List<SingularDoorInfo> doorInfos = new ArrayList<>();
        ConfigurationSection config = configurationSection.getConfigurationSection(s);
        if (config == null){
            Util.log("&cError: No registered doors for this template.");
            return new DoorInfo(doorInfos);
        }
        for (String key : config.getKeys(false)) {
            ConfigurationSection doorSection = config.getConfigurationSection(key);
            if (doorSection == null) {
                Util.log("&cError: "+key+" section is not a valid door section");
                continue;
            }
            String room1 = doorSection.getString("room1");
            String room2 = doorSection.getString("room2");
            int cost = doorSection.getInt("cost");
            List<String> hologramText = doorSection.getStringList("hologram-text");
            Location hologram1Location = locationConfigLogic.getFromConfig(doorSection, "hologram1-location");
            Location hologram2Location = locationConfigLogic.getFromConfig(doorSection, "hologram2-location");
            Area doorArea = areaLogic.getFromConfig(doorSection, "door-area");
            doorInfos.add(new SingularDoorInfo(room1, room2, cost, hologramText, hologram1Location, hologram2Location, doorArea));
        }
        Util.log("Loaded "+doorInfos.size()+" doors.");
        return new DoorInfo(doorInfos);
    }

    @Override
    public void setInConfig(DoorInfo doorInfo, ConfigurationSection configurationSection, String s) {
        LocationConfigLogic locationConfigLogic = new LocationConfigLogic();
        AreaLogic areaLogic = new AreaLogic();
        for (SingularDoorInfo door : doorInfo.doors()) {
            ConfigurationSection doorSection = configurationSection.createSection(s+"."+door.getId());
            doorSection.set("room1", door.room1());
            doorSection.set("room2", door.room2());
            doorSection.set("cost", door.cost());
            doorSection.set("hologram-text", door.hologramText());
            locationConfigLogic.setInConfig(door.hologram1Location(), doorSection, "hologram1-location");
            locationConfigLogic.setInConfig(door.hologram2Location(), doorSection, "hologram2-location");
            areaLogic.setInConfig(door.doorArea(), doorSection, "door-area");
        }
    }
}
