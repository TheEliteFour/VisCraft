package net.aesircraft.VisCraft.Player.Commands;

import net.aesircraft.VisCraft.Player.User;
import net.aesircraft.VisCraft.VisCraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class vis implements CommandExecutor {

    public static VisCraft plugin;

    public vis(VisCraft instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender player, Command command, String cmd,
            String[] comA) {
        User user = new User();
        user.load((Player) player);
        user.showVis();
        return true;
    }
}
