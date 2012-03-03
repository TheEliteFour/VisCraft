package net.aesircraft.VisCraft.Listeners;

import net.aesircraft.VisCraft.Player.User;
import net.aesircraft.VisCraft.VisCraft;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {

    public PlayerListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, VisCraft.getStatic());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        User user = new User();
        user.load(event.getPlayer());
        user.checkInteract(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignChange(final SignChangeEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getLine(0) != null) {
            if (event.getLine(0).contains("§5--")) {
                event.setLine(0, "§5VisCraft - NO");
                event.setCancelled(true);
            }
        }
    }
}
