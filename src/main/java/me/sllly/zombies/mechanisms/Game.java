package me.sllly.zombies.mechanisms;

import com.octanepvp.splityosis.octaneworldsapi.TaskStatus;
import com.octanepvp.splityosis.octaneworldsapi.exceptions.InvalidWorldName;
import me.sllly.zombies.Zombies;
import me.sllly.zombies.exceptions.InvalidTemplateSettingsException;
import me.sllly.zombies.mechanisms.game.*;
import me.sllly.zombies.mechanisms.setup.GameTemplate;
import me.sllly.zombies.mechanisms.setup.singulars.SingularDoorInfo;
import me.sllly.zombies.mechanisms.setup.singulars.SingularWindowInfo;
import me.sllly.zombies.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Game {

    private final UUID gameID;
    private final GameTemplate gameTemplate;
    private final Map<UUID, ZombiesPlayer> players;
    private final Set<String> openRooms;
    private final Set<Window> windows;
    private final Set<Window> activeWindows;
    private final Set<Door> closedDoors;
    private final PowerSwitch powerSwitch;
    private Round currentRound;
    private GameStatus gameStatus;
    private World world;

    private Location spawnLocation;

    public Game(GameTemplate gameTemplate) {
        this.gameID = UUID.randomUUID();
        this.gameTemplate = gameTemplate;
        this.players = new HashMap<>();
        windows = new HashSet<>();
        openRooms = new HashSet<>();
        activeWindows = new HashSet<>();
        closedDoors = new HashSet<>();
        powerSwitch = new PowerSwitch(this);
        this.currentRound = null;
        gameStatus = GameStatus.LOBBY;
        setUpWorld();

    }

    public void setUpWorld(){
        TaskStatus worldStatus = createWorld();

        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                if (worldStatus.isComplete()){
                    world = Bukkit.getWorld("zombies_"+gameTemplate.templateId()+"_"+gameID.toString());
                    setUpMechanisms();
                    cancel();
                    return;
                }
                count++;
                if (count > 100){
                    cancel();
                    Util.log("&4World setup timed out");
                }
            }
        }.runTaskTimer(Zombies.plugin, 1, 1);
    }

    public void setUpMechanisms(){
        fixLocations();
        setUpRooms();
        setUpWindows();
        setUpDoors();

        onSetupComplete();
    }

    private TaskStatus createWorld(){
        World template = Bukkit.getWorld(gameTemplate.templateWorldName());
        if (template == null)
            throw new InvalidTemplateSettingsException("Template world not found");
        try {
            return Zombies.octaneWorldsAPI.cloneWorld(template, "zombies_"+gameTemplate.templateId()+"_"+gameID.toString());
        } catch (InvalidWorldName e) {
            throw new RuntimeException(e);
        }
    }

    public void setUpRooms(){
        openRooms.add(gameTemplate.defaultRoomName());
    }

    public void setUpWindows() {
        for (SingularWindowInfo singularWindowInfo : gameTemplate.windowInfo().windows()) {
            Window window = new Window(singularWindowInfo.windowArea(), singularWindowInfo.roomName(), this, singularWindowInfo.windowSpawnLocation());
            windows.add(window);
        }
        windows.forEach(window -> {
            if (openRooms.contains(window.getRoomName())){
                window.activate();
                activeWindows.add(window);
            }
        });
    }

    public void setUpDoors(){
        Util.log("&9Setting up doors, there are "+gameTemplate.doorInfo().doors().size()+" doors.");
        for (SingularDoorInfo singularDoorInfo : gameTemplate.doorInfo().doors()) {
            Door door = new Door(this, singularDoorInfo);
            closedDoors.add(door);
        }
    }

    public void onSetupComplete(){
        Zombies.activeGames.put(gameID, this);
        scheduleStart();
    }

    public void addPlayer(Player player){
        ZombiesPlayer zombiesPlayer = new ZombiesPlayer(player);
        zombiesPlayer.initializeInventory(this);
        zombiesPlayer.setPlayerExp();
        players.put(player.getUniqueId(), zombiesPlayer);
        player.teleport(spawnLocation);
        player.setLevel(0);
        try {
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        }catch (Exception e){
            player.setHealth(20);
        }
        player.setFoodLevel(20);
        player.setGameMode(GameMode.ADVENTURE);
        players.values().forEach(zPlayer -> {
            Zombies.actionsConfig.playerJoinGame.perform(zPlayer.getPlayer(), Map.of("%player-name%", player.getName()));
        });
    }

    public void fixLocations(){
        spawnLocation = gameTemplate.spawnLocation().clone();
        spawnLocation.setWorld(world);
    }

    public void scheduleStart(){
        new BukkitRunnable(){
            @Override
            public void run() {
                if (players.isEmpty()){
                    end(GameCompletionReason.NO_PLAYERS);
                    return;
                }
                start();
            }
        }.runTaskLater(Zombies.plugin, 20*15);
    }

    public void end(){
        for (ZombiesPlayer value : players.values()) {
            value.getPlayer().teleport(Zombies.settingsConfig.fallbackLocation);
        }
        for (Window window : windows) {
            window.deactivate();
        }
        Zombies.activeGames.remove(gameID);
        try {
            Zombies.octaneWorldsAPI.deleteWorld(world, Zombies.settingsConfig.fallbackLocation);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void end(GameCompletionReason reason){
        switch (reason){
            case NO_PLAYERS -> end();
            case ALL_ROUNDS_COMPLETE -> {
                for (ZombiesPlayer value : players.values()) {
                    Zombies.actionsConfig.gameWinEnd.perform(value.getPlayer());
                    value.getPlayer().getInventory().clear();
                    value.getPlayer().getActivePotionEffects().clear();
                }
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        end();
                    }
                }.runTaskLater(Zombies.plugin, 20*10);
            }
        }
        //todo complete
    }

    public void start(){
        gameStatus = GameStatus.PLAYING;
        for (ZombiesPlayer value : players.values()) {
            value.getPlayer().teleport(spawnLocation);
        }
        Round.startRound(1, this);
    }

    public static Game getGame(UUID gameID){
        return Zombies.activeGames.get(gameID);
    }

    public static Game getGame(World world){
        String worldName = world.getName();
        String[] split = worldName.split("_");
        if (split.length < 3){
            return null;
        }
        try {
            return getGame(UUID.fromString(split[2]));
        }catch (Exception e){
            return null;
        }
    }

    public UUID getGameID() {
        return gameID;
    }

    public GameTemplate getGameTemplate() {
        return gameTemplate;
    }

    public Map<UUID, ZombiesPlayer> getPlayers() {
        return players;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public World getWorld() {
        return world;
    }

    public Set<String> getOpenRooms() {
        return openRooms;
    }

    public Set<Window> getWindows() {
        return windows;
    }

    public Set<Window> getActiveWindows() {
        return activeWindows;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Set<Door> getClosedDoors() {
        return closedDoors;
    }

    public PowerSwitch getPowerSwitch() {
        return powerSwitch;
    }
}
