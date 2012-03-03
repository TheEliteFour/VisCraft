package net.aesircraft.VisCraft.Machines.Effects.Colors;

import net.aesircraft.VisCraft.Machines.Effects.RuneConfigs;
import net.aesircraft.VisCraft.Player.User;
import net.aesircraft.VisCraft.VisCraft;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Pink {

    public static String getReqs() {
        String req = "§b1000 Ethereal Vis";
        return req;
    }

    public static boolean checkReqs(User user) {
        if (!user.hasRunePermission("pink")) {
            user.getPlayer().sendMessage("§4You do not have permission for this 5Vis Rune§4.");
            return false;
        }
        if (user.hasEtherealVis(1000)) {
            return true;
        }
        return false;
    }

    public static boolean isEnabled() {
        return RuneConfigs.pink;
    }

    public static void takeReqs(User user) {
        user.subtractEtherealVis(1000);
    }

    public static void execute(User user) {
        teleport(user.getPlayer(), user.getPlayer().getLocation());
    }

    public static void teleport(final Player player, final Location location) {
        VisCraft.getStatic().getServer().getScheduler().scheduleSyncDelayedTask(VisCraft.getStatic(), new Runnable() {

            @Override
            public void run() {
                if (player == null) {
                    return;
                }
                if (!player.isOnline()) {
                    return;
                }
                if (player.isDead()) {
                    player.sendMessage("§4Your relocation failed because you were dead!");
                    return;
                }
                player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        }, 1200L);
    }
}
