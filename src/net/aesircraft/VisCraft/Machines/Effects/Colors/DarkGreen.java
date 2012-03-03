package net.aesircraft.VisCraft.Machines.Effects.Colors;

import java.util.ArrayList;
import java.util.List;
import net.aesircraft.VisCraft.Machines.Effects.RuneConfigs;
import net.aesircraft.VisCraft.Player.User;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class DarkGreen {

    public static String getReqs() {
        String req = "§c1,000 Unstable Vis";
        return req;
    }

    public static boolean checkReqs(User user) {
        if (!user.hasRunePermission("darkgreen")) {
            user.getPlayer().sendMessage("§4You do not have permission for this 5Vis Rune§4.");
            return false;
        }
        if (user.hasUnstableVis(5000)) {
            return true;
        }
        return false;
    }

    public static boolean isEnabled() {
        return RuneConfigs.darkgreen;
    }

    public static void takeReqs(User user) {
        user.subtractUnstableVis(5000);
    }

    public static void execute(User user, Location l) {
        List<Location> l2 = new ArrayList<Location>();
        int y = 0;
        for (int ctr = 0; ctr < 10; ctr++) {
            y = l.getBlockY() - ctr - 3;
            if (y <= 4) {
                break;
            }
            l2.add(new Location(l.getWorld(), l.getBlockX(), y, l.getBlockZ()));
            l2.add(new Location(l.getWorld(), l.getBlockX() + 1, y, l.getBlockZ()));
            l2.add(new Location(l.getWorld(), l.getBlockX(), y, l.getBlockZ() + 1));
            l2.add(new Location(l.getWorld(), l.getBlockX() + 1, y, l.getBlockZ() + 1));
            l2.add(new Location(l.getWorld(), l.getBlockX() - 1, y, l.getBlockZ()));
            l2.add(new Location(l.getWorld(), l.getBlockX(), y, l.getBlockZ() - 1));
            l2.add(new Location(l.getWorld(), l.getBlockX() - 1, y, l.getBlockZ() - 1));
            l2.add(new Location(l.getWorld(), l.getBlockX() + 1, y, l.getBlockZ() - 1));
            l2.add(new Location(l.getWorld(), l.getBlockX() - 1, y, l.getBlockZ() + 1));
        }
        Block b;
        for (Location loc : l2) {
            b = loc.getBlock();
            if (b == null) {
                continue;
            }
            if (b.getTypeId() == 0) {
                continue;
            }
            if (b.getTypeId() == 1 || b.getTypeId() == 2 || b.getTypeId() == 3 || b.getTypeId() == 12 || b.getTypeId() == 13 || b.getTypeId() == 24 || b.getTypeId() == 80) {
                b.setTypeId(0);
            }
        }
    }
}
