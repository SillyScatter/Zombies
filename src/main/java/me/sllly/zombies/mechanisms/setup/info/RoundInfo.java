package me.sllly.zombies.mechanisms.setup.info;

import me.sllly.zombies.mechanisms.setup.singulars.SingularRoundInfo;
import me.sllly.zombies.mechanisms.setup.singulars.TickSpawnInfo;

import java.util.List;
import java.util.Map;

public record RoundInfo(List<SingularRoundInfo> rounds) {

    public static RoundInfo getDefault(){
        Map<String, Integer> tick200 = Map.of("Skeleton_Warrior", 3);
        TickSpawnInfo tickSpawnInfo200 = new TickSpawnInfo(tick200);
        Map<String, Integer> tick400 = Map.of("Skeleton_Archer", 2);
        TickSpawnInfo tickSpawnInfo400 = new TickSpawnInfo(tick400);
        Map<Integer, TickSpawnInfo> tickSpawns = Map.of(200, tickSpawnInfo200, 400, tickSpawnInfo400);
        SingularRoundInfo round1 = new SingularRoundInfo(1, tickSpawns);
        SingularRoundInfo round2 = new SingularRoundInfo(2, tickSpawns);
        SingularRoundInfo round3 = new SingularRoundInfo(3, tickSpawns);
        return new RoundInfo(List.of(round1, round2, round3));
    }
}
