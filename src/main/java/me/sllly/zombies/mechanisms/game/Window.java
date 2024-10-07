package me.sllly.zombies.mechanisms.game;

import me.sllly.zombies.Zombies;
import me.sllly.zombies.mechanisms.Game;
import me.sllly.zombies.objects.Area;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Slab;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;

public class Window {
    private final Area windowArea;
    private final String roomName;
    private final Game game;
    private final Location windowSpawnLocation;
    private final List<Block> blocks;
    private boolean isActive;

    public Window(Area windowArea, String roomName, Game game, Location windowSpawnLocation) {
        this.windowArea = new Area(windowArea.getCornerOne(), windowArea.getCornerTwo());
        this.windowArea.setWorld(game.getWorld());
        this.roomName = roomName;
        this.windowSpawnLocation = windowSpawnLocation;
        this.windowSpawnLocation.setWorld(game.getWorld());
        blocks = this.windowArea.getBlocksWithinArea();
        this.game = game;
        isActive = false;
    }

    public void activate(){
        isActive = true;
        beginZombieSearch();
        beginPlayerSearch();
    }

    public void deactivate(){
        isActive = false;
    }

    public void beginZombieSearch(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isActive){
                    cancel();
                    return;
                }
                double radius = Zombies.settingsConfig.windowDamageDistance;
                if (windowArea.getCentreLocation().getWorld().getNearbyEntities(windowArea.getCentreLocation(), radius, radius, radius).stream().anyMatch(entity -> {
                   return entity.getPersistentDataContainer().has(MobHandler.zombieKey);
                })){
                    damage();
                }
            }
        }.runTaskTimer(Zombies.plugin, Zombies.settingsConfig.windowDamageTime, Zombies.settingsConfig.windowDamageTime);
    }

    public void beginPlayerSearch() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isActive) {
                    cancel();
                    return;
                }

                game.getPlayers().values().stream()
                        .filter(zombiesPlayer -> zombiesPlayer.getPlayer().isSneaking() &&
                                zombiesPlayer.getPlayer().getWorld().equals(windowArea.getCentreLocation().getWorld()) &&
                                zombiesPlayer.getPlayer().getLocation().distanceSquared(windowArea.getCentreLocation()) < Math.pow(Zombies.settingsConfig.windowRepairDistance, 2))
                        .findFirst().ifPresent(repairPlayer -> repair(repairPlayer));

            }
        }.runTaskTimer(Zombies.plugin, Zombies.settingsConfig.windowRepairTime, Zombies.settingsConfig.windowRepairTime);
    }

    public void repair(ZombiesPlayer zombiesPlayer){
        Collections.shuffle(blocks);
        for (Block block : blocks){
            if (block.getType().isAir()){
                Slab slab = (Slab) Material.SPRUCE_SLAB.createBlockData();
                slab.setType(Slab.Type.TOP);
                block.setBlockData(slab);
                if (blocks.stream().allMatch(block1 -> block1.getType() == Material.SPRUCE_SLAB)){
                    block.getWorld().playSound(block.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 2f);
                    return;
                }
                block.getWorld().playSound(block.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 1f);
                return;
            }
        }
    }

    public void damage(){
        Collections.shuffle(blocks);
        for (Block block : blocks){
            if (!block.getType().isAir()){
                block.setType(Material.AIR);
                block.getWorld().playSound(block.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1, 0.5f);
                return;
            }
        }
    }

    public Area getWindowArea() {
        return windowArea;
    }

    public String getRoomName() {
        return roomName;
    }

    public Game getGame() {
        return game;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Location getWindowSpawnLocation() {
        return windowSpawnLocation;
    }
}
