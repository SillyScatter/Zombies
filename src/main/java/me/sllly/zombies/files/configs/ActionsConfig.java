package me.sllly.zombies.files.configs;

import com.octane.sllly.octaneitemregistry.util.Util;
import com.octanepvp.splityosis.configsystem.configsystem.AnnotatedConfig;
import com.octanepvp.splityosis.configsystem.configsystem.ConfigField;
import com.octanepvp.splityosis.configsystem.configsystem.actionsystem.Actions;
import org.bukkit.Sound;

import java.io.File;

public class ActionsConfig extends AnnotatedConfig {
    public ActionsConfig(File parentDirectory, String name) {
        super(parentDirectory, name);
    }

    @ConfigField(path = "round-start")
    public Actions roundStart = Util.getDefaultTitleActions(Sound.ENTITY_ENDER_DRAGON_FLAP, "&c&lRound %round-number%", "&6Begin!");

    @ConfigField(path = "player-join-game")
    public Actions playerJoinGame = Util.getDefaultActions(null, "&7%player-name% &ehas joined the game!");

    @ConfigField(path = "game-end-win")
    public Actions gameWinEnd = Util.getDefaultTitleActions(Sound.ENTITY_FIREWORK_ROCKET_BLAST, "&a&lVictory!", "&6You have survived!");

    @ConfigField(path = "out-of-ammo")
    public Actions outOfAmmo = Util.getDefaultTitleActions(Sound.ENTITY_ITEM_BREAK, "&c&lOut of Ammo!", "&6Purchase more.");

    @ConfigField(path = "not-enough-money")
    public Actions notEnoughMoney = Util.getDefaultActions(Sound.BLOCK_NOTE_BLOCK_BASS, "&cNot enough money!");

    @ConfigField(path = "receive-money")
    public Actions receiveMoney = Util.getDefaultActions(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, "&e+ %amount% money!");

    @ConfigField(path = "not-part-of-game")
    public Actions notPartOfGame = Util.getDefaultActions(Sound.BLOCK_NOTE_BLOCK_BASS, "&cYou are not part of this game!");
}
