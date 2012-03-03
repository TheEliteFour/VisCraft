package net.aesircraft.VisCraft.Machines.Effects.Colors;

import net.aesircraft.VisCraft.Machines.Effects.RuneConfigs;
import net.aesircraft.VisCraft.Player.User;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class Lime {

    public static String getReqs() {
        String req = "§a1000 Brilliant Vis§e and space for 2 blocks on top.";
        return req;
    }

    public static boolean checkReqs(User user, Location l) {
        if (!user.hasRunePermission("lime")) {
            user.getPlayer().sendMessage("§4You do not have permission for this 5Vis Rune§4.");
            return false;
        }
        if (user.hasBrilliantVis(1000)) {
            Block b = new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ()).getBlock();
            Block b2 = new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 2, l.getBlockZ()).getBlock();
            if (l.getBlockY() < 255 && b.getTypeId() > 0 && b2.getTypeId() == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEnabled() {
        return RuneConfigs.lime;
    }

    public static void takeReqs(User user) {
        user.subtractBrilliantVis(1000);
    }

    public static void execute(User user, Location l) {
        Block b = new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ()).getBlock();
        Block b2 = new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 2, l.getBlockZ()).getBlock();
        b2.setTypeIdAndData(b.getTypeId(), b.getData(), true);
    }
}
