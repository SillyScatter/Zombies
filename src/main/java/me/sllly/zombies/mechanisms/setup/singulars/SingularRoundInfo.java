package me.sllly.zombies.mechanisms.setup.singulars;

import java.util.Map;

public record SingularRoundInfo(int roundNumber, Map<Integer, TickSpawnInfo> tickSpawns) {
}
