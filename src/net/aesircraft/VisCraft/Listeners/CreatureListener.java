package net.aesircraft.VisCraft.Listeners;

import java.util.List;
import net.aesircraft.VisCraft.Data.Machines;
import net.aesircraft.VisCraft.Data.VisLocation;
import net.aesircraft.VisCraft.VisCraft;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class CreatureListener implements Listener {

    public CreatureListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, VisCraft.getStatic());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        Location location = event.getBlock().getLocation();
        VisLocation v = new VisLocation();
        v.loadFromLocation(location);
        if (Machines.signBlocks.contains(v.toString())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        if (event.isCancelled()) {
            if (Machines.safeSpawns.contains(event.getEntity().getEntityId())) {
                event.setCancelled(false);
                Machines.safeSpawns.remove(event.getEntity().getEntityId());
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (!event.isCancelled()) {
            List<Block> blocks=event.blockList();
            VisLocation l=new VisLocation();
            for (Block b : blocks){
                l.loadFromLocation(b.getLocation());
                if (Machines.protectedBlocks.containsKey(l.toString()) || Machines.signBlocks.contains(l.toString())){
                    blocks.remove(b);
                }
            }            
        }
    }
}
