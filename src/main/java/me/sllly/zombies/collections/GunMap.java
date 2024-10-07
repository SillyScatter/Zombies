package me.sllly.zombies.collections;

import com.octane.sllly.octaneitemregistry.util.Util;
import com.octanepvp.splityosis.octaneengine.function.Function;
import com.octanepvp.splityosis.octaneengine.function.mathfunction.MathFunction;
import me.sllly.zombies.mechanisms.weapons.Gun;
import me.sllly.zombies.mechanisms.weapons.WeaponUpgradeInfo;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class GunMap extends HashMap<String, Gun> {

    public static GunMap getDefault(){
        WeaponUpgradeInfoList weaponUpgradeInfoList = new WeaponUpgradeInfoList();
        ItemStack defaultItemstack = Util.createItemStack(Material.WOODEN_HOE, 1, "&6&lPistol");
        ItemStack upgradeItemstack = Util.createItemStack(Material.WOODEN_HOE, 1, "&6&lPistol Upgrade");
        Function damageFunction = new MathFunction("100-distance").variables("distance").compile();
        WeaponUpgradeInfo defaultUpgrade = new WeaponUpgradeInfo(damageFunction, 1, 10, 100, 10, defaultItemstack);
        WeaponUpgradeInfo upgradeUpgrade = new WeaponUpgradeInfo(damageFunction, 1, 15, 150, 10, upgradeItemstack);
        weaponUpgradeInfoList.add(defaultUpgrade);
        weaponUpgradeInfoList.add(upgradeUpgrade);
        Gun gun = new Gun("pistol", weaponUpgradeInfoList, new ArrayList<>());

        GunMap gunMap = new GunMap();
        gunMap.put("pistol", gun);

        return gunMap;
    }
}
