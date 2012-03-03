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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Infuser {

    private int id;
    private int vis = 0;
    private int consumed = 0;
    private int ivis = 0;
    private int charge = 0;
    private int tics = 0;
    private String type = "_VISNULL_";
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
            if (v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getTypeId() == 9) {
                v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).setType(Material.AIR);
                charge = charge + 10000;
            }
        }
    }

    private void rechargeTics() {
        VisLocation v = new VisLocation();
        v.loadFromString(chestBlock());
        Chest c = (Chest) origin.getWorld().getBlockAt(v.convertToLocation()).getState();
        ItemStack[] items = c.getInventory().getContents();
        int tempTics = tics;
        for (int ctr = 0; ctr < items.length && tics == 0; ctr++) {
            if (items[ctr] != null) {
                if (items[ctr].getTypeId() != 0) {
                    if (Items.getAlignment(items[ctr].getTypeId()).equals("none")) {
                        continue;
                    }
                    if (Items.getCharge(items[ctr].getTypeId()) == -1) {
                        continue;
                    }
                    if (Items.getAlignment(items[ctr].getTypeId()).equals(type)) {
                        tics = tics + Items.getCharge(items[ctr].getTypeId());
                        if (items[ctr].getAmount() == 1) {
                            c.getInventory().remove(items[ctr]);
                        } else {
                            items[ctr].setAmount(items[ctr].getAmount() - 1);
                        }
                    }
                }
            }
        }
    }

    public void clearType(User user) {
        if (!user.hasPermission()) {
            user.getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
            return;
        }
        if (Config.ownership) {
            if (!user.getName().toLowerCase().equals(getOwner().toLowerCase()) && !getOwner().equals("_VISNULL_")) {
                user.getPlayer().sendMessage("§4You do not own this §5Infuser§4.");
            }
            return;
        }
        Player p = user.getPlayer();
        if (ivis > 0) {
            p.sendMessage("§4You cannot do this while there is §5Infused Vis§4 in the §5Infuser§4!");
            return;
        }
        if (tics > 0) {
            p.sendMessage("§4You cannot do this while there is §ban item§4 in the §5Infuser§4's mixture still!");
            return;
        }
        int uvis = user.getVis();
        vis = vis + uvis;
        p.sendMessage("§eYou cleared the §5Infuser§e's Type!");
        vis = vis + consumed;
        consumed = 0;
        type = "_VISNULL_";
        save();
    }

    private void chooseType() {
        VisLocation v = new VisLocation();
        v.loadFromString(chestBlock());
        Chest c = (Chest) origin.getWorld().getBlockAt(v.convertToLocation()).getState();
        ItemStack[] items = c.getInventory().getContents();
        for (int ctr = 0; ctr < items.length && tics == 0; ctr++) {
            if (items[ctr] != null) {
                if (items[ctr].getTypeId() != 0) {
                    type = Items.getAlignment(items[ctr].getTypeId());


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
            LivingEntity spawnCreature = v.convertToLocation().getWorld().spawnCreature(v.convertToLocation(), EntityType.ENDERMAN);
            Machines.safeSpawns.add(spawnCreature.getEntityId());
        }
        v.loadFromString(redstoneBlock());
        if (ivis < 100000 && stage == 1 && v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getTypeId() != 0) {
            v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(0, (byte) 0, true);
        }
        if (ivis < 1000000 && stage == 2 && v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getTypeId() != 0) {
            v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(0, (byte) 0, true);
        }
        if (ivis < 10000000 && stage == 3 && v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getTypeId() != 0) {
            v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(0, (byte) 0, true);
        }
        int full = 0;
        if (ivis < 100000 && stage == 1) {
            if (consumed >= 10000) {
                ivis++;
                consumed = -10000;
                full = 1;
            }
        }
        if (ivis < 1000000 && stage == 2) {
            if (consumed >= 10000) {
                ivis++;
                consumed = -10000;
                full = 1;
            }
        }

        if (ivis < 100000 && stage == 3) {
            if (consumed >= 10000) {
                ivis++;
                consumed = -10000;
                full = 1;
            }
        }
        if (full == 1) {
            state = false;
            return;
        }

        if (charge == 0) {
            rechargeCharge();
            if (charge == 0) {
                state = false;
                save();
                return;
            }
        }
        if (type.equals("_VISNULL_")) {
            chooseType();
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
            if (vis >= 100) {
                int takeTics = 100;
                if (tics < 100) {
                    int tempTics;
                    while (tics < 100) {
                        tempTics = tics;
                        rechargeTics();
                        if (tempTics == tics) {
                            takeTics = tics;
                            break;
                        }
                    }
                }
                tics = tics - takeTics;
                charge--;
                vis = vis - takeTics;
                consumed = consumed + takeTics;
            } else {
                state = false;
            }
        }
        if (stage == 2) {
            if (vis >= 200) {
                int takeTics = 200;
                if (tics < 200) {
                    int tempTics;
                    while (tics < 200) {
                        tempTics = tics;
                        rechargeTics();
                        if (tempTics == tics) {
                            takeTics = tics;
                            break;
                        }
                    }
                }
                tics = tics - takeTics;
                if (charge < 25) {
                    int tempCharge;
                    while (charge < 25) {
                        tempCharge = charge;
                        rechargeCharge();
                        if (tempCharge == charge) {
                            state = false;
                            save();
                            return;
                        }
                    }
                }
                charge = charge - 25;
                vis = vis - takeTics;
                consumed = consumed + takeTics;
            } else {
                state = false;
            }
        }
        if (stage == 3) {
            if (vis >= 400) {
                int takeTics = 400;
                if (tics < 400) {
                    int tempTics;
                    while (tics < 400) {
                        tempTics = tics;
                        rechargeTics();
                        if (tempTics == tics) {
                            takeTics = tics;
                            break;
                        }
                    }
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
                vis = vis - takeTics;
                consumed = consumed + takeTics;
            } else {
                state = false;
            }
        }




    }

    public void save() {
        YamlConfiguration config = getConfig();
        config.set("vis", vis);
        config.set("charge", charge);
        config.set("consumed", consumed);
        config.set("tics", tics);
        config.set("stage", stage);
        config.set("ivis", ivis);
        config.set("type", type);
        saveConfig(config);
    }

    private void setID(int i) {
        id = i;
    }

    private File getFile() {
        File dir = new File(VisCraft.getStatic().getDataFolder() + File.separator + "Infusers");
        File file = new File(VisCraft.getStatic().getDataFolder() + File.separator + "Infusers" + File.separator + id + ".inf");
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                VisCraft.logger.severe("[VisCraft] failed to create Infuser " + id + "'s file!");
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
                VisCraft.logger.severe("[VisCraft] failed to create Infuser List file!");
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
            VisCraft.logger.severe("[VisCraft] failed to find Infuser " + id + "'s file!");
        } catch (IOException ex) {
            VisCraft.logger.severe("[VisCraft] failed to load Infuser " + id + "'s file!");
        } catch (InvalidConfigurationException ex) {
            VisCraft.logger.severe("[VisCraft] You fudged up Infuser " + id + "'s file!");
        }
        return config;
    }

    private void deleteConfig() {
        getFile().delete();
    }

    public void turnOn() {
        state = true;
    }

    public void takeVis(User user) {
        if (Config.ownership) {
            if (!user.getName().toLowerCase().equals(getOwner().toLowerCase()) && !getOwner().equals("_VISNULL_")) {
                user.getPlayer().sendMessage("§4You do not own this §5Infuser§4.");
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
        user.getPlayer().sendMessage("§eYou have withdrawn §5" + amount + " Vis§e.");
        save();
    }

    public void extract(User user) {
        if (!user.hasPermission()) {
            user.getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
            return;
        }
        if (Config.ownership) {
            if (!user.getName().toLowerCase().equals(getOwner().toLowerCase()) && !getOwner().equals("_VISNULL_")) {
                user.getPlayer().sendMessage("§4You do not own this §5Infuser§4.");
            }
            return;
        }
        if (!type.equals("_VISNULL_")) {

            int userVis = 0;
            String string = "";
            String color = "";
            if (type.equals("fire")) {
                string = "Fire Infused Vis";
                color = "§4";
                userVis = user.getFireVis();
            }
            if (type.equals("water")) {
                string = "Water Infused Vis";
                color = "§1";
                userVis = user.getWaterVis();
            }
            if (type.equals("earth")) {
                string = "Earth Infused Vis";
                color = "§2";
                userVis = user.getEarthVis();
            }
            if (type.equals("air")) {
                string = "Air Infused Vis";
                color = "§6";
                userVis = user.getAirVis();
            }
            if (userVis > 500000) {
                user.getPlayer().sendMessage("§4You already carry to much " + color + string + "§4.");
                return;
            }
            int amount;
            if ((userVis + ivis) > 500000) {
                if (type.equals("fire")) {
                    user.setFireVis(500000);
                }
                if (type.equals("water")) {
                    user.setWaterVis(500000);
                }
                if (type.equals("earth")) {
                    user.setEarthVis(500000);
                }
                if (type.equals("air")) {
                    user.setAirVis(500000);
                }

                ivis = ivis - (500000 - userVis);
                amount = 500000 - userVis;
            } else {
                if (type.equals("fire")) {
                    user.addFireVis(ivis);
                }
                if (type.equals("water")) {
                    user.addWaterVis(ivis);
                }
                if (type.equals("earth")) {
                    user.addEarthVis(ivis);
                }
                if (type.equals("air")) {
                    user.addAirVis(ivis);
                }
                amount = ivis;
                ivis = 0;
            }
            if (!state) {
                state = true;
            }
            user.getPlayer().sendMessage("§eYou have withdrawn " + color + amount + " " + string + "§e.");
        } else {
            user.getPlayer().sendMessage("§4This §5Infuser§4 has no §5Infused Vis§4.");

        }
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
            VisCraft.logger.severe("[VisCraft] failed to save Infuser " + id + "'s file!");
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
        if (b1.getTypeId() == 42 && b2.getTypeId() == 42 && b3.getTypeId() == 54) {
            return true;
        }
        return false;
    }

    public int getVis() {
        save();
        return vis;
    }

    public int getIvis() {
        save();
        return ivis;
    }

    public String getType() {
        String string = "";
        if (type.equals("fire")) {
            string = "Fire Infused Vis";
        }
        if (type.equals("water")) {
            string = "Water Infused Vis";
        }
        if (type.equals("earth")) {
            string = "Earth Infused Vis";
        }
        if (type.equals("air")) {
            string = "Air Infused Vis";
        }
        return string;

    }

    public String getColor() {
        String color = "";
        if (type.equals("fire")) {
            color = "§4";
        }
        if (type.equals("water")) {
            color = "§1";
        }
        if (type.equals("earth")) {
            color = "§2";
        }
        if (type.equals("air")) {
            color = "§6";
        }
        return color;

    }

    public void announce(User user) {
        user.getPlayer().sendMessage("§eThis §5Infuser§e has,");
        user.getPlayer().sendMessage("§5" + getVis() + " Vis");
        if (!type.equals("_VISNULL_")) {
            user.getPlayer().sendMessage(getColor() + ivis + " " + getType());
        } else {
            user.getPlayer().sendMessage("§50 Infused Vis§e");
        }
        user.getPlayer().sendMessage("§eAnd §2Powered§e: §b" + state + "§e.");
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

    public void setIvis(int amount) {
        ivis = amount;
        save();
    }

    public void setType(String string) {
        type = string;
        save();
    }

    public void addIvis(int amount) {
        setIvis(getIvis() + amount);
    }

    public void subtractIvis(int amount) {
        setIvis(getIvis() - amount);
    }

    public void upgrade(User user) {
        if (stage == 3) {
            user.getPlayer().sendMessage("§4This §5Infuser§4 is already at §bStage 3§4.");
            return;
        }
        VisLocation v = new VisLocation();
        ItemStack[] items = new ItemStack[4];
        if (stage == 1) {
            items[0] = new ItemStack(42, 20);
            items[1] = new ItemStack(89, 64);
            items[2] = new ItemStack(264, 15);
            items[3] = new ItemStack(331, 45);
            if (!(InventoryWorkaround.containsItem(user.getPlayer().getInventory(), false, items) && user.hasPureVis(1000))) {
                user.getPlayer().sendMessage("§4You need,");
                user.getPlayer().sendMessage("§415 §bDiamond§4, 20 §bIron Blocks§4, 64 §bGlowStone§4, and 45§b Redstone");
                user.getPlayer().sendMessage("§51000 §fPure Condensed Vis");
                user.getPlayer().sendMessage("§4To upgrade this §5Infuser§4 to §bStage 2§4.");
                return;
            }
            InventoryWorkaround.removeItem(user.getPlayer().getInventory(), true, items);
            user.subtractPureVis(1000);
            stage = 2;
            v.loadFromString(signBlock());
            ((Sign) v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getState()).setLine(1, "Stage 2");
            ((Sign) v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getState()).update();
            save();
            return;
        }
        items[0] = new ItemStack(42, 32);
        items[1] = new ItemStack(89, 128);
        items[2] = new ItemStack(264, 20);
        items[3] = new ItemStack(331, 64);
        if (!(InventoryWorkaround.containsItem(user.getPlayer().getInventory(), false, items) && user.hasPureVis(10000))) {
            user.getPlayer().sendMessage("§4You need,");
            user.getPlayer().sendMessage("§420 §bDiamond§4, 32 §bIron Blocks§4, 128 §bGlowStone§4, and 64§b Redstone");
            user.getPlayer().sendMessage("§510000 §fPure Condensed Vis");
            user.getPlayer().sendMessage("§4To upgrade this §5Infuser§4 to §bStage 3§4.");
            return;
        }
        InventoryWorkaround.removeItem(user.getPlayer().getInventory(), true, items);
        user.subtractPureVis(10000);
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
        Machines.infusers.remove(id);
        Machines.machines.remove(id);
        deleteConfig();
        return true;
    }

    public void inputVis(User user) {
        if (!user.hasPermission()) {
            user.getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
            return;
        }
        if (Config.ownership) {
            if (!user.getName().toLowerCase().equals(getOwner().toLowerCase()) && !getOwner().equals("_VISNULL_")) {
                user.getPlayer().sendMessage("§4You do not own this §5Infuser§4.");
            }
            return;
        }
        Player p = user.getPlayer();
        int uvis = user.getVis();
        vis = vis + uvis;
        user.setVis(0);
        p.sendMessage("§eYou supplied the §5Infuser§e with §5" + uvis + " Vis§4!");
    }

    public void newInfuser(User user, Location location) {
        ItemStack[] items = new ItemStack[4];
        items[0] = new ItemStack(42, 10);
        items[1] = new ItemStack(89, 32);
        items[2] = new ItemStack(264, 5);
        items[3] = new ItemStack(331, 25);
        if (!(InventoryWorkaround.containsItem(user.getPlayer().getInventory(), false, items) && user.hasVis(10000))) {
            user.getPlayer().sendMessage("§4You need,");
            user.getPlayer().sendMessage("§45 §bDiamond§4, 10 §bIron Blocks§4, 32 §bGlowStone§4, and 25§b Redstone");
            user.getPlayer().sendMessage("§510,000 Vis§4");
            user.getPlayer().sendMessage("§4To construct an §5Infuser§4.");
            return;
        }
        if (!checkBlocks(location)) {
            user.getPlayer().sendMessage("§4The machine is obstructed.");
            return;
        }
        InventoryWorkaround.removeItem(user.getPlayer().getInventory(), false, items);
        user.subtractVis(10000);
        form(location);
        newConfig(user);
        storeProtectedBlocks();
        storeSignBlock();
        storeFuelBlocks();
        Machines.infusers.put(id, this);
        Machine machine = new Machine();
        machine.Machine(this);
        Machines.machines.put(id, machine);
        Machines.ticInfusers.add(this);
        user.getPlayer().sendMessage("§eThe §5Infuser §eforms before your eyes.");
    }

    public void loadInfuser(int fid) {
        id = fid;
        loadConfig();
        storeProtectedBlocks();
        storeSignBlock();
        storeFuelBlocks();
        Machines.infusers.put(id, this);
        Machine machine = new Machine();
        machine.Machine(this);
        Machines.machines.put(id, machine);
        Machines.ticInfusers.add(this);
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
        List<String> list = ironBlocks();
        VisLocation v = new VisLocation();
        for (int ctr = 0; ctr < list.size(); ctr++) {
            v.loadFromString(list.get(ctr));
            l.getWorld().getBlockAt(v.convertToLocation()).setTypeId(42);
        }

        list = lightBlocks();
        for (int ctr = 0; ctr < list.size(); ctr++) {
            v.loadFromString(list.get(ctr));
            l.getWorld().getBlockAt(v.convertToLocation()).setTypeId(89);
        }
        v.loadFromString(chestBlock());
        l.getWorld().getBlockAt(v.convertToLocation()).setData(((byte) 3), true);
        v.loadFromString(signBlock());
        l.getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(68, ((byte) 3), true);
        Sign sign = (Sign) l.getWorld().getBlockAt(v.convertToLocation()).getState();
        sign.setLine(0, "§5--Infuser--");
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
        config.set("ivis", 0);
        config.set("type", "_VISNULL_");
        config.set("charge", 0);
        config.set("consumed", 0);
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
        ivis = config.getInt("ivis", 0);
        type = config.getString("type", "_VISNULL_");
        charge = config.getInt("charge", 0);
        tics = config.getInt("tics", 0);
        consumed = config.getInt("consumed", 0);
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

    private List<String> ironBlocks() {
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

    private List<String> lightBlocks() {
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
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 2, l.getBlockY(), l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 3, l.getBlockY(), l.getBlockZ() + 1));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 2, l.getBlockZ() + 3));
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
