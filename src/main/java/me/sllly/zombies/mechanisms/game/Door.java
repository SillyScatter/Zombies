package me.sllly.zombies.mechanisms.game;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.sllly.zombies.Zombies;
import me.sllly.zombies.mechanisms.Game;
import me.sllly.zombies.mechanisms.setup.singulars.SingularDoorInfo;
import me.sllly.zombies.objects.Area;
import me.sllly.zombies.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Door {
    private final Game game;
    private final SingularDoorInfo doorInfo;
    private final Hologram hologram1;
    private final Hologram hologram2;

    public Door(Game game, SingularDoorInfo doorInfo) {
        this.game = game;
        this.doorInfo = doorInfo;
        Location hologram1SpawnLocation = doorInfo.hologram1Location().clone();
        hologram1SpawnLocation.setWorld(game.getWorld());
        hologram1 = DHAPI.createHologram(findHologramID(1), hologram1SpawnLocation, Util.replaceList(doorInfo.hologramText(), "%room-name%", doorInfo.room2()));
        Location hologram2SpawnLocation = doorInfo.hologram2Location().clone();
        hologram2SpawnLocation.setWorld(game.getWorld());
        hologram2 = DHAPI.createHologram(findHologramID(2), hologram2SpawnLocation, Util.replaceList(doorInfo.hologramText(), "%room-name%", doorInfo.room1()));
        game.getClosedDoors().add(this);
    }

    public String findHologramID(int doorNumber) {
        return game.getGameID().toString() + "-door"+doorNumber;
    }

    public void open(String openedRoom){
        hologram1.destroy();
        hologram2.destroy();
        Area doorArea = new Area(doorInfo.doorArea().getCornerOne(), doorInfo.doorArea().getCornerTwo());
        doorArea.setWorld(game.getWorld());

        //Without scheduling on main thread, code will run async and won't work.
        Bukkit.getScheduler().runTask(Zombies.plugin, () -> {
            for (Block block : doorArea.getBlocksWithinArea()) {
                block.setType(Material.AIR);
            }
        });

        game.getClosedDoors().remove(this);
        game.getOpenRooms().add(openedRoom);
    }

    public static Door getDoor(Hologram hologram){
        World world = hologram.getLocation().getWorld();
        if (world == null) {
            return null;
        }
        Game game = Game.getGame(world);
        if (game == null) {
            return null;
        }
        for (Door door : game.getClosedDoors()){
            if (door.hologram1.equals(hologram) || door.hologram2.equals(hologram)){
                return door;
            }
        }
        return null;
    }

    public void onDoorClick(ZombiesPlayer zombiesPlayer, Hologram hologram){
        int cost = doorInfo.cost();
        if (!zombiesPlayer.hasEnoughMoney(cost)){
            Zombies.actionsConfig.notEnoughMoney.perform(zombiesPlayer.getPlayer());
            return;
        }
        int hologramNumber = 0;
        if (hologram1.equals(hologram)){
            hologramNumber = 1;
        } else if (hologram2.equals(hologram)){
            hologramNumber = 2;
        }
        if (hologramNumber == 0){
            Util.log("&cError -> Failed to find hologram number");
            return;
        }
        String roomOpenedFrom = hologramNumber == 1 ? doorInfo.room1() : doorInfo.room2();
        String openedRoom = hologramNumber == 1 ? doorInfo.room2() : doorInfo.room1();
        if (!game.getOpenRooms().contains(roomOpenedFrom)){
            Util.log("&cError -> Hologram clicked from room that is not open");
            return;
        }
        zombiesPlayer.removeMoney(cost);
        open(openedRoom);
    }

    public Game getGame() {
        return game;
    }

    public SingularDoorInfo getDoorInfo() {
        return doorInfo;
    }

    public Hologram getHologram1() {
        return hologram1;
    }

    public Hologram getHologram2() {
        return hologram2;
    }
}
