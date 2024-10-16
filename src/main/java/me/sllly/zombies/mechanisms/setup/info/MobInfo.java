package me.sllly.zombies.mechanisms.setup.info;

import me.sllly.zombies.mechanisms.setup.singulars.SingularMobInfo;

import java.util.HashMap;
import java.util.Map;

public record MobInfo(Map<String, SingularMobInfo> mobInfoHashMap) {

    public static MobInfo getDefaultMobInfo(){
        HashMap<String, SingularMobInfo> mobInfoHashMap = new HashMap<>();
        mobInfoHashMap.put("Skeleton_Warrior", new SingularMobInfo("Skeleton_Warrior", 10, 20));
        mobInfoHashMap.put("Skeleton_Archer", new SingularMobInfo("Skeleton_Archer", 15, 25));
        return new MobInfo(mobInfoHashMap);
    }
}
