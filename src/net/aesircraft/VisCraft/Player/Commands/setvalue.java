package net.aesircraft.VisCraft.Player.Commands;

import net.aesircraft.VisCraft.Data.Items;
import net.aesircraft.VisCraft.Player.User;
import net.aesircraft.VisCraft.VisCraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setvalue implements CommandExecutor {

    public static VisCraft plugin;

    public setvalue(VisCraft instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender player, Command command, String cmd,
            String[] comA) {
        if (!(player instanceof Player)) {
            return true;
        }
        
        User user = new User();
        user.load((Player) player);
        if (!user.hasAdminPermission()) {
            player.sendMessage("§4You do not have permission to do that§4.");
            return true;
        }
        if (comA.length < 4) {
            return false;
        }
        if (comA[0] == null) {
            return false;
        }
        if (comA[1] == null) {
            return false;
        }
        if (comA[2] == null) {
            return false;
        } 
        if (comA[3] == null) {
            return false;
        }   
        if (!comA[0].toLowerCase().equals("water")
                && !comA[0].toLowerCase().equals("earth")
                && !comA[0].toLowerCase().equals("fire")
                && !comA[0].toLowerCase().equals("air")
                && !comA[0].toLowerCase().equals("dark")
                && !comA[0].toLowerCase().equals("pure")
                && !comA[0].toLowerCase().equals("brilliant")
                && !comA[0].toLowerCase().equals("ethereal")
                && !comA[0].toLowerCase().equals("unstable")
                && !comA[0].toLowerCase().equals("vis")) {
            player.sendMessage("§4Unknown type of §5Vis§4.");
            return true;
        }
        int vis = 0;
        try {
            vis = Integer.parseInt(comA[1]);
        } catch (NumberFormatException e) {
            return false;
        }         
        int charge = 0;
        try {
            charge = Integer.parseInt(comA[2]);
        } catch (NumberFormatException e) {
            return false;
        }  
        int quality = 0;
        try {
            quality = Integer.parseInt(comA[3]);
        } catch (NumberFormatException e) {
            return false;
        } 
        if (((Player)player).getItemInHand().getTypeId()==0){
            player.sendMessage("§4You need to be holding an item!");
            return true;
        }
        Items.setAlignment(((Player)player).getItemInHand().getTypeId(), comA[0]);
        Items.setVis(((Player)player).getItemInHand().getTypeId(), vis);
        Items.setCharge(((Player)player).getItemInHand().getTypeId(), charge);
        Items.setQuality(((Player)player).getItemInHand().getTypeId(), quality);
        player.sendMessage("§eItem stats are now set!");
        return true;
    }
}
