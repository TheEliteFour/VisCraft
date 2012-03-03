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
        if (color == 1 && DarkBlue.isEnabled()) {
            if (!DarkBlue.checkReqs(user)) {
                user.getPlayer().sendMessage("§4This §5Vis Rune§4 requires,");
                user.getPlayer().sendMessage(DarkBlue.getReqs());
                return false;
            }
            DarkBlue.takeReqs(user);
            DarkBlue.execute(user);
            return true;
        }
        if (color == 2 && DarkGreen.isEnabled()) {
            if (!DarkGreen.checkReqs(user)) {
                user.getPlayer().sendMessage("§4This §5Vis Rune§4 requires,");
                user.getPlayer().sendMessage(DarkGreen.getReqs());
                return false;
            }
            DarkGreen.takeReqs(user);
            DarkGreen.execute(user, location);
            return true;
        }
        if (color == 3 && DarkAqua.isEnabled()) {
            if (!DarkAqua.checkReqs(user)) {
                user.getPlayer().sendMessage("§4This §5Vis Rune§4 requires,");
                user.getPlayer().sendMessage(DarkAqua.getReqs());
                return false;
            }
            DarkAqua.takeReqs(user);
            DarkAqua.execute(user, location);
            return true;
        }
        if (color == 4 && DarkRed.isEnabled()) {
            if (!DarkRed.checkReqs(user)) {
                user.getPlayer().sendMessage("§4This §5Vis Rune§4 requires,");
                user.getPlayer().sendMessage(DarkRed.getReqs());
                return false;
            }
            DarkRed.takeReqs(user);
            DarkRed.execute(user);
            return true;
        }
        if (color == 5 && Purple.isEnabled()) {
            if (!Purple.checkReqs(user)) {
                user.getPlayer().sendMessage("§4This §5Vis Rune§4 requires,");
                user.getPlayer().sendMessage(Purple.getReqs());
                return false;
            }
            Purple.takeReqs(user);
            Purple.execute(user);
            return true;
        }
        if (color == 5 && Gold.isEnabled()) {
            if (!Gold.checkReqs(user, location)) {
                user.getPlayer().sendMessage("§4This §5Vis Rune§4 requires,");
                user.getPlayer().sendMessage(Gold.getReqs());
                return false;
            }
            Gold.takeReqs(user);
            Gold.execute(user, location);
            return true;
        }

        user.getPlayer().sendMessage("§4This is not a proper §5Vis Rune§4.");
        return false;
    }
}
