package net.aesircraft.VisCraft.Machines.Effects.Colors;

import net.aesircraft.VisCraft.Machines.Effects.RuneConfigs;
import net.aesircraft.VisCraft.Player.User;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;

public class LightBlue {
    
    public static String getReqs(){
       String req="§b100 Ethereal Vis";
       return req;
    }
    
    public static boolean checkReqs(User user){
        if (!user.hasRunePermission("lightblue")) {
            user.getPlayer().sendMessage("§4You do not have permission for this 5Vis Rune§4.");
            return false;
        }
        if (user.hasEtherealVis(100))
            return true;
        return false;
    }
    
    public static boolean isEnabled(){
        return RuneConfigs.lightblue;
    }
    
    public static void takeReqs(User user){
        user.subtractEtherealVis(100);
    }
    
    public static void execute(User user, Location l){
        Location l2=l.getWorld().getHighestBlockAt(l).getLocation();
        user.getPlayer().teleport(new Location(l2.getWorld(),l2.getBlockX(),l2.getBlockY()+1,l2.getBlockZ()), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
    
    
}
