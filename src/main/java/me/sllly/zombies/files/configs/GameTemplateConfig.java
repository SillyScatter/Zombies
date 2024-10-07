package me.sllly.zombies.files.configs;

import com.octanepvp.splityosis.configsystem.configsystem.AnnotatedConfig;
import com.octanepvp.splityosis.configsystem.configsystem.ConfigField;
import com.octanepvp.splityosis.configsystem.configsystem.InvalidConfigFileException;
import me.sllly.zombies.mechanisms.setup.info.RoundInfo;
import me.sllly.zombies.mechanisms.setup.info.WindowInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.util.List;

public class GameTemplateConfig extends AnnotatedConfig {
    public GameTemplateConfig(File file) throws InvalidConfigFileException {
        super(file);
    }

    @ConfigField(path = "template-id")
    public String templateId = "default";

    @ConfigField(path = "template-world-name")
    public String templateWorldName = "zombies";

    @ConfigField(path = "room-names")
    public List<String> roomNames = List.of("main", "progress");

    @ConfigField(path = "default-room-name")
    public String defaultRoomName = "main";

    @ConfigField(path = "default-gun-name")
    public String defaultGunName = "pistol";

    @ConfigField(path = "spawn-location")
    public Location spawnLocation = new Location(Bukkit.getWorld("zombies"), 0.5, 101, 0.5);

    @ConfigField(path = "window-info")
    public WindowInfo windowInfo = WindowInfo.getDefaultWindowInfo();

    @ConfigField(path = "round-info")
    public RoundInfo roundInfo = RoundInfo.getDefault();

    @ConfigField(path = "max-players")
    public int maxPlayers = 4;
}
