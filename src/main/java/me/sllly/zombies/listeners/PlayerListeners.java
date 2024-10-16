package me.sllly.zombies.listeners;

import com.octane.sllly.octaneitemregistry.objects.registry.OctaneItemStackRegistry;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.event.HologramClickEvent;
import me.sllly.zombies.Zombies;
import me.sllly.zombies.mechanisms.game.Door;
import me.sllly.zombies.mechanisms.game.ZombiesPlayer;
import me.sllly.zombies.mechanisms.weapons.Gun;
import me.sllly.zombies.utils.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

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

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        if (Zombies.activeGames.values().stream().anyMatch(game -> game.getPlayers().containsKey(event.getPlayer().getUniqueId()))) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if (itemStack == null || itemStack.getType().isAir()) {
            player.setTotalExperience(0);
            return;
        }
        Gun gun = (Gun) OctaneItemStackRegistry.getOctaneItemStack(itemStack);
        if (gun == null) {
            player.setTotalExperience(0);
            return;
        }
        int totalAmmo = gun.getCurrentAvailableAmmo(itemStack);
        player.setLevel(totalAmmo);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFoodLevelChange(EntityExhaustionEvent event) {
        if (Zombies.activeGames.values().stream().anyMatch(game -> game.getPlayers().containsKey(event.getEntity().getUniqueId()))) {
            event.getEntity().setFoodLevel(20);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }
        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) {
            return;
        }
        Gun gun = (Gun) OctaneItemStackRegistry.getOctaneItemStack(itemStack);
        if (gun == null) {
            return;
        }
        gun.reloadGun(itemStack);
    }

    @EventHandler
    public void onPlayerAnimation(PlayerAnimationEvent event) {
        if (event.getAnimationType() == PlayerAnimationType.ARM_SWING) {
            Player player = event.getPlayer();
            ItemStack itemStack = player.getInventory().getItemInMainHand();

            if (itemStack.getType().isAir()) {
                return;
            }

            Gun gun = (Gun) OctaneItemStackRegistry.getOctaneItemStack(itemStack);
            if (gun == null) {
                return;
            }

            gun.reloadGun(itemStack);
        }
    }


    @EventHandler(ignoreCancelled = true)
    public void onHologramClick(HologramClickEvent event) {
        Player player = event.getPlayer();
        ZombiesPlayer zombiesPlayer = ZombiesPlayer.getZombiePlayer(player);
        if (zombiesPlayer == null) {
            Util.log("&cError -> Failed to find user that interacted with hologram.");
            return;
        }
        Hologram hologram = event.getHologram();

        Door door = Door.getDoor(hologram);
        if (door != null){
            door.onDoorClick(zombiesPlayer, hologram);
            return;
        }
    }

}
