package me.sllly.zombies.mechanisms.weapons;

import com.octanepvp.splityosis.octaneengine.function.Function;
import org.bukkit.inventory.ItemStack;

public record WeaponUpgradeInfo(Function distanceDamageFunction, int penetration, int clipAmmo,
                                int maxAmmo, int cooldown, ItemStack itemStack) {
}