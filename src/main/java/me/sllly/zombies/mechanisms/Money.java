package me.sllly.zombies.mechanisms;

public class Money {

    public static void addMoney(ZombiesPlayer zombiesPlayer, int amount) {
        zombiesPlayer.setMoney(zombiesPlayer.getMoney() + amount);
    }

    public static void removeMoney(ZombiesPlayer zombiesPlayer, int amount) {
        zombiesPlayer.setMoney(zombiesPlayer.getMoney() - amount);
    }

    public static boolean hasEnoughMoney(ZombiesPlayer zombiesPlayer, int amount) {
        return zombiesPlayer.getMoney() >= amount;
    }
}
