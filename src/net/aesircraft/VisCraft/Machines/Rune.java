package net.aesircraft.VisCraft.Machines;

import net.aesircraft.VisCraft.Data.Machines;
import net.aesircraft.VisCraft.Data.VisLocation;
import net.aesircraft.VisCraft.Machines.Effects.RuneEffect;
import net.aesircraft.VisCraft.Player.User;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class Rune {

    private Location origin;

    public boolean checkSignature(Location l) {
        VisLocation v = new VisLocation();
        v.loadFromLocation(l);
        if (Machines.protectedBlocks.containsKey(v.toString())) {
            return false;
        }
        Block b1 = l.getBlock();
        Block b2 = l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY() - 1, l.getBlockZ());
        Block b3 = l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ());
        Block b4 = l.getWorld().getBlockAt(l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ());
        Block b5 = l.getWorld().getBlockAt(l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ());
        Block b6 = l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() + 1);
        Block b7 = l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() - 1);
        if (b1.getTypeId() == 20 && b2.getTypeId() == 1 && b3.getTypeId() == 35 && b4.getTypeId() == 1 && b5.getTypeId() == 1 && b6.getTypeId() == 1 && b7.getTypeId() == 1) {
            return true;
        }
        return false;
    }

    public void remove() {
        Location l = origin;
        Block b1 = l.getBlock();
        Block b2 = l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY() - 1, l.getBlockZ());
        Block b3 = l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ());
        Block b4 = l.getWorld().getBlockAt(l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ());
        Block b5 = l.getWorld().getBlockAt(l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ());
        Block b6 = l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() + 1);
        Block b7 = l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() - 1);
        b1.setTypeId(0);
        b2.setTypeId(0);
        b3.setTypeId(0);
        b4.setTypeId(0);
        b5.setTypeId(0);
        b6.setTypeId(0);
        b7.setTypeId(0);
    }

    public void executeRune(User user, Location location) {
        origin = location;
        RuneEffect rune = new RuneEffect();
        rune.load(origin);
        if (rune.execute(user)) {
            remove();
        }
    }
}
