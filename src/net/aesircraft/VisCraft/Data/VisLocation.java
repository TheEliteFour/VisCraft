package net.aesircraft.VisCraft.Data;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class VisLocation {

    private int y;
    private int x;
    private int z;
    private String world;

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }

    public void loadFromLocation(Location loc) {
        y = loc.getBlockY();
        x = loc.getBlockX();
        z = loc.getBlockZ();
        world = loc.getWorld().getName();
    }

    public Location convertToLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    @Override
    public String toString() {
        return world + ":" + x + ":" + y + ":" + z;
    }

    public void loadFromString(String string) {
        String[] list = string.split(":");
        world = list[0];
        x = Integer.parseInt(list[1]);
        y = Integer.parseInt(list[2]);
        z = Integer.parseInt(list[3]);
    }
}
