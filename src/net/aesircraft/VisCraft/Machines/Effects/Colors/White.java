package net.aesircraft.VisCraft.Machines.Effects.Colors;

import net.aesircraft.VisCraft.Machines.Effects.RuneConfigs;
import net.aesircraft.VisCraft.Player.User;

public class White {

    public static String getReqs() {
        String req = "§f100 Pure Vis";
        return req;
    }

    public static boolean checkReqs(User user) {
        if (!user.hasRunePermission("white")) {
            user.getPlayer().sendMessage("§4You do not have permission for this 5Vis Rune§4.");
            return false;
        }
        if (user.hasPureVis(100)) {
            return true;
        }
        return false;
    }

    public static boolean isEnabled() {
        return RuneConfigs.white;
    }

    public static void takeReqs(User user) {
        user.subtractPureVis(100);
    }

    public static void execute(User user) {
        int h = user.getPlayer().getHealth();
        if (h + 10 < 21) {
            h = h + 10;
        } else {
            h = 20;
        }
        user.getPlayer().setHealth(h);
    }
}
