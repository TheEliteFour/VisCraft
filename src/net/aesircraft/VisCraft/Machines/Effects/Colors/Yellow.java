package net.aesircraft.VisCraft.Machines.Effects.Colors;

import java.util.Random;
import net.aesircraft.VisCraft.Machines.Effects.RuneConfigs;
import net.aesircraft.VisCraft.Player.User;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Yellow {

    public static String getReqs() {
        String req = "§81000 Dark Vis";
        return req;
    }

    public static boolean checkReqs(User user) {
        if (!user.hasRunePermission("yellow")) {
            user.getPlayer().sendMessage("§4You do not have permission for this 5Vis Rune§4.");
            return false;
        }
        if (user.hasDarkVis(1000)) {
            return true;
        }
        return false;
    }

    public static boolean isEnabled() {
        return RuneConfigs.yellow;
    }

    public static void takeReqs(User user) {
        user.subtractDarkVis(1000);
    }

    public static void execute(User user) {
        Random random = new Random(user.getPlayer().getEntityId() + user.getPlayer().getTicksLived() + user.getPlayer().hashCode());
        int x = random.nextInt(RuneConfigs.yellowmax);
        int z = random.nextInt(RuneConfigs.yellowmax);
        int y = 70;
        World world = user.getPlayer().getWorld();
        if (random.nextInt(1) == 0) {
            x = x * -1;
        }
        if (random.nextInt(1) == 0) {
            z = z * -1;
        }
        user.getPlayer().teleport(new Location(world, x, y, z), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
}
