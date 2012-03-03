package net.aesircraft.VisCraft;

import java.io.File;
import java.util.logging.Logger;
import net.aesircraft.VisCraft.Data.Machines;
import net.aesircraft.VisCraft.Listeners.BlockListeners;
import net.aesircraft.VisCraft.Listeners.CreatureListener;
import net.aesircraft.VisCraft.Listeners.PlayerListener;
import net.aesircraft.VisCraft.Player.Commands.*;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VisCraft extends JavaPlugin {

    public static final Logger logger = Logger.getLogger("Minecraft");
    private static VisCraft instance = null;
    public static Permission permission = null;

    private Boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    public static VisCraft getStatic() {
        return instance;
    }

    private void setStatic() {
        instance = this;
    }

    @Override
    public void onDisable() {
        logger.info("VisCraft is INACTIVE!");
    }

    @Override
    public void onEnable() {
        setStatic();
        PluginManager pm = getServer().getPluginManager();
        logger.info("[VisCraft] Loaded " + this.getDescription().getName() + " build V" + this.getDescription().getVersion() + "!");
        File data = this.getDataFolder();
        File config = new File(data, "config.yml");
        if (!data.exists()) {
            data.mkdir();
        }
        if (!config.exists()) {
            Config.newConfig(config);
        }
        Config.loadConfig(config);
        if (pm.isPluginEnabled("Vault")) {
            Config.permissions = true;
        } else {
            Config.permissions = false;
        }
        if (Config.permissions) {
            setupPermissions();
        }
        new BlockListeners();
        new PlayerListener();
        new CreatureListener();
        getCommand("vis").setExecutor(new vis(this));
        getCommand("ivis").setExecutor(new ivis(this));
        getCommand("cvis").setExecutor(new cvis(this));
        getCommand("givevis").setExecutor(new gvis(this));
        getCommand("setvalue").setExecutor(new setvalue(this));
        Machines.loadExtractors();
        Machines.loadInfusers();
        Machines.loadCondensers();
        Machines.loadCollectors();
        tic();
    }

    public void tic() {
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                Machines.ticExtractors();
                Machines.ticInfusers();
                Machines.ticCondensers();
            }
        }, 0L, 1L);
    }
}
