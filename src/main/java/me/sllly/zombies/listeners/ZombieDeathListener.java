package me.sllly.zombies.listeners;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.sllly.zombies.mechanisms.Game;
import me.sllly.zombies.mechanisms.game.MobHandler;
import me.sllly.zombies.mechanisms.game.ZombiesPlayer;
import me.sllly.zombies.mechanisms.setup.singulars.SingularMobInfo;
import me.sllly.zombies.utils.Util;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
        String mobType = activeMob.getMobType();
        if (!game.getGameTemplate().mobInfo().mobInfoHashMap().containsKey(mobType)){
            Util.log("&cError: Mob killed during game not found in mob info: " + mobType);
            return;
        }
        SingularMobInfo singularMobInfo = game.getGameTemplate().mobInfo().mobInfoHashMap().get(mobType);
        LivingEntity killer = event.getKiller();
        if (killer != null){
            if (killer instanceof Player player){
                ZombiesPlayer zombiesPlayer = ZombiesPlayer.getZombiePlayer(player);
                if (zombiesPlayer != null) {
                    zombiesPlayer.addMoney(singularMobInfo.normalMoney());
                }
            }
        }
        game.getCurrentRound().onZombieDeath(activeMob.getUniqueId());
    }


}
