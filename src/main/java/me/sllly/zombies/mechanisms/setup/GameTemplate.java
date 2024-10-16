package me.sllly.zombies.mechanisms.setup;

import me.sllly.zombies.mechanisms.setup.info.*;
import org.bukkit.Location;

import java.util.List;

public record GameTemplate(
        String templateId,
        String templateWorldName,
        List<String> roomNames,
        String defaultRoomName,
        String defaultGunName,
        Location spawnLocation,
        RoundInfo roundInfo,
        WindowInfo windowInfo,
        DoorInfo doorInfo,
        PurchasablesInfo purchasablesInfo,
        PackAPunchInfo packAPunchInfo,
        PowerSwitchInfo powerSwitchInfo,
        AbilitiesInfo abilitiesInfo,
        WeaponsChestInfo weaponsChestInfo,
        MobInfo mobInfo,
        int maxPlayers
) {

}