package me.sllly.zombies.mechanisms.game;

import com.octane.sllly.octaneitemregistry.objects.registry.OctaneItemStackRegistry;
import me.sllly.zombies.Zombies;
import me.sllly.zombies.mechanisms.Game;
import me.sllly.zombies.mechanisms.ability.player.AbstractPlayerAbility;
import me.sllly.zombies.mechanisms.weapons.Gun;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZombiesPlayer {
    private Player player;
    private int money;
    private final Map<String, Integer> statistics;
    private PlayerStatus status;
    private int weaponAmount;
    private final List<AbstractPlayerAbility> abilities;
    private int kills;

    public ZombiesPlayer(Player player, int money, Map<String, Integer> statistics, PlayerStatus status, int weaponAmount, List<AbstractPlayerAbility> abilities, int kills) {
        this.player = player;
        this.money = money;
        this.statistics = statistics;
        this.status = status;
        this.weaponAmount = weaponAmount;
        this.abilities = abilities;
        this.kills = kills;
    }

    public ZombiesPlayer(Player player){
        this.player = player;
        this.money = 0;
        this.statistics = new HashMap<>();
        this.status = PlayerStatus.ALIVE;
        this.weaponAmount = 2;
        this.abilities = new ArrayList<>();
        this.kills = 0;
    }

    public void initializeInventory(Game game){
        Inventory inventory = player.getInventory();
        inventory.clear();
        inventory.setItem(0, Zombies.gunConfig.knife.clone());
        Gun defaultGun = Zombies.gunConfig.guns.get(game.getGameTemplate().defaultGunName());
        if (defaultGun == null) {
            inventory.setItem(1, Zombies.gunConfig.nullGun);
        }else {
            inventory.setItem(1, defaultGun.getFunctionalItemStack());
        }
        inventory.setItem(2, Zombies.gunConfig.nullGun);
    }

    public static ZombiesPlayer getZombiePlayer(Player player){
        Game game = Game.getGame(player.getWorld());
        if (game == null) {
            return null;
        }
        return game.getPlayers().get(player.getUniqueId());
    }

    public void setPlayerExp(){
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) {
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

    public void addMoney(int amount) {
        Map<String, String> placeHolders = Map.of("%amount%", amount + "");
        Zombies.actionsConfig.receiveMoney.perform(player, placeHolders);
        this.money += amount;
    }

    public void removeMoney(int amount) {
        this.money -= amount;
    }

    public boolean hasEnoughMoney(int amount) {
        return this.money >= amount;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Map<String, Integer> getStatistics() {
        return statistics;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public int getWeaponAmount() {
        return weaponAmount;
    }

    public void setWeaponAmount(int weaponAmount) {
        this.weaponAmount = weaponAmount;
    }

    public List<AbstractPlayerAbility> getAbilities() {
        return abilities;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }
}
