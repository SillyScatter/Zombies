package me.sllly.zombies;

import com.octane.sllly.octaneitemregistry.objects.registry.OctaneItemStackRegistry;
import com.octanepvp.splityosis.octaneworldsapi.OctaneWorldsAPI;
import me.sllly.zombies.commands.ZombiesCommandSystem;
import me.sllly.zombies.files.configs.*;
import me.sllly.zombies.files.logics.*;
import me.sllly.zombies.listeners.PlayerListeners;
import me.sllly.zombies.listeners.PowerSwitchListener;
import me.sllly.zombies.listeners.ZombieDeathListener;
import me.sllly.zombies.mechanisms.Game;
import me.sllly.zombies.mechanisms.ability.player.AbstractPlayerAbility;
import me.sllly.zombies.mechanisms.ability.weapon.*;
import me.sllly.zombies.mechanisms.setup.GameTemplate;
import me.sllly.zombies.mechanisms.weapons.Gun;
import me.sllly.zombies.utils.Util;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Zombies extends JavaPlugin {

    public static Zombies plugin;
    public static WeaponAbilityConfig weaponAbilityConfig;
    public static GunConfig gunConfig;
    public static SettingsConfig settingsConfig;
    public static ActionsConfig actionsConfig;
    public static Map<String, AbstractWeaponAbility> weaponAbilities = new HashMap<>();
    public static Map<String, AbstractPlayerAbility> playerAbilities = new HashMap<>();
    public static Map<String, GameTemplate> gameTemplates = new HashMap<>();
    public static Map<UUID, Game> activeGames = new HashMap<>();

    public static File templateDirectory;

    public static OctaneWorldsAPI octaneWorldsAPI;

    @Override
    public void onEnable() {
        plugin = this;

        octaneWorldsAPI = (OctaneWorldsAPI) getServer().getPluginManager().getPlugin("OctaneWorlds");
        registerConfigLogics();


        templateDirectory = new File(plugin.getDataFolder(), "template-configs");
        initializeConfigs();
        registerWeaponAbilities();
        registerGuns();
        reloadTemplates();

        new ZombiesCommandSystem("zombies").registerCommandBranch(this);
        getServer().getPluginManager().registerEvents(new ZombieDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        getServer().getPluginManager().registerEvents(new PowerSwitchListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reload(){
        initializeConfigs();
        registerGuns();
        reloadTemplates();
    }

    public void initializeConfigs(){
        settingsConfig = new SettingsConfig(getDataFolder(), "settings");
        try {
            settingsConfig.initialize();
        }catch (Exception e){
            e.printStackTrace();
        }

        actionsConfig = new ActionsConfig(getDataFolder(), "actions");
        try {
            actionsConfig.initialize();
        }catch (Exception e){
            e.printStackTrace();
        }

        weaponAbilityConfig = new WeaponAbilityConfig(getDataFolder(), "weapon-abilities");
        try {
            weaponAbilityConfig.initialize();
        }catch (Exception e){
            e.printStackTrace();
        }

        registerWeaponAbilities();

        gunConfig = new GunConfig(getDataFolder(), "guns");
        try {
            gunConfig.initialize();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void registerWeaponAbilities(){
        PoisonAbility poisonAbility = new PoisonAbility();
        weaponAbilities.put(poisonAbility.getId(), poisonAbility);
        RocketLaunchAbility rocketLaunchAbility = new RocketLaunchAbility();
        weaponAbilities.put(rocketLaunchAbility.getId(), rocketLaunchAbility);
        SetOnFireAbility setOnFireAbility = new SetOnFireAbility();
        weaponAbilities.put(setOnFireAbility.getId(), setOnFireAbility);
        SoakerAbility soakerAbility = new SoakerAbility();
        weaponAbilities.put(soakerAbility.getId(), soakerAbility);
        StunAbility stunAbility = new StunAbility();
        weaponAbilities.put(stunAbility.getId(), stunAbility);
    }

    public void registerConfigLogics(){
        new WeaponUpgradeInfoListLogic().register();
        new GunMapConfigLogic().register();
        new AreaLogic().register();
        new WindowInfoLogic().register();
        new RoundInfoLogic().register();
        new DoorInfoLogic().register();
        new MobInfoLogic().register();
        new PowerSwitchLogic().register();
        new PackAPunchLogic().register();
    }

    public void registerGuns(){
        OctaneItemStackRegistry.registry.unregisterAll(plugin);
        for (String s : gunConfig.guns.keySet()) {
            Gun gun = gunConfig.guns.get(s);
            OctaneItemStackRegistry.registerOctaneItemStack(gun);
        }
    }

    public void reloadTemplates(){
        int count = 0;
        gameTemplates.clear();
        File[] templateFiles = templateDirectory.listFiles();
        if (templateFiles == null || templateFiles.length == 0){
            try {
                new GameTemplateConfig(new File(templateDirectory, "default-template.yml")).initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        templateFiles = templateDirectory.listFiles();
        for (File templateFile : templateFiles) {
            GameTemplateConfig templateConfig = new GameTemplateConfig(templateFile);
            try {
                templateConfig.initialize();
            } catch (Exception e){
                e.printStackTrace();
            }

            GameTemplate gameTemplate = new GameTemplate(templateConfig.templateId, templateConfig.templateWorldName, templateConfig.roomNames, templateConfig.defaultRoomName, templateConfig.defaultGunName, templateConfig.spawnLocation,
                    templateConfig.roundInfo, templateConfig.windowInfo, templateConfig.doorInfo, null, null, templateConfig.powerSwitchInfo, null, null, templateConfig.mobInfo, templateConfig.maxPlayers);
            gameTemplates.put(templateConfig.templateId, gameTemplate);
            count++;
        }
        Util.log("&aSuccessfully registered templates: &2&l"+count);
    }
}
