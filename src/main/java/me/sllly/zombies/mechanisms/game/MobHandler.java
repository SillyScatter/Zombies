package me.sllly.zombies.mechanisms.game;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.sllly.zombies.Zombies;
import me.sllly.zombies.mechanisms.Game;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class MobHandler {
    public static NamespacedKey zombieKey = new NamespacedKey(Zombies.plugin, "zombies");

    public static void spawnMob(MythicMob mob, Location location, Game game){
        ActiveMob activeMob = mob.spawn(BukkitAdapter.adapt(location),1);
        activeMob.getEntity().getDataContainer().set(zombieKey, PersistentDataType.STRING ,game.getGameID().toString());
        game.getCurrentRound().getActiveZombies().add(activeMob.getEntity().getBukkitEntity().getUniqueId());
    }

    public static Game getGameFromMob(ActiveMob activeMob){
        if (!activeMob.getEntity().getDataContainer().has(zombieKey, PersistentDataType.STRING)){
            return null;
        }
        return Zombies.activeGames.get(UUID.fromString(activeMob.getEntity().getDataContainer().get(zombieKey, PersistentDataType.STRING)));
    }
}
