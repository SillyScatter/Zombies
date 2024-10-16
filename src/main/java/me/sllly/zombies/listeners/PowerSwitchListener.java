package me.sllly.zombies.listeners;

import me.sllly.zombies.Zombies;
import me.sllly.zombies.mechanisms.Game;
import me.sllly.zombies.mechanisms.game.ZombiesPlayer;
import me.sllly.zombies.utils.Util;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PowerSwitchListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }
        Block block = event.getClickedBlock();
        if (!(block.getBlockData() instanceof Switch lever)){
            return;
        }
        Game game = Game.getGame(block.getWorld());
        if (game == null) {
            return;
        }
        if (lever.isPowered()){
            event.setCancelled(true);
            return;
        }
        if (game.getPowerSwitch().isPowered()){
            Util.log("&cError: Power switch attempted to switch on, but power is already on.");
            event.setCancelled(true);
            return;
        }
        if (!game.getGameTemplate().powerSwitchInfo().switchLocation().equals(block.getLocation())){
            event.setCancelled(true);
            return;
        }
        if (!game.getOpenRooms().contains(game.getGameTemplate().powerSwitchInfo().roomName())){
            event.setCancelled(true);
            return;
        }
        ZombiesPlayer zombiesPlayer = ZombiesPlayer.getZombiePlayer(event.getPlayer());
        if (zombiesPlayer == null) {
            Zombies.actionsConfig.notPartOfGame.perform(event.getPlayer());
            event.setCancelled(true);
            return;
        }
        int cost = game.getGameTemplate().powerSwitchInfo().cost();
        if (!zombiesPlayer.hasEnoughMoney(cost)){
            Zombies.actionsConfig.notEnoughMoney.perform(zombiesPlayer.getPlayer());
            event.setCancelled(true);
            return;
        }
        zombiesPlayer.removeMoney(cost);
        game.getPowerSwitch().setPowered(true);
    }
}
