package me.sllly.zombies.mechanisms.ability.player;

import me.sllly.zombies.mechanisms.game.ZombiesPlayer;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractPlayerAbility {

    private final String id;
    private final ItemStack item;

    public AbstractPlayerAbility(String id, ItemStack item) {
        this.id = id;
        this.item = item;
    }

    public abstract void onActivate(ZombiesPlayer zombiesPlayer);
    public abstract void onDeactivate(ZombiesPlayer zombiesPlayer);

    public String getId() {
        return id;
    }

    public ItemStack getItem() {
        return item;
    }
}
