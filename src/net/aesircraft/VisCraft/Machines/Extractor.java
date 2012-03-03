package net.aesircraft.VisCraft.Machines;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.aesircraft.VisCraft.Config;
import net.aesircraft.VisCraft.Data.InventoryWorkaround;
import net.aesircraft.VisCraft.Data.Items;
import net.aesircraft.VisCraft.Data.Machines;
import net.aesircraft.VisCraft.Data.VisLocation;
import net.aesircraft.VisCraft.Player.User;
import net.aesircraft.VisCraft.VisCraft;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class Extractor {

    private int id;
    private int vis = 0;
    private int charge = 0;
    private int tics = 0;
    private int lived = 0;
    private int stage = 1;
    private boolean state = false;
    private Location origin;

    public int getID() {
        return id;
    }

    private void rechargeCharge() {
        VisLocation v = new VisLocation();
        List<String> list = fuelBlocks();
        for (int ctr = 0; ctr < list.size() && charge == 0; ctr++) {
            v.loadFromString(list.get(ctr));
            if (v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getTypeId() == 11) {
                v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).setType(Material.AIR);
                charge = charge + 15000;
            }
        }
    }

    private void rechargeTics() {
        VisLocation v = new VisLocation();
        v.loadFromString(chestBlock());
        Chest c = (Chest) origin.getWorld().getBlockAt(v.convertToLocation()).getState();
        ItemStack[] items = c.getInventory().getContents();
        for (int ctr = 0; ctr < items.length && tics == 0; ctr++) {
            if (items[ctr] != null) {
                if (items[ctr].getTypeId() != 0) {
                    if (Items.getVis(items[ctr].getTypeId()) == -1) {
                        continue;
                    }
                    tics = tics + Items.getVis(items[ctr].getTypeId());
                    if (items[ctr].getAmount() == 1) {
                        c.getInventory().remove(items[ctr]);
                    } else {
                        items[ctr].setAmount(items[ctr].getAmount() - 1);
                    }
                }
            }
        }
    }

    public void tic() {
        lived++;
        if (lived == 36000) {
            save();
            lived = 0;
        }
        VisLocation v = new VisLocation();
        Random random = new Random(this.hashCode());
        if (Config.spawn && random.nextInt(500) == random.nextInt(500)) {
            v.loadFromString(redstoneBlock());
            LivingEntity spawnCreature = v.convertToLocation().getWorld().spawnCreature(v.convertToLocation(), EntityType.SLIME);
            Machines.safeSpawns.add(spawnCreature.getEntityId());
        }
        v.loadFromString(redstoneBlock());
        if (vis < 100000 && stage == 1 && v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getTypeId() != 0) {
            v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(0, (byte) 0, true);
        }
        if (vis < 1000000 && stage == 2 && v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getTypeId() != 0) {
            v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(0, (byte) 0, true);
        }
        if (vis < 10000000 && stage == 3 && v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getTypeId() != 0) {
            v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(0, (byte) 0, true);
        }
        if (charge == 0) {
            rechargeCharge();
            if (charge == 0) {
                state = false;
                save();
                return;
            }
        }
        if (tics == 0) {

            rechargeTics();
            if (tics == 0) {
                state = false;
                save();
                return;
            }
        }


        if (stage == 1) {
            if (vis >= 100000) {
                v.loadFromString(redstoneBlock());
                v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(76, ((byte) 5), true);
                state = false;
                save();
                return;
            }
            tics--;
            charge--;
            vis++;

            return;
        }
        if (stage == 2) {
            if (vis >= 1000000) {
                v.loadFromString(redstoneBlock());
                v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(76, ((byte) 5), true);
                state = false;
                save();
                return;
            }
            int takeTics = 5;
            if (tics < 5) {
                int tempTics;
                while (tics < 5) {
                    tempTics = tics;
                    rechargeTics();
                    if (tempTics == tics) {
                        takeTics = tics;
                        break;
                    }
                }
            }
            if (vis + takeTics > 1000000) {
                takeTics = (takeTics + vis) - 1000000;
            }
            tics = tics - takeTics;
            if (charge < 50) {
                int tempCharge;
                while (charge < 50) {
                    tempCharge = charge;
                    rechargeCharge();
                    if (tempCharge == charge) {
                        state = false;
                        save();
                        return;
                    }
                }
            }
            charge = charge - 50;
            vis = vis + takeTics;
        }

        if (stage == 3) {
            if (vis >= 10000000) {
                v.loadFromString(redstoneBlock());
                v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(76, ((byte) 5), true);
                state = false;
                save();
                return;
            }
            int takeTics = 80;
            if (tics < 80) {
                int tempTics;
                while (tics < 80) {
                    tempTics = tics;
                    rechargeTics();
                    if (tempTics == tics) {
                        takeTics = tics;
                        break;
                    }
                }
            }
            if (vis + takeTics > 10000000) {
                takeTics = (takeTics + vis) - 10000000;
            }
            tics = tics - takeTics;
            if (charge < 100) {
                int tempCharge;
                while (charge < 100) {
                    tempCharge = charge;
                    rechargeCharge();
                    if (tempCharge == charge) {
                        state = false;
                        save();
                        return;
                    }
                }
            }
            charge = charge - 100;
            vis = vis + takeTics;

        }



    }

    public void save() {
        YamlConfiguration config = getConfig();
        config.set("vis", vis);
        config.set("charge", charge);
        config.set("tics", tics);
        config.set("stage", stage);
        saveConfig(config);
    }

    private void setID(int i) {
        id = i;
    }

    private File getFile() {
        File dir = new File(VisCraft.getStatic().getDataFolder() + File.separator + "Extractors");
        File file = new File(VisCraft.getStatic().getDataFolder() + File.separator + "Extractors" + File.separator + id + ".ext");
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                VisCraft.logger.severe("[VisCraft] failed to create Extractor " + id + "'s file!");
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
                VisCraft.logger.severe("[VisCraft] failed to create Extractor List file!");
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
            VisCraft.logger.severe("[VisCraft] failed to find Extractor " + id + "'s file!");
        } catch (IOException ex) {
            VisCraft.logger.severe("[VisCraft] failed to load Extractor " + id + "'s file!");
        } catch (InvalidConfigurationException ex) {
            VisCraft.logger.severe("[VisCraft] You fudged up Extractor " + id + "'s file!");
        }
        return config;
    }

    private void deleteConfig() {
        getFile().delete();
    }

    public void turnOn() {
        state = true;
    }

    public void extract(User user) {
        if (Config.ownership) {
            if (!user.getName().toLowerCase().equals(getOwner().toLowerCase()) && !getOwner().equals("_VISNULL_")) {
                user.getPlayer().sendMessage("§4You do not own this §5Extractor§4.");
            }
            return;
        }
        int userVis = user.getVis();
        if (!user.hasPermission()) {
            user.getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
            return;
        }
        if (userVis > 500000) {
            user.getPlayer().sendMessage("§4You already carry to much §5Vis§4.");
            return;
        }
        int amount;
        if ((userVis + vis) > 500000) {
            user.setVis(500000);

            vis = vis - (500000 - userVis);
            amount = 500000 - userVis;
        } else {
            user.addVis(vis);
            amount = vis;
            vis = 0;
        }
        if (!state) {
            state = true;
        }
        if (amount == 0) {
            user.getPlayer().sendMessage("§4This §5Extractor§4 has no §5Vis§4.");
            return;
        }
        user.getPlayer().sendMessage("§eYou have withdrawn §5" + amount + " Vis§e.");
        save();
    }

    private int getNewID() {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(getListFile());
        } catch (FileNotFoundException ex) {
            VisCraft.logger.severe("[VisCraft] failed to find Machine List file!");
        } catch (IOException ex) {
            VisCraft.logger.severe("[VisCraft] failed to load Machine List file!");
        } catch (InvalidConfigurationException ex) {
            VisCraft.logger.severe("[VisCraft] You fudged up Machine List file!");
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
            VisCraft.logger.severe("[VisCraft] failed to save Extractor " + id + "'s file!");
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
        if (b1.getTypeId() == 49 && b2.getTypeId() == 49 && b3.getTypeId() == 54) {
            return true;
        }
        return false;
    }

    public int getVis() {
        save();
        return vis;
    }

    public void announce(User user) {
        user.getPlayer().sendMessage("§eThis §5Extractor§e has §5" + getVis() + " Vis§e and §2Powered§e: §b" + state + "§4.");
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
            user.getPlayer().sendMessage("§4This §5Extractor§4 is already at §bStage 3§4.");
            return;
        }
        VisLocation v = new VisLocation();
        ItemStack[] items = new ItemStack[4];
        if (stage == 1) {
            items[0] = new ItemStack(49, 20);
            items[1] = new ItemStack(266, 10);
            items[2] = new ItemStack(264, 5);
            items[3] = new ItemStack(331, 25);
            if (!(InventoryWorkaround.containsItem(user.getPlayer().getInventory(), false, items) && user.hasDarkVis(500))) {
                user.getPlayer().sendMessage("§4You need,");
                user.getPlayer().sendMessage("§45 §b Diamond§4, 20 §bObsidian§4, 10 §bGold Ingots§4, and 25§b Redstone");
                user.getPlayer().sendMessage("§5500 §8Dark Condensed Vis");
                user.getPlayer().sendMessage("§4To upgrade this §5Extractor§4 to §bStage 2§4.");
                return;
            }
            InventoryWorkaround.removeItem(user.getPlayer().getInventory(), true, items);
            user.subtractDarkVis(500);
            stage = 2;
            v.loadFromString(signBlock());
            ((Sign) v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getState()).setLine(1, "Stage 2");
            ((Sign) v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getState()).update();
            save();
            return;
        }
        items[0] = new ItemStack(49, 50);
        items[1] = new ItemStack(266, 25);
        items[2] = new ItemStack(264, 10);
        items[3] = new ItemStack(331, 50);
        if (!(InventoryWorkaround.containsItem(user.getPlayer().getInventory(), false, items) && user.hasDarkVis(1000))) {
            user.getPlayer().sendMessage("§4You need,");
            user.getPlayer().sendMessage("§410 §b Diamond§4, 50 §bObsidian§4, 25 §bGold Ingots§4, and 50§b Redstone");
            user.getPlayer().sendMessage("§51000 §8Dark Condensed Vis");
            user.getPlayer().sendMessage("§4To upgrade this §5Extractor§4 to §bStage 3§4.");
            return;
        }
        InventoryWorkaround.removeItem(user.getPlayer().getInventory(), true, items);
        user.subtractDarkVis(1000);
        stage = 3;
        v.loadFromString(signBlock());
        ((Sign) v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getState()).setLine(1, "Stage 3");
        ((Sign) v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getState()).update();
        save();
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
        removeFuelBlocks();
        Machines.extractors.remove(id);
        Machines.machines.remove(id);
        deleteConfig();
        return true;
    }

    public void newExtractor(User user, Location location) {
        ItemStack[] items = new ItemStack[4];
        items[0] = new ItemStack(49, 10);
        items[1] = new ItemStack(266, 5);
        items[2] = new ItemStack(264, 1);
        items[3] = new ItemStack(331, 15);
        if (!InventoryWorkaround.containsItem(user.getPlayer().getInventory(), false, items)) {
            user.getPlayer().sendMessage("§4You need,");
            user.getPlayer().sendMessage("§41 §b Diamond§4, 10 §bObsidian§4, 5 §bGold Ingots§4, and 15§b Redstone");
            user.getPlayer().sendMessage("§4To construct an §5Extractor§4.");
            return;
        }
        if (!checkBlocks(location)) {
            user.getPlayer().sendMessage("§4The machine is obstructed.");
            return;
        }
        InventoryWorkaround.removeItem(user.getPlayer().getInventory(), false, items);
        form(location);
        newConfig(user);
        storeProtectedBlocks();
        storeSignBlock();
        storeFuelBlocks();
        Machines.extractors.put(id, this);
        Machine machine = new Machine();
        machine.Machine(this);
        Machines.machines.put(id, machine);
        Machines.ticExtractors.add(this);
        user.getPlayer().sendMessage("§eThe §5Extractor §eforms before your eyes.");
    }

    public void loadExtractor(int fid) {
        id = fid;
        loadConfig();
        storeProtectedBlocks();
        storeSignBlock();
        storeFuelBlocks();
        Machines.extractors.put(id, this);
        Machine machine = new Machine();
        machine.Machine(this);
        Machines.machines.put(id, machine);
        Machines.ticExtractors.add(this);
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

    private void storeFuelBlocks() {
        List<String> list = fuelBlocks();
        for (int ctr = 0; ctr < list.size(); ctr++) {
            Machines.fuelBlocks.put(list.get(ctr), id);
        }
    }

    private void removeSignBlock() {
        Machines.signBlocks.remove(signBlock());
    }

    private void removeFuelBlocks() {
        List<String> list = fuelBlocks();
        for (int ctr = 0; ctr < list.size(); ctr++) {
            Machines.fuelBlocks.remove(list.get(ctr));
        }
    }

    private void form(Location l) {
        l = new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY(), l.getBlockZ());
        origin = l;
        List<String> list = obsidianBlocks();
        VisLocation v = new VisLocation();
        for (int ctr = 0; ctr < list.size(); ctr++) {
            v.loadFromString(list.get(ctr));
            l.getWorld().getBlockAt(v.convertToLocation()).setTypeId(49);
        }

        list = eggBlocks();
        for (int ctr = 0; ctr < list.size(); ctr++) {
            v.loadFromString(list.get(ctr));
            l.getWorld().getBlockAt(v.convertToLocation()).setTypeId(122);
        }
        v.loadFromString(chestBlock());
        l.getWorld().getBlockAt(v.convertToLocation()).setData(((byte) 3), true);
        v.loadFromString(signBlock());
        l.getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(68, ((byte) 3), true);
        Sign sign = (Sign) l.getWorld().getBlockAt(v.convertToLocation()).getState();
        sign.setLine(0, "§5--Extractor--");
        sign.setLine(1, "Stage 1");
        id = getNewID();
        sign.setLine(2, "" + id);
        sign.update();

    }

    private void newConfig(User user) {
        YamlConfiguration config = getConfig();
        config.set("stage", 1);
        VisLocation v = new VisLocation();
        v.loadFromLocation(origin);
        config.set("origin", v.toString());
        config.set("vis", 0);
        config.set("charge", 0);
        config.set("tics", 0);
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
        charge = config.getInt("charge", 0);
        tics = config.getInt("tics", 0);
        state = true;
    }

    private boolean checkBlocks(Location l) {
        List<Location> list = new ArrayList<Location>();

        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY(), l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY(), l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY(), l.getBlockZ() + 1));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY(), l.getBlockZ() + 1));
        list.add(new Location(l.getWorld(), l.getBlockX() - 2, l.getBlockY() - 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY(), l.getBlockZ() + 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY(), l.getBlockZ() + 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY(), l.getBlockZ() + 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY(), l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY(), l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY(), l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY(), l.getBlockZ() - 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY(), l.getBlockZ() - 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY(), l.getBlockZ() - 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY(), l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY(), l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY(), l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() + 1));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() - 2));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() + 2));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY(), l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY(), l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY(), l.getBlockZ() + 1));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY(), l.getBlockZ() - 2));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY(), l.getBlockZ() + 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() - 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() - 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ() + 1));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() - 1, l.getBlockZ() + 1));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() - 1, l.getBlockZ() + 1));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ() + 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() - 1, l.getBlockZ() + 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() - 1, l.getBlockZ() + 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() - 1, l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() - 1, l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ() - 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() - 1, l.getBlockZ() - 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() - 1, l.getBlockZ() - 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() - 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() - 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() + 1));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() - 2));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() + 2));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ() + 1));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ() - 2));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ() + 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() + 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() + 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() + 1, l.getBlockZ() + 1));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() + 1, l.getBlockZ() + 1));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() + 1, l.getBlockZ() + 1));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() + 1, l.getBlockZ() + 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() + 1, l.getBlockZ() + 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() + 1, l.getBlockZ() + 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() + 1, l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() + 1, l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() + 1, l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() + 1, l.getBlockZ() - 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() + 1, l.getBlockZ() - 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() + 1, l.getBlockZ() - 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() + 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() + 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() + 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 1));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 2));
        list.add(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 2));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() + 1, l.getBlockZ()));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() + 1, l.getBlockZ() - 1));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() + 1, l.getBlockZ() + 1));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() + 1, l.getBlockZ() - 2));
        list.add(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() + 1, l.getBlockZ() + 2));
        list.add(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ() + 3));

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

    private String redstoneBlock() {
        Location l = origin;
        VisLocation v = new VisLocation();
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ() + 3));
        return v.toString();
    }

    private List<String> obsidianBlocks() {
        Location l = origin;
        l = new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
        List<String> list = new ArrayList<String>();
        VisLocation v = new VisLocation();
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY(), l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() - 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ() - 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() - 1, l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() - 1, l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() + 1));
        list.add(v.toString());
        return list;
    }

    private List<String> fuelBlocks() {
        Location l = origin;
        List<String> list = new ArrayList<String>();
        VisLocation v = new VisLocation();
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() - 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ() - 1));
        list.add(v.toString());
        return list;
    }

    private List<String> eggBlocks() {
        Location l = origin;
        List<String> list = new ArrayList<String>();
        VisLocation v = new VisLocation();
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY(), l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY(), l.getBlockZ() - 1));
        list.add(v.toString());
        return list;
    }

    private String chestBlock() {
        Location l = origin;
        VisLocation v = new VisLocation();
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() + 1, l.getBlockZ()));
        return v.toString();
    }

    private String signBlock() {
        Location l = origin;
        l = new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY(), l.getBlockZ() + 1);
        VisLocation v = new VisLocation();
        v.loadFromLocation(l);
        return v.toString();
    }

    private List<String> protectedBlocks() {
        Location l = origin;
        List<String> list = new ArrayList<String>();
        VisLocation v = new VisLocation();
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY(), l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY(), l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY(), l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 2, l.getBlockZ() + 3));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY(), l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY(), l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY(), l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY(), l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY(), l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY(), l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY(), l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY(), l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY(), l.getBlockZ() - 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY(), l.getBlockZ() - 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY(), l.getBlockZ() - 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY(), l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY(), l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY(), l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() - 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY(), l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY(), l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY(), l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY(), l.getBlockZ() - 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY(), l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() - 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() - 1, l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() - 1, l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() - 1, l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() - 1, l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() - 1, l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() - 1, l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ() - 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() - 1, l.getBlockZ() - 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() - 1, l.getBlockZ() - 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() - 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() - 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() - 1, l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ() - 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() + 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() + 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() + 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() + 1, l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() + 1, l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() + 1, l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() + 1, l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() + 1, l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() + 1, l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() + 1, l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() + 1, l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() + 1, l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() + 1, l.getBlockZ() - 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() + 1, l.getBlockZ() - 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() + 1, l.getBlockZ() - 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() + 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY() + 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY() + 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() + 1, l.getBlockZ()));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() + 1, l.getBlockZ() - 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() + 1, l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() + 1, l.getBlockZ() - 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() - 1, l.getBlockY() + 1, l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 1, l.getBlockZ() + 3));
        list.add(v.toString());


        return list;
    }
}
