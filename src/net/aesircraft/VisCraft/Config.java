package net.aesircraft.VisCraft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.aesircraft.VisCraft.Machines.Effects.RuneConfigs;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

    public static boolean permissions = false;
    public static boolean spawn = true;
    public static boolean loss = true;
    public static boolean explosions = true;
    public static boolean ownership = false;
    public static boolean runes = true;

    public static void newConfig(File file) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            file.createNewFile();
        } catch (IOException ex) {
            VisCraft.logger.severe("[VisCraft] failed to write config file!");
        }
        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
            VisCraft.logger.severe("[VisCraft] failed to find config file!");
        } catch (IOException ex) {
            VisCraft.logger.severe("[VisCraft] failed to load config file!");
        } catch (InvalidConfigurationException ex) {
            VisCraft.logger.severe("[VisCraft] you fudged up your config file!");
        }
        config.set("Spawn-Monsters", "true");
        config.set("Use-Permissions", "true");
        config.set("Lose-Vis-On-Death", "true");
        config.set("Unstable-Death-Explosions", "true");
        config.set("Own-Machines", "false");
        config.set("Use-Runes", "true");
        config.set("White-Rune", "true");
        config.set("Orange-Rune", "true");
        config.set("Magenta-Rune", "true");
        config.set("Yellow-Rune", "true");
        config.set("Yellow-Max", "500000");
        config.set("LightBlue-Rune", "true");
        config.set("Pink-Rune", "true");
        config.set("Lime-Rune", "true");
        try {
            config.save(file);
        } catch (IOException ex) {
            VisCraft.logger.severe("[VisCraft] failed to write config file!");
        }
    }

    public static void loadConfig(File file) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
            VisCraft.logger.severe("[VisCraft] failed to find config file!");
        } catch (IOException ex) {
            VisCraft.logger.severe("[VisCraft] failed to load config file!");
        } catch (InvalidConfigurationException ex) {
            VisCraft.logger.severe("[VisCraft] you fudged up your config file!");
        }
        spawn = config.getBoolean("Spawn-Monsters", true);
        permissions = config.getBoolean("Use-Permissions", true);
        loss = config.getBoolean("Lose-Vis-On-Death", true);
        explosions = config.getBoolean("Unstable-Death-Explosions", true);
        ownership = config.getBoolean("Own-Machines", false);
        runes = config.getBoolean("Use-Runes", true);
        RuneConfigs.white = config.getBoolean("White-Rune", true);
        RuneConfigs.orange = config.getBoolean("Orange-Rune", true);
        RuneConfigs.magenta = config.getBoolean("Magenta-Rune", true);
        RuneConfigs.yellow = config.getBoolean("Yellow-Rune", true);
        RuneConfigs.yellowmax = config.getInt("Yellow-Max", 500000);
        RuneConfigs.lightblue = config.getBoolean("LightBlue-Rune", true);
        RuneConfigs.pink = config.getBoolean("Pink-Rune", true);
        RuneConfigs.lime = config.getBoolean("Lime-Rune", true);
    }
}
