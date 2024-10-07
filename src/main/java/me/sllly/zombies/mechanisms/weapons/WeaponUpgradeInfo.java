package me.sllly.zombies.mechanisms.weapons;

import com.octane.sllly.octaneitemregistry.util.Util;
import com.octanepvp.splityosis.octaneengine.function.Function;
import com.octanepvp.splityosis.octaneengine.function.mathfunction.MathFunction;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public record WeaponUpgradeInfo(Function distanceDamageFunction, int penetration, int clipAmmo,
                                int maxAmmo, int cooldown, ItemStack itemStack) {

    public static WeaponUpgradeInfo getDefault(){
        return new WeaponUpgradeInfo(new MathFunction("100-distance").variables("distance"), 1, 10, 100, 10, Util.createItemStack(Material.WOODEN_HOE, 1, "Default Upgrade"));
    }
}