package me.sllly.zombies.listeners;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.sllly.zombies.mechanisms.Game;
import me.sllly.zombies.mechanisms.game.MobHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ZombieDeathListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onMythicMobDeath(MythicMobDeathEvent event) {
        ActiveMob activeMob = event.getMob();
        Game game = MobHandler.getGameFromMob(activeMob);
        if (game == null) {
            return;
        }
        game.getCurrentRound().onZombieDeath(activeMob.getUniqueId());
    }


}
