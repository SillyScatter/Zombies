package me.sllly.zombies.mechanisms.game;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.sllly.zombies.Zombies;
import me.sllly.zombies.exceptions.InvalidTemplateSettingsException;
import me.sllly.zombies.mechanisms.Game;
import me.sllly.zombies.mechanisms.setup.singulars.SingularRoundInfo;
import me.sllly.zombies.mechanisms.setup.singulars.TickSpawnInfo;
import me.sllly.zombies.utils.Util;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Round {
    private final Game game;
    private final int roundNumber;
    private final SingularRoundInfo singularRoundInfo;
    private final Set<UUID> activeZombies;
    int remainderToSpawn;

    public Round(int roundNumber, Game game) {
        this.game = game;
        this.roundNumber = roundNumber;
        activeZombies = new HashSet<>();
        int indexNumber = roundNumber-1;
        if (game.getGameTemplate().roundInfo().rounds().size() <= indexNumber){
            throw new InvalidTemplateSettingsException("Round number is higher than the amount of rounds in the game template");
        }
        singularRoundInfo = game.getGameTemplate().roundInfo().rounds().get(indexNumber);
        findTotalToSpawn();
    }

    public void findTotalToSpawn() {
        int count = 0;
        for (TickSpawnInfo value : singularRoundInfo.tickSpawns().values()) {
            for (Integer i : value.mobs().values()) {
                count += i;
            }
        }
        remainderToSpawn = count;
    }

    public static void startRound(int roundNumber, Game game) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%round-number%", roundNumber+"");
        for (ZombiesPlayer value : game.getPlayers().values()) {
            Zombies.actionsConfig.roundStart.perform(value.getPlayer(), placeholders);
        }
        int indexNumber = roundNumber-1;
        if (game.getGameTemplate().roundInfo().rounds().size() <= indexNumber){
            Util.log("&cError: Round number is higher than the amount of rounds in the game template");
            return;
        }
        Round round = new Round(roundNumber, game);
        round.startSchedule();
        game.setCurrentRound(round);
    }

    public void startSchedule(){
        singularRoundInfo.tickSpawns().forEach((tick, tickSpawnInfo) -> {
            new BukkitRunnable(){
                @Override
                public void run() {
                    if (game.getActiveWindows().isEmpty()){
                        Util.log("&cNo windows found to spawn zombies in");
                        return;
                    }
                    for (String mobId : tickSpawnInfo.mobs().keySet()) {
                        int amount = tickSpawnInfo.mobs().get(mobId);
                        MythicMob mob = MythicBukkit.inst().getMobManager().getMythicMob(mobId).orElse(null);
                        if (mob == null) {
                            Util.log("&cCould not find mob with id " + mobId);
                            continue;
                        }
                        for (int i = 0; i < amount; i++) {
                            spawnMob(mob);
                        }
                    }
                }
            }.runTaskLater(Zombies.plugin, tick);
        });
    }

    public void spawnMob(MythicMob mob){
        Window window = Util.getRandomEntry(game.getActiveWindows());
        MobHandler.spawnMob(mob, window.getWindowSpawnLocation(), game);
    }

    public void onZombieDeath(UUID zombieUUID){
        activeZombies.remove(zombieUUID);
        remainderToSpawn--;
        if (remainderToSpawn == 0 && activeZombies.isEmpty()){
            if (game.getGameTemplate().roundInfo().rounds().size() == roundNumber){
                game.end(GameCompletionReason.ALL_ROUNDS_COMPLETE);
                return;
            }
            Round.startRound(roundNumber+1, game);
        }
    }

    public Game getGame() {
        return game;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public SingularRoundInfo getSingularRoundInfo() {
        return singularRoundInfo;
    }

    public Set<UUID> getActiveZombies() {
        return activeZombies;
    }

    public int getRemainderToSpawn() {
        return remainderToSpawn;
    }
}
