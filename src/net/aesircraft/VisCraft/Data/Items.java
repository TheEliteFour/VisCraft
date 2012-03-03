package net.aesircraft.VisCraft.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import net.aesircraft.VisCraft.VisCraft;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Items {

    private static YamlConfiguration getFile(int id) {
        File dir = new File(VisCraft.getStatic().getDataFolder() + File.separator + "Items");
        File file = new File(VisCraft.getStatic().getDataFolder() + File.separator + "Items" + File.separator + id + ".itm");
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                VisCraft.logger.severe("[VisCraft] failed to create item " + id + "'s file!");
            }
            YamlConfiguration config = new YamlConfiguration();
            try {
                config.load(file);
            } catch (FileNotFoundException ex) {
                VisCraft.logger.severe("[VisCraft] failed to find item " + id + "'s file!");
            } catch (IOException ex) {
                VisCraft.logger.severe("[VisCraft] failed to load item " + id + "'s file!");
            } catch (InvalidConfigurationException ex) {
                VisCraft.logger.severe("[VisCraft] You fudged up item " + id + "'s file!");
            }
            config.set("vis", 10);
            config.set("charge", 100);
            config.set("quality", 100);
            Random random = new Random(Bukkit.getServer().hashCode());
            int rand = random.nextInt(3);
            String align = "";
            if (rand == 0) {
                align = "water";
            }
            if (rand == 1) {
                align = "fire";
            }
            if (rand == 2) {
                align = "earth";
            }
            if (rand == 3) {
                align = "air";
            }
            config.set("alignment", align);
            try {
                config.save(file);
            } catch (IOException ex) {
                VisCraft.logger.severe("[VisCraft] failed to save item " + id + "'s file!");
            }
        }
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
            VisCraft.logger.severe("[VisCraft] failed to find item " + id + "'s file!");
        } catch (IOException ex) {
            VisCraft.logger.severe("[VisCraft] failed to load item " + id + "'s file!");
        } catch (InvalidConfigurationException ex) {
            VisCraft.logger.severe("[VisCraft] You fudged up item " + id + "'s file!");
        }
        return config;
    }

    private static void saveConfig(YamlConfiguration config, int id) {
        try {
            config.save(new File(VisCraft.getStatic().getDataFolder() + File.separator + "Items" + File.separator + id + ".itm"));
        } catch (IOException ex) {
            VisCraft.logger.severe("[VisCraft] failed to save item " + id + "'s file!");
        }
    }

    public static int getVis(int id) {
        YamlConfiguration config = getFile(id);
        return config.getInt("vis");
    }

    public static int getCharge(int id) {
        YamlConfiguration config = getFile(id);
        return config.getInt("charge");
    }

    public static int getQuality(int id) {
        YamlConfiguration config = getFile(id);
        return config.getInt("quality");
    }

    public static String getAlignment(int id) {
        YamlConfiguration config = getFile(id);
        return config.getString("alignment");
    }

    public static void setVis(int id, int vis) {
        YamlConfiguration config = getFile(id);
        config.set("vis", vis);
        saveConfig(config, id);
    }

    public static void setCharge(int id, int charge) {
        YamlConfiguration config = getFile(id);
        config.set("charge", charge);
        saveConfig(config, id);
    }

    public static void setAlignment(int id, String alignment) {
        YamlConfiguration config = getFile(id);
        config.set("alignment", alignment);
        saveConfig(config, id);
    }

    public static void setQuality(int id, int quality) {
        YamlConfiguration config = getFile(id);
        config.set("quality", quality);
        saveConfig(config, id);
    }
}
