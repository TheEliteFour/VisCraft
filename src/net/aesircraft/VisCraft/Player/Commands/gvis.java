package net.aesircraft.VisCraft.Player.Commands;

import net.aesircraft.VisCraft.Player.User;
import net.aesircraft.VisCraft.VisCraft;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class gvis implements CommandExecutor {

    public static VisCraft plugin;

    public gvis(VisCraft instance) {
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
        if (!user.hasPermission()) {
            player.sendMessage("§4You do not have permission to use §5VisCraft§4.");
            return true;
        }
        if (comA.length < 3) {
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
        if (Bukkit.getPlayer(comA[0]) == null) {
            return false;
        }
        if (!Bukkit.getPlayer(comA[0]).isOnline()) {
            return false;
        }
        if (!comA[1].toLowerCase().equals("water")
                && !comA[1].toLowerCase().equals("earth")
                && !comA[1].toLowerCase().equals("fire")
                && !comA[1].toLowerCase().equals("air")
                && !comA[1].toLowerCase().equals("dark")
                && !comA[1].toLowerCase().equals("pure")
                && !comA[1].toLowerCase().equals("brilliant")
                && !comA[1].toLowerCase().equals("ethereal")
                && !comA[1].toLowerCase().equals("unstable")
                && !comA[1].toLowerCase().equals("vis")) {
            player.sendMessage("§4Unknown type of §5Vis§4.");
            return true;
        }
        int vis = 0;
        try {
            vis = Integer.parseInt(comA[2]);
        } catch (NumberFormatException e) {
            return false;
        }
        if (vis == 0) {
            return false;
        }
        User u2 = new User();
        u2.load(Bukkit.getPlayer(comA[0]));
        String color = "§5";
        String name = "Vis";
        if (comA[1].toLowerCase().equals("water")) {
            if (!user.hasWaterVis(vis)) {
                player.sendMessage("§4You do not have that much§4.");
                return true;
            }

            if (!user.hasRoomForWaterVis(vis)) {
                player.sendMessage("§4They do not have enough space for that much§4.");
                return true;
            }
            color = "§1";
            name = "Water Infused Vis";
            player.sendMessage("§eYou sent §b" + comA[0] + " " + color + vis + " " + name + "§e.");
            u2.getPlayer().sendMessage("§b" + player.getName() + "§e sent you " + color + vis + " " + name + "§e.");
            user.subtractWaterVis(vis);
            u2.addWaterVis(vis);
            return true;

        }
        if (comA[1].toLowerCase().equals("earth")) {
            if (!user.hasEarthVis(vis)) {
                player.sendMessage("§4You do not have that much§4.");
                return true;
            }

            if (!user.hasRoomForEarthVis(vis)) {
                player.sendMessage("§4They do not have enough space for that much§4.");
                return true;
            }
            color = "§2";
            name = "Earth Infused Vis";
            player.sendMessage("§eYou sent §b" + comA[0] + " " + color + vis + " " + name + "§e.");
            u2.getPlayer().sendMessage("§b" + player.getName() + "§e sent you " + color + vis + " " + name + "§e.");
            user.subtractEarthVis(vis);
            u2.addEarthVis(vis);
            return true;

        }
        if (comA[1].toLowerCase().equals("fire")) {
            if (!user.hasFireVis(vis)) {
                player.sendMessage("§4You do not have that much§4.");
                return true;
            }

            if (!user.hasRoomForFireVis(vis)) {
                player.sendMessage("§4They do not have enough space for that much§4.");
                return true;
            }
            color = "§4";
            name = "Fire Infused Vis";
            player.sendMessage("§eYou sent §b" + comA[0] + " " + color + vis + " " + name + "§e.");
            u2.getPlayer().sendMessage("§b" + player.getName() + "§e sent you " + color + vis + " " + name + "§e.");
            user.subtractFireVis(vis);
            u2.addFireVis(vis);
            return true;

        }
        if (comA[1].toLowerCase().equals("air")) {
            if (!user.hasAirVis(vis)) {
                player.sendMessage("§4You do not have that much§4.");
                return true;
            }

            if (!user.hasRoomForAirVis(vis)) {
                player.sendMessage("§4They do not have enough space for that much§4.");
                return true;
            }
            color = "§6";
            name = "Air Infused Vis";
            player.sendMessage("§eYou sent §b" + comA[0] + " " + color + vis + " " + name + "§e.");
            u2.getPlayer().sendMessage("§b" + player.getName() + "§e sent you " + color + vis + " " + name + "§e.");
            user.subtractAirVis(vis);
            u2.addAirVis(vis);
            return true;

        }
        if (comA[1].toLowerCase().equals("dark")) {
            if (!user.hasDarkVis(vis)) {
                player.sendMessage("§4You do not have that much§4.");
                return true;
            }

            if (!user.hasRoomForDarkVis(vis)) {
                player.sendMessage("§4They do not have enough space for that much§4.");
                return true;
            }
            color = "§8";
            name = "Dark Condensed Vis";
            player.sendMessage("§eYou sent §b" + comA[0] + " " + color + vis + " " + name + "§e.");
            u2.getPlayer().sendMessage("§b" + player.getName() + "§e sent you " + color + vis + " " + name + "§e.");
            user.subtractDarkVis(vis);
            u2.addDarkVis(vis);
            return true;

        }
        if (comA[1].toLowerCase().equals("pure")) {
            if (!user.hasPureVis(vis)) {
                player.sendMessage("§4You do not have that much§4.");
                return true;
            }

            if (!user.hasRoomForPureVis(vis)) {
                player.sendMessage("§4They do not have enough space for that much§4.");
                return true;
            }
            color = "§f";
            name = "Pure Condensed Vis";
            player.sendMessage("§eYou sent §b" + comA[0] + " " + color + vis + " " + name + "§e.");
            u2.getPlayer().sendMessage("§b" + player.getName() + "§e sent you " + color + vis + " " + name + "§e.");
            user.subtractPureVis(vis);
            u2.addPureVis(vis);
            return true;

        }
        if (comA[1].toLowerCase().equals("brilliant")) {
            if (!user.hasBrilliantVis(vis)) {
                player.sendMessage("§4You do not have that much§4.");
                return true;
            }

            if (!user.hasRoomForBrilliantVis(vis)) {
                player.sendMessage("§4They do not have enough space for that much§4.");
                return true;
            }
            color = "§a";
            name = "Brilliant Condensed Vis";
            player.sendMessage("§eYou sent §b" + comA[0] + " " + color + vis + " " + name + "§e.");
            u2.getPlayer().sendMessage("§b" + player.getName() + "§e sent you " + color + vis + " " + name + "§e.");
            user.subtractBrilliantVis(vis);
            u2.addBrilliantVis(vis);
            return true;

        }
        if (comA[1].toLowerCase().equals("ethereal")) {
            if (!user.hasEtherealVis(vis)) {
                player.sendMessage("§4You do not have that much§4.");
                return true;
            }

            if (!user.hasRoomForEtherealVis(vis)) {
                player.sendMessage("§4They do not have enough space for that much§4.");
                return true;
            }
            color = "§b";
            name = "Ethereal Condensed Vis";
            player.sendMessage("§eYou sent §b" + comA[0] + " " + color + vis + " " + name + "§e.");
            u2.getPlayer().sendMessage("§b" + player.getName() + "§e sent you " + color + vis + " " + name + "§e.");
            user.subtractEtherealVis(vis);
            u2.addEtherealVis(vis);
            return true;

        }
        if (comA[1].toLowerCase().equals("unstable")) {
            if (!user.hasUnstableVis(vis)) {
                player.sendMessage("§4You do not have that much§4.");
                return true;
            }

            if (!user.hasRoomForUnstableVis(vis)) {
                player.sendMessage("§4They do not have enough space for that much§4.");
                return true;
            }
            color = "§c";
            name = "Unstable Condensed Vis";
            player.sendMessage("§eYou sent §b" + comA[0] + " " + color + vis + " " + name + "§e.");
            u2.getPlayer().sendMessage("§b" + player.getName() + "§e sent you " + color + vis + " " + name + "§e.");
            user.subtractUnstableVis(vis);
            u2.addUnstableVis(vis);
            return true;

        }
        if (comA[1].toLowerCase().equals("vis")) {
            if (!user.hasVis(vis)) {
                player.sendMessage("§4You do not have that much§4.");
                return true;
            }

            if (!user.hasRoomForVis(vis)) {
                player.sendMessage("§4They do not have enough space for that much§4.");
                return true;
            }
            player.sendMessage("§eYou sent §b" + comA[0] + " " + color + vis + " " + name + "§e.");
            u2.getPlayer().sendMessage("§b" + player.getName() + "§e sent you " + color + vis + " " + name + "§e.");
            user.subtractVis(vis);
            u2.addVis(vis);

        }
        return true;
    }
}
