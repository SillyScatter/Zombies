package me.sllly.zombies.files.logics;

import com.octanepvp.splityosis.configsystem.configsystem.ConfigTypeLogic;
import com.octanepvp.splityosis.configsystem.configsystem.logics.LocationConfigLogic;
import me.sllly.zombies.objects.Area;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class AreaLogic extends ConfigTypeLogic<Area> {
    @Override
    public Area getFromConfig(ConfigurationSection config, String path) {

        LocationConfigLogic locationConfigLogic = new LocationConfigLogic();

        Location cornerOne = locationConfigLogic.getFromConfig(config, path+".corner-one");
        Location cornerTwo = locationConfigLogic.getFromConfig(config, path+".corner-two");
        return new Area(cornerOne, cornerTwo);
    }

    @Override
    public void setInConfig(Area instance, ConfigurationSection config, String path) {

        LocationConfigLogic locationConfigLogic = new LocationConfigLogic();

        locationConfigLogic.setInConfig(instance.getCornerOne(), config, path+".corner-one");
        locationConfigLogic.setInConfig(instance.getCornerTwo(), config, path+".corner-two");
    }
}