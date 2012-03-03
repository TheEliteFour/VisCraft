package net.aesircraft.VisCraft.Listeners;

import net.aesircraft.VisCraft.Data.Machines;
import net.aesircraft.VisCraft.Data.VisLocation;
import net.aesircraft.VisCraft.Machines.Condenser;
import net.aesircraft.VisCraft.Machines.Extractor;
import net.aesircraft.VisCraft.Machines.Infuser;
import net.aesircraft.VisCraft.Player.User;
import net.aesircraft.VisCraft.VisCraft;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class BlockListeners implements Listener {

    public BlockListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(this, VisCraft.getStatic());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockIgnite(final BlockIgniteEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Location location = event.getBlock().getLocation();
        VisLocation v = new VisLocation();
        v.loadFromLocation(location);
        if (Machines.protectedBlocks.containsKey(v.toString()) || Machines.signBlocks.contains(v.toString())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBurn(BlockBurnEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Location location = event.getBlock().getLocation();
        VisLocation v = new VisLocation();
        v.loadFromLocation(location);
        if (Machines.protectedBlocks.containsKey(v.toString()) || Machines.signBlocks.contains(v.toString())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(final BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        User u2 = new User();
        u2.load(event.getPlayer());
        if (u2.hasAdminPermission()) {
            return;
        }
        VisLocation v = new VisLocation();
        v.loadFromLocation(event.getBlock().getLocation());
        if (Machines.protectedBlocks.containsKey(v.toString())) {
            if (Machines.machines.get(Machines.protectedBlocks.get(v.toString())).isExtractor()) {
                event.getPlayer().sendMessage("§4You may not build in an §5Extractor's§4 area!");
                event.setCancelled(true);
                return;
            }
            if (Machines.machines.get(Machines.protectedBlocks.get(v.toString())).isInfuser()) {
                event.getPlayer().sendMessage("§4You may not build in an §5Infuser's§4 area!");
                event.setCancelled(true);
                return;
            }
            if (Machines.machines.get(Machines.protectedBlocks.get(v.toString())).isCondenser()) {
                event.getPlayer().sendMessage("§4You may not build in an §5Condenser's§4 area!");
                event.setCancelled(true);
                return;
            }
            if (Machines.machines.get(Machines.protectedBlocks.get(v.toString())).isCollector()) {
                event.getPlayer().sendMessage("§4You may not build in an §5Collector's§4 area!");
                event.setCancelled(true);
                return;
            }
        }
        User u = new User();
        u.load(event.getPlayer());
        u.checkRemove(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }
        VisLocation v = new VisLocation();
        v.loadFromLocation(event.getBlock().getLocation());
        if (Machines.protectedBlocks.containsKey(v.toString())) {
            User u = new User();
            u.load(event.getPlayer());
            if (u.hasAdminPermission()) {
                return;
            }
            if (Machines.machines.get(Machines.protectedBlocks.get(v.toString())).isExtractor()) {
                event.getPlayer().sendMessage("§4You may not build in an §5Extractor's§4 area!");
                event.setCancelled(true);
                return;
            }
            if (Machines.machines.get(Machines.protectedBlocks.get(v.toString())).isInfuser()) {
                event.getPlayer().sendMessage("§4You may not build in an §5Infuser's§4 area!");
                event.setCancelled(true);
                return;
            }
            if (Machines.machines.get(Machines.protectedBlocks.get(v.toString())).isCondenser()) {
                event.getPlayer().sendMessage("§4You may not build in an §5Condenser's§4 area!");
                event.setCancelled(true);
                return;
            }
            if (Machines.machines.get(Machines.protectedBlocks.get(v.toString())).isCollector()) {
                event.getPlayer().sendMessage("§4You may not build in an §5Collector's§4 area!");
                event.setCancelled(true);
                return;
            }
        }
        if (Machines.fuelBlocks.containsKey(v.toString())) {
            if (Machines.machines.get(Machines.fuelBlocks.get(v.toString())).isExtractor()) {
                ((Extractor) Machines.machines.get(Machines.fuelBlocks.get(v.toString())).getMachine()).turnOn();
                return;
            }
            if (Machines.machines.get(Machines.fuelBlocks.get(v.toString())).isInfuser()) {
                ((Infuser) Machines.machines.get(Machines.fuelBlocks.get(v.toString())).getMachine()).turnOn();
                return;
            }
            if (Machines.machines.get(Machines.fuelBlocks.get(v.toString())).isCondenser()) {
                ((Condenser) Machines.machines.get(Machines.fuelBlocks.get(v.toString())).getMachine()).turnOn();
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignChange(final SignChangeEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Location location = event.getBlock().getLocation();
        VisLocation v = new VisLocation();
        v.loadFromLocation(location);
        if (Machines.signBlocks.contains(v.toString())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDamage(final BlockDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Location location = event.getBlock().getLocation();
        VisLocation v = new VisLocation();
        v.loadFromLocation(location);
        if (Machines.protectedBlocks.containsKey(v.toString()) || Machines.signBlocks.contains(v.toString())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockFade(final BlockFadeEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Location location = event.getBlock().getLocation();
        VisLocation v = new VisLocation();
        v.loadFromLocation(location);
        if (Machines.protectedBlocks.containsKey(v.toString()) || Machines.signBlocks.contains(v.toString())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPistonExtend(final BlockPistonExtendEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Location location = event.getBlock().getLocation();
        VisLocation v = new VisLocation();
        v.loadFromLocation(location);
        if (Machines.protectedBlocks.containsKey(v.toString()) || Machines.signBlocks.contains(v.toString())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPistonRetract(final BlockPistonRetractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Location location = event.getBlock().getLocation();
        VisLocation v = new VisLocation();
        v.loadFromLocation(location);
        if (Machines.protectedBlocks.containsKey(v.toString()) || Machines.signBlocks.contains(v.toString())) {
            event.setCancelled(true);
        }
    }
}
