package me.sllly.zombies.mechanisms.purchasables;

import me.sllly.zombies.mechanisms.game.ZombiesPlayer;

public class ArmorPurchasable implements Purchasable{
    @Override
    public boolean canPurchase(ZombiesPlayer zombiesPlayer) {
        return true;
    }

    @Override
    public void onPurchase(ZombiesPlayer zombiesPlayer) {

    }
}
