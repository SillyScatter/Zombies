package me.sllly.zombies.files.configs;

import com.octanepvp.splityosis.configsystem.configsystem.AnnotatedConfig;
import com.octanepvp.splityosis.configsystem.configsystem.ConfigField;
import me.sllly.zombies.collections.GunMap;
import me.sllly.zombies.utils.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;

public class GunConfig extends AnnotatedConfig {
    public GunConfig(File parentDirectory, String name) {
        super(parentDirectory, name);
    }

    @ConfigField(path = "guns")
    public GunMap guns = GunMap.getDefault();

    @ConfigField(path = "knife")
    public ItemStack knife = getKnife();

    @ConfigField(path = "null-gun")
    public ItemStack nullGun = getNullGun();

    public ItemStack getKnife() {
        ItemStack knife = Util.createItemStack(Material.IRON_SWORD, 1, "&cKnife");
        ItemMeta knifeMeta = knife.getItemMeta();
        knifeMeta.setUnbreakable(true);
        knifeMeta.getItemFlags().add(ItemFlag.HIDE_UNBREAKABLE);
        knifeMeta.getItemFlags().add(ItemFlag.HIDE_ATTRIBUTES);
        knife.setItemMeta(knifeMeta);
        return knife;
    }

    public ItemStack getNullGun() {
        return Util.createItemStack(Material.CLAY_BALL, 1, "&cNull Gun");
    }
}
