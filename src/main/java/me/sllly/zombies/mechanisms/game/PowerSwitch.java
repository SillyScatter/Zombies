package me.sllly.zombies.mechanisms.game;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.sllly.zombies.mechanisms.Game;
import org.bukkit.Location;

public class PowerSwitch {
    private final Game game;
    private final Hologram hologram;
    private boolean powered;

    public PowerSwitch(Game game) {
        this.game = game;
        Location hologramLocation = game.getGameTemplate().powerSwitchInfo().hologramLocation().clone();
        hologramLocation.setWorld(game.getWorld());
        hologram = DHAPI.createHologram("power-switch-"+game.getGameID(), hologramLocation, game.getGameTemplate().powerSwitchInfo().hologramText());
        powered = false;
    }

    public Game getGame() {
        return game;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public boolean isPowered() {
        return powered;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
        if (powered){
            hologram.destroy();
        }
    }
}
