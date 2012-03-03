package net.aesircraft.VisCraft.Machines.Effects;

import net.aesircraft.VisCraft.Machines.Effects.Colors.*;
import net.aesircraft.VisCraft.Player.User;
import org.bukkit.Location;

public class RuneEffect {

    private int color = 0;
    private Location location;

    public void load(Location l) {
        Location l2 = new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ());
        if (l2.getBlock() == null) {
            return;
        }
        if (l2.getBlock().getTypeId() == 0) {
            return;
        }
        location = l;
        color = l2.getBlock().getData();
    }

    public boolean execute(User user) {
        if (color == 0 && White.isEnabled()) {
            if (!White.checkReqs(user)) {
                user.getPlayer().sendMessage("§4This §5Vis Rune§4 requires,");
                user.getPlayer().sendMessage(White.getReqs());
                return false;
            }
            White.takeReqs(user);
            White.execute(user);
            return true;
        }
        if (color == 1 && Orange.isEnabled()) {
            if (!Orange.checkReqs(user)) {
                user.getPlayer().sendMessage("§4This §5Vis Rune§4 requires,");
                user.getPlayer().sendMessage(Orange.getReqs());
                return false;
            }
            Orange.takeReqs(user);
            Orange.execute(user);
            return true;
        }
        if (color == 2 && Magenta.isEnabled()) {
            if (!Magenta.checkReqs(user)) {
                user.getPlayer().sendMessage("§4This §5Vis Rune§4 requires,");
                user.getPlayer().sendMessage(Magenta.getReqs());
                return false;
            }
            Magenta.takeReqs(user);
            Magenta.execute(user, location);
            return true;
        }
        if (color == 3 && LightBlue.isEnabled()) {
            if (!LightBlue.checkReqs(user)) {
                user.getPlayer().sendMessage("§4This §5Vis Rune§4 requires,");
                user.getPlayer().sendMessage(LightBlue.getReqs());
                return false;
            }
            LightBlue.takeReqs(user);
            LightBlue.execute(user, location);
            return true;
        }
        if (color == 4 && Yellow.isEnabled()) {
            if (!Yellow.checkReqs(user)) {
                user.getPlayer().sendMessage("§4This §5Vis Rune§4 requires,");
                user.getPlayer().sendMessage(Yellow.getReqs());
                return false;
            }
            Yellow.takeReqs(user);
            Yellow.execute(user);
            return true;
        }
        if (color == 5 && Pink.isEnabled()) {
            if (!Pink.checkReqs(user)) {
                user.getPlayer().sendMessage("§4This §5Vis Rune§4 requires,");
                user.getPlayer().sendMessage(Pink.getReqs());
                return false;
            }
            Pink.takeReqs(user);
            Pink.execute(user);
            return true;
        }
        if (color == 5 && Lime.isEnabled()) {
            if (!Lime.checkReqs(user, location)) {
                user.getPlayer().sendMessage("§4This §5Vis Rune§4 requires,");
                user.getPlayer().sendMessage(Lime.getReqs());
                return false;
            }
            Lime.takeReqs(user);
            Lime.execute(user, location);
            return true;
        }

        user.getPlayer().sendMessage("§4This is not a proper §5Vis Rune§4.");
        return false;
    }
}
