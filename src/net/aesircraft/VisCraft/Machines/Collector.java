package net.aesircraft.VisCraft.Machines;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.aesircraft.VisCraft.Config;
import net.aesircraft.VisCraft.Data.InventoryWorkaround;
import net.aesircraft.VisCraft.Data.Machines;
import net.aesircraft.VisCraft.Data.VisLocation;
import net.aesircraft.VisCraft.Player.User;
import net.aesircraft.VisCraft.VisCraft;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class Collector {

    private int id;
    private int vis = 0;
    private int max = 0;
    private String type = "";
    private String color = "";
    private String string = "";
    private int stage = 1;
    private Location origin;

    public int getID() {
        return id;
    }

    public void save() {
        YamlConfiguration config = getConfig();
        config.set("vis", vis);
        config.set("stage", stage);
        saveConfig(config);
    }

    private void setID(int i) {
        id = i;
    }

    private File getFile() {
        File dir = new File(VisCraft.getStatic().getDataFolder() + File.separator + "Collectors");
        File file = new File(VisCraft.getStatic().getDataFolder() + File.separator + "Collectors" + File.separator + id + ".col");
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                VisCraft.logger.severe("[VisCraft] failed to create Collector " + id + "'s file!");
            }
        }
        return file;
    }

    private File getListFile() {
        File dir = new File(VisCraft.getStatic().getDataFolder() + File.separator + "Machines");
        File file = new File(VisCraft.getStatic().getDataFolder() + File.separator + "Machines" + File.separator + "machineList" + ".vml");
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                VisCraft.logger.severe("[VisCraft] failed to create Collector List file!");
            }
            YamlConfiguration config = new YamlConfiguration();
            try {
                config.load(file);
            } catch (FileNotFoundException ex) {
                VisCraft.logger.severe("[VisCraft] failed to find Machine List file!");
            } catch (IOException ex) {
                VisCraft.logger.severe("[VisCraft] failed to load Machine List file!");
            } catch (InvalidConfigurationException ex) {
                VisCraft.logger.severe("[VisCraft] You fudged up Machine List file!");
            }
            config.set("id", 0);
            saveListConfig(config);
        }
        return file;
    }

    private YamlConfiguration getConfig() {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(getFile());
        } catch (FileNotFoundException ex) {
            VisCraft.logger.severe("[VisCraft] failed to find Collector " + id + "'s file!");
        } catch (IOException ex) {
            VisCraft.logger.severe("[VisCraft] failed to load Collector " + id + "'s file!");
        } catch (InvalidConfigurationException ex) {
            VisCraft.logger.severe("[VisCraft] You fudged up Collector " + id + "'s file!");
        }
        return config;
    }

    private void deleteConfig() {
        getFile().delete();
    }

    public void inputVis(User user) {
        if (Config.ownership) {
            if (!user.getName().toLowerCase().equals(getOwner().toLowerCase()) && !getOwner().equals("_VISNULL_")) {
                user.getPlayer().sendMessage("§4You do not own this §5Collector§4.");
            }
            return;
        }
        int userVis = 0;
        if (!user.hasPermission()) {
            user.getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
            return;
        }
        if (user.getPlayer().getItemInHand().getTypeId() != 281 && user.getPlayer().getItemInHand().getTypeId() != 325) {
            announce(user);
            return;
        }
        if (type.equals("fire")) {
            userVis = user.getFireVis();
        }
        if (type.equals("water")) {
            userVis = user.getWaterVis();
        }
        if (type.equals("earth")) {
            userVis = user.getEarthVis();
        }
        if (type.equals("air")) {
            userVis = user.getAirVis();
        }
        if (type.equals("dark")) {
            userVis = user.getDarkVis();
        }
        if (type.equals("pure")) {
            userVis = user.getPureVis();
        }
        if (type.equals("brilliant")) {
            userVis = user.getBrilliantVis();
        }
        if (type.equals("ethereal")) {
            userVis = user.getEtherealVis();
        }
        if (type.equals("unstable")) {
            userVis = user.getUnstableVis();
        }
        if (type.equals("vis")) {
            userVis = user.getVis();
        }
        int amount = 1000;
        if (user.getPlayer().getItemInHand().getTypeId() == 281) {
            if (userVis < 1000) {
                user.getPlayer().sendMessage("§4You do not have that much " + color + string + "§4.");
                return;
            }
            if (1000 + vis > max) {
                if (vis < max) {
                    amount = max - vis;
                } else {
                    user.getPlayer().sendMessage("§4The §5Collector§4 already contains to much " + color + string + "§4.");
                    return;
                }
            }
        }
        if (user.getPlayer().getItemInHand().getTypeId() == 325) {
            amount = 10000;
            if (userVis < 10000) {
                user.getPlayer().sendMessage("§4You do not have that much " + color + string + "§4.");
                return;
            }
            if (10000 + vis > max) {
                if (vis < max) {
                    amount = max - vis;
                } else {
                    user.getPlayer().sendMessage("§4The §5Collector§4 already contains to much " + color + string + "§4.");
                    return;
                }
            }
        }
        if (type.equals("fire")) {
            user.subtractFireVis(amount);
        }
        if (type.equals("water")) {
            user.subtractWaterVis(amount);
        }
        if (type.equals("earth")) {
            user.subtractEarthVis(amount);
        }
        if (type.equals("air")) {
            user.subtractAirVis(amount);
        }
        if (type.equals("dark")) {
            user.subtractDarkVis(amount);
        }
        if (type.equals("pure")) {
            user.subtractPureVis(amount);
        }
        if (type.equals("brilliant")) {
            user.subtractBrilliantVis(amount);
        }
        if (type.equals("ethereal")) {
            user.subtractEtherealVis(amount);
        }
        if (type.equals("unstable")) {
            user.subtractUnstableVis(amount);
        }
        if (type.equals("vis")) {
            user.subtractVis(amount);
        }
        user.getPlayer().sendMessage("§eYou deposited " + color + amount + " " + string + "§e.");
        vis = vis + amount;
        save();
    }

    public void extract(User user) {
        if (Config.ownership) {
            if (!user.getName().toLowerCase().equals(getOwner().toLowerCase()) && !getOwner().equals("_VISNULL_")) {
                user.getPlayer().sendMessage("§4You do not own this §5Collector§4.");
            }
            return;
        }
        int userVis = 0;
        if (!user.hasPermission()) {
            user.getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
            return;
        }
        if (user.getPlayer().getItemInHand().getTypeId() != 374 && user.getPlayer().getItemInHand().getTypeId() != 380) {
            inputVis(user);
            return;
        }
        if (type.equals("fire")) {
            userVis = user.getFireVis();
        }
        if (type.equals("water")) {
            userVis = user.getWaterVis();
        }
        if (type.equals("earth")) {
            userVis = user.getEarthVis();
        }
        if (type.equals("air")) {
            userVis = user.getAirVis();
        }
        if (type.equals("dark")) {
            userVis = user.getDarkVis();
        }
        if (type.equals("pure")) {
            userVis = user.getPureVis();
        }
        if (type.equals("brilliant")) {
            userVis = user.getBrilliantVis();
        }
        if (type.equals("ethereal")) {
            userVis = user.getEtherealVis();
        }
        if (type.equals("unstable")) {
            userVis = user.getUnstableVis();
        }
        if (type.equals("vis")) {
            userVis = user.getVis();
        }
        int amount = 1000;
        if (user.getPlayer().getItemInHand().getTypeId() == 374) {
            if (userVis + 1000 > 500000) {
                if (userVis < 500000) {
                    amount = 500000 - userVis;
                } else {
                    user.getPlayer().sendMessage("§4You already carry to much " + color + string + "§4.");
                    return;
                }
            }
        }
        if (user.getPlayer().getItemInHand().getTypeId() == 380) {
            amount = 10000;
            if (userVis + 10000 > 500000) {
                if (userVis < 500000) {
                    amount = 500000 - userVis;
                } else {
                    user.getPlayer().sendMessage("§4You already carry to much " + color + string + "§4.");
                    return;
                }
            }
        }
        if (user.getPlayer().getItemInHand().getTypeId() == 374) {
            if (1000 > vis) {
                if (1000 - vis < 1 || vis < 1) {
                    user.getPlayer().sendMessage("§4This §5Collector§4 has no " + color + string + "§4.");
                    return;
                }
                amount = 1000 - vis;
            }
        }
        if (user.getPlayer().getItemInHand().getTypeId() == 380) {
            if (10000 > vis) {
                if (10000 - vis < 1) {
                    user.getPlayer().sendMessage("§4This §5Collector§4 has no " + color + string + "§4.");
                    return;
                }
                amount = 10000 - vis;
            }
        }
        if (type.equals("fire")) {
            user.addFireVis(amount);
        }
        if (type.equals("water")) {
            user.addWaterVis(amount);
        }
        if (type.equals("earth")) {
            user.addEarthVis(amount);
        }
        if (type.equals("air")) {
            user.addAirVis(amount);
        }
        if (type.equals("dark")) {
            user.addDarkVis(amount);
        }
        if (type.equals("pure")) {
            user.addPureVis(amount);
        }
        if (type.equals("brilliant")) {
            user.addBrilliantVis(amount);
        }
        if (type.equals("ethereal")) {
            user.addEtherealVis(amount);
        }
        if (type.equals("unstable")) {
            user.addUnstableVis(amount);
        }
        if (type.equals("vis")) {
            user.addVis(amount);
        }
        user.getPlayer().sendMessage("§eYou withdrew " + color + amount + " " + string + "§e.");
        vis = vis - amount;
        save();
    }

    private int getNewID() {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(getListFile());
        } catch (FileNotFoundException ex) {
            VisCraft.logger.severe("[VisCraft] failed to find Collector List file!");
        } catch (IOException ex) {
            VisCraft.logger.severe("[VisCraft] failed to load Collector List file!");
        } catch (InvalidConfigurationException ex) {
            VisCraft.logger.severe("[VisCraft] You fudged up Collector List file!");
        }
        int id = config.getInt("id", 0);
        config.set("id", id + 1);
        saveListConfig(config);
        return id;
    }

    private void saveConfig(YamlConfiguration config) {
        try {
            config.save(getFile());
        } catch (IOException ex) {
            VisCraft.logger.severe("[VisCraft] failed to save Collector " + id + "'s file!");
        }
    }

    private void saveListConfig(YamlConfiguration config) {
        try {
            config.save(getListFile());
        } catch (IOException ex) {
            VisCraft.logger.severe("[VisCraft] failed to save Machine List file!");
        }
    }

    public boolean checkSignature(Location l) {
        VisLocation v = new VisLocation();
        v.loadFromLocation(l);
        if (Machines.protectedBlocks.containsKey(v.toString())) {
            return false;
        }
        Block b1 = l.getBlock();
        Block b2 = l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY() - 1, l.getBlockZ());
        Block b3 = l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ());
        if (b1.getTypeId() == 35 && b2.getTypeId() == 49 && b3.getTypeId() == 49 && getSignatureCheck(l) != -1) {
            return true;
        }
        return false;
    }

    public int getVis() {
        save();
        return vis;
    }

    public void announce(User user) {
        user.getPlayer().sendMessage("§eThis §5Collector§e has " + color + getVis() + " " + string + "§e.");
    }

    public void setVis(int amount) {
        vis = amount;
        save();
    }

    public void addVis(int amount) {
        setVis(getVis() + amount);
    }

    public void subtractVis(int amount) {
        setVis(getVis() - amount);
    }

    public void upgrade(User user) {
        if (stage == 3) {
            user.getPlayer().sendMessage("§4This §5Collector§4 is already at §bStage 3§4.");
            return;
        }
        VisLocation v = new VisLocation();
        ItemStack[] items = new ItemStack[4];
        if (stage == 1) {
            items[0] = new ItemStack(49, 32);
            items[1] = new ItemStack(266, 16);
            items[2] = new ItemStack(264, 10);
            items[3] = new ItemStack(331, 32);
            if (!(InventoryWorkaround.containsItem(user.getPlayer().getInventory(), false, items) && user.hasBrilliantVis(500))) {
                user.getPlayer().sendMessage("§4You need,");
                user.getPlayer().sendMessage("§410 §b Diamond§4, 32 §bObsidian§4, 16 §bGold Ingots§4, and 32§b Redstone");
                user.getPlayer().sendMessage("§5500 §aBrilliant Condensed Vis");
                user.getPlayer().sendMessage("§4To upgrade this §5Collector§4 to §bStage 2§4.");
                return;
            }
            InventoryWorkaround.removeItem(user.getPlayer().getInventory(), true, items);
            user.subtractBrilliantVis(500);
            stage = 2;
            v.loadFromString(signBlock());
            ((Sign) v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getState()).setLine(1, "Stage 2");
            ((Sign) v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getState()).update();
            save();
            return;
        }
        items[0] = new ItemStack(49, 64);
        items[1] = new ItemStack(266, 32);
        items[2] = new ItemStack(264, 20);
        items[3] = new ItemStack(331, 64);
        if (!(InventoryWorkaround.containsItem(user.getPlayer().getInventory(), false, items) && user.hasBrilliantVis(1000))) {
            user.getPlayer().sendMessage("§4You need,");
            user.getPlayer().sendMessage("§420 §b Diamond§4, 64 §bObsidian§4, 32 §bGold Ingots§4, and 64§b Redstone");
            user.getPlayer().sendMessage("§51000 §aBrilliant Condensed Vis");
            user.getPlayer().sendMessage("§4To upgrade this §5Collector§4 to §bStage 3§4.");
            return;
        }
        InventoryWorkaround.removeItem(user.getPlayer().getInventory(), true, items);
        user.subtractBrilliantVis(1000);
        stage = 3;
        v.loadFromString(signBlock());
        ((Sign) v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getState()).setLine(1, "Stage 3");
        ((Sign) v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getState()).update();
        save();
        setMax();
    }

    public String getOwner() {
        YamlConfiguration config = getConfig();
        return config.getString("owner");
    }

    public boolean remove(User user) {
        if (Config.ownership) {
            if (!user.getName().toLowerCase().equals(getOwner().toLowerCase()) && !getOwner().equals("_VISNULL_")) {
                return false;
            }
        }
        removeProtectedBlocks();
        removeSignBlock();
        Machines.collectors.remove(id);
        Machines.machines.remove(id);
        deleteConfig();
        return true;
    }

    public void newCollector(User user, Location location) {
        ItemStack[] items = new ItemStack[4];
        items[0] = new ItemStack(49, 10);
        items[1] = new ItemStack(266, 5);
        items[2] = new ItemStack(264, 1);
        items[3] = new ItemStack(331, 10);
        if (!InventoryWorkaround.containsItem(user.getPlayer().getInventory(), false, items) && user.hasVis(1000)) {
            user.getPlayer().sendMessage("§4You need,");
            user.getPlayer().sendMessage("§41 §b Diamond§4, 10 §bObsidian§4, 5 §bGold Ingots§4, and 10§b Redstone");
            user.getPlayer().sendMessage("§51000 Vis");
            user.getPlayer().sendMessage("§4To construct a §5ECollector§4.");
            return;
        }
        if (!checkBlocks(location)) {
            user.getPlayer().sendMessage("§4The machine is obstructed.");
            return;
        }
        InventoryWorkaround.removeItem(user.getPlayer().getInventory(), false, items);
        user.subtractVis(1000);
        form(location);
        newConfig(user);
        storeProtectedBlocks();
        storeSignBlock();
        Machines.collectors.put(id, this);
        Machine machine = new Machine();
        machine.Machine(this);
        Machines.machines.put(id, machine);
        user.getPlayer().sendMessage("§eThe §5Collector §eforms before your eyes.");
    }

    public void loadCollector(int fid) {
        id = fid;
        loadConfig();
        storeProtectedBlocks();
        storeSignBlock();
        setMax();
        getSignature(origin);
        Machines.collectors.put(id, this);
        Machine machine = new Machine();
        machine.Machine(this);
        Machines.machines.put(id, machine);
    }

    private void storeProtectedBlocks() {
        List<String> list = protectedBlocks();
        for (int ctr = 0; ctr < list.size(); ctr++) {
            Machines.protectedBlocks.put(list.get(ctr), id);
        }
    }

    private void removeProtectedBlocks() {
        List<String> list = protectedBlocks();
        for (int ctr = 0; ctr < list.size(); ctr++) {
            Machines.protectedBlocks.remove(list.get(ctr));
        }
    }

    private void storeSignBlock() {
        Machines.signBlocks.add(signBlock());
    }

    private void removeSignBlock() {
        Machines.signBlocks.remove(signBlock());
    }

    private void form(Location l) {
        l = new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
        origin = l;
        List<String> list = obsidianBlocks();
        VisLocation v = new VisLocation();
        for (int ctr = 0; ctr < list.size(); ctr++) {
            v.loadFromString(list.get(ctr));
            l.getWorld().getBlockAt(v.convertToLocation()).setTypeId(49);
        }

        list = clothBlocks();
        for (int ctr = 0; ctr < list.size(); ctr++) {
            v.loadFromString(list.get(ctr));
            l.getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(35, ((byte) getSignature(l)), true);
        }
        v.loadFromString(signBlock());
        l.getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(68, ((byte) 3), true);
        Sign sign = (Sign) l.getWorld().getBlockAt(v.convertToLocation()).getState();
        sign.setLine(0, "§5--Collector--");
        sign.setLine(1, "Stage 1");
        id = getNewID();
        sign.setLine(2, "" + id);
        sign.setLine(3, "" + color + type);
        sign.update();
        setMax();

    }

    private void setMax() {
        if (max != 0) {
            return;
        }
        if (stage == 1) {
            max = 1000000;
        }
        if (stage == 2) {
            max = 10000000;
        }
        if (stage == 3) {
            max = 100000000;
        }
    }

    private void newConfig(User user) {
        YamlConfiguration config = getConfig();
        config.set("stage", 1);
        VisLocation v = new VisLocation();
        v.loadFromLocation(origin);
        config.set("origin", v.toString());
        config.set("vis", 0);
        if (Config.ownership) {
            config.set("owner", user.getName());
        } else {
            config.set("owner", "_VISNULL_");
        }
        saveConfig(config);
    }

    private void loadConfig() {
        YamlConfiguration config = getConfig();
        stage = config.getInt("stage", 1);
        VisLocation v = new VisLocation();
        v.loadFromString(config.getString("origin"));
        origin = v.convertToLocation();
        vis = config.getInt("vis", 0);
    }

    private boolean checkBlocks(Location l) {
        List<Location> list = new ArrayList<Location>();

        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() + 2));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() + 1));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY(), l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY(), l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() + 1));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() + 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() + 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 1));

        for (int ctr = 0; ctr < list.size(); ctr++) {
            if (l.getWorld().getBlockAt(list.get(ctr)) == null) {
                continue;
            }
            if (l.getWorld().getBlockAt(list.get(ctr)).isEmpty()) {
                continue;
            }
            if (l.getWorld().getBlockAt(list.get(ctr)).getTypeId() == 0) {
                continue;
            }
            return false;
        }

        return true;
    }

    private List<String> obsidianBlocks() {
        Location l = origin;
        l = new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
        List<String> list = new ArrayList<String>();
        VisLocation v = new VisLocation();
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() + 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() + 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 1));
        list.add(v.toString());
        return list;
    }

    private int getSignature(Location l) {
        int id = ((int) l.getWorld().getBlockAt(l).getData());
        int check = 0;
        if (id == 0) {
            type = "pure";
            color = "§f";
            string = "Pure Condensed Vis";
            check = 1;
        }
        if (id == 1) {
            type = "air";
            color = "§6";
            string = "Air Infused Vis";
            check = 1;
        }
        if (id == 3) {
            type = "ethereal";
            color = "§3";
            string = "Ethereal Condensed Vis";
            check = 1;
        }
        if (id == 5) {
            type = "earth";
            color = "§2";
            string = "Earth Infused Vis";
            check = 1;
        }
        if (id == 7) {
            type = "dark";
            color = "§8";
            string = "Dark Condensed Vis";
            check = 1;
        }

        if (id == 10) {
            type = "vis";
            color = "§5";
            string = "Vis";
            check = 1;
        }

        if (id == 14) {
            type = "fire";
            color = "§4";
            string = "Fire Infused Vis";
            check = 1;
        }
        if (id == 15) {
            type = "unstable";
            color = "§c";
            string = "Unstable Condensed Vis";
            check = 1;
        }
        if (id == 13) {
            type = "brilliant";
            color = "§a";
            string = "Brilliant Condensed Vis";
            check = 1;
        }

        if (check == 1) {
            return id;
        }
        return -1;
    }

    private int getSignatureCheck(Location l) {
        int id = ((int) l.getWorld().getBlockAt(l).getData());
        int check = 0;
        if (id == 0) {
            check = 1;
        }
        if (id == 1) {
            check = 1;
        }
        if (id == 3) {
            check = 1;
        }
        if (id == 5) {
            check = 1;
        }
        if (id == 7) {
            check = 1;
        }

        if (id == 10) {
            check = 1;
        }

        if (id == 14) {
            check = 1;
        }
        if (id == 15) {
            check = 1;
        }
        if (id == 13) {
            check = 1;
        }

        if (check == 1) {
            return id;
        }
        return -1;
    }

    private List<String> clothBlocks() {
        Location l = origin;
        l = new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
        List<String> list = new ArrayList<String>();
        VisLocation v = new VisLocation();
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY(), l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY(), l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() + 1));
        list.add(v.toString());
        return list;
    }

    private String signatureBlock() {
        Location l = origin;
        l = new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
        VisLocation v = new VisLocation();
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        return v.toString();
    }

    private String signBlock() {
        Location l = origin;
        l = new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
        VisLocation v = new VisLocation();
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() + 2));
        return v.toString();
    }

    private List<String> protectedBlocks() {
        Location l = origin;
        List<String> list = new ArrayList<String>();
        VisLocation v = new VisLocation();
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY(), l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY(), l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() + 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() + 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 1));
        list.add(v.toString());


        return list;
    }
}
