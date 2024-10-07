package me.sllly.zombies.mechanisms.purchasables;

import me.sllly.zombies.mechanisms.game.ZombiesPlayer;

public interface Purchasable {

    boolean canPurchase(ZombiesPlayer zombiesPlayer);
    void onPurchase(ZombiesPlayer zombiesPlayer);


}
