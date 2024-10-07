package me.sllly.zombies.listeners;

import me.sllly.zombies.Zombies;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.InventoryHolder;

public class PlayerListeners implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (Zombies.activeGames.values().stream().anyMatch(game -> game.getPlayers().containsKey(event.getWhoClicked().getUniqueId()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        if (Zombies.activeGames.values().stream().anyMatch(game -> {
            InventoryHolder holder = event.getInventory().getHolder();
            if (holder instanceof HumanEntity humanEntity){
                return game.getPlayers().containsKey(humanEntity.getUniqueId());
            }
            return false;
        })) {
            event.setCancelled(true);
        }
    }


}
