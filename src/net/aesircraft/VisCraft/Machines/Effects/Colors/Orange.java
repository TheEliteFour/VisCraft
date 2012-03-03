package net.aesircraft.VisCraft.Machines.Effects.Colors;

import net.aesircraft.VisCraft.Machines.Effects.RuneConfigs;
import net.aesircraft.VisCraft.Player.User;

public class Orange {

    public static String getReqs() {
        String req = "§a100 Brilliant Vis";
        return req;
    }

    public static boolean checkReqs(User user) {
        if (!user.hasRunePermission("orange")) {
            user.getPlayer().sendMessage("§4You do not have permission for this 5Vis Rune§4.");
            return false;
        }
        if (user.hasBrilliantVis(100)) {
            return true;
        }
        return false;
    }

    public static boolean isEnabled() {
        return RuneConfigs.orange;
    }

    public static void takeReqs(User user) {
        user.subtractBrilliantVis(100);
    }

    public static void execute(User user) {
        int h = user.getPlayer().getFoodLevel();
        if (h + 10 < 21) {
            h = h + 10;
        } else {
            h = 20;
        }
        user.getPlayer().setFoodLevel(h);
    }
}
