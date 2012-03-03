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

public class Condenser {

    private int id;
    private int ivis1 = 0;
    private int ivis2 = 0;
    private int consumed1 = 0;
    private int consumed2 = 0;
    private int cvis = 0;
    private int charge = 0;
    private int tics = 0;
    private String type = "_VISNULL_";
    private String itype1 = "_VISNULL_";
    private String itype2 = "_VISNULL_";
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
            if (v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getTypeId() == 22) {
                v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).setType(Material.AIR);
                charge = charge + 20000;
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
                    if (Items.getQuality(items[ctr].getTypeId()) == -1) {
                        continue;
                    }
                    tics = tics + Items.getQuality(items[ctr].getTypeId());
                    if (items[ctr].getAmount() == 1) {
                        c.getInventory().remove(items[ctr]);
                    } else {
                        items[ctr].setAmount(items[ctr].getAmount() - 1);
                    }
                }
            }
        }
    }

    private void clearType() {
        if (ivis1 == 0 || ivis2 == 0) {
            ivis2 = ivis2 + consumed2;
            ivis1 = ivis1 + consumed1;
            consumed2 = 0;
            consumed1 = 0;
        }
        if (ivis1 == 0) {
            itype1 = "_VISNULL_";
        }
        if (ivis2 == 0) {
            itype2 = "_VISNULL_";
        }
        if (cvis > 0) {
            return;
        }
        type = "_VISNULL_";
        save();
    }

    private void chooseType() {
        if (itype1.equals("water") && itype2.equals("air")) {
            type = "white";
            return;
        }
        if (itype1.equals("fire") && itype2.equals("earth")) {
            type = "black";
            return;
        }
        if (itype1.equals("fire") && itype2.equals("air")) {
            type = "ethereal";
            return;
        }
        if (itype1.equals("water") && itype2.equals("earth")) {
            type = "brilliant";
            return;
        }
        if (itype1.equals("earth") && itype2.equals("air")) {
            type = "unstable";
            return;
        }
        if (itype1.equals("fire") && itype2.equals("water")) {
            type = "unstable";
            return;
        }
        if (itype1.equals("air") && itype2.equals("water")) {
            type = "white";
            return;
        }
        if (itype1.equals("earth") && itype2.equals("fire")) {
            type = "black";
            return;
        }
        if (itype1.equals("air") && itype2.equals("fire")) {
            type = "ethereal";
            return;
        }
        if (itype1.equals("earth") && itype2.equals("water")) {
            type = "brilliant";
            return;
        }
        if (itype1.equals("air") && itype2.equals("earth")) {
            type = "unstable";
            return;
        }
        if (itype1.equals("water") && itype2.equals("fire")) {
            type = "unstable";
            return;
        }
    }

    public void tic() {
        lived++;
        if (lived == 36000) {
            save();
            lived = 0;
        }
        clearType();
        VisLocation v = new VisLocation();
        Random random = new Random(this.hashCode());
        if (Config.spawn && random.nextInt(500) == random.nextInt(500)) {
            v.loadFromString(redstoneBlock());
            LivingEntity spawnCreature = v.convertToLocation().getWorld().spawnCreature(v.convertToLocation(), EntityType.PIG_ZOMBIE);
            Machines.safeSpawns.add(spawnCreature.getEntityId());
        }
        v.loadFromString(redstoneBlock());
        if (cvis < 100000 && stage == 1 && v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getTypeId() != 0) {
            v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(0, (byte) 0, true);
        }
        if (cvis < 1000000 && stage == 2 && v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getTypeId() != 0) {
            v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(0, (byte) 0, true);
        }
        if (cvis < 10000000 && stage == 3 && v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getTypeId() != 0) {
            v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(0, (byte) 0, true);
        }
        int full = 0;
        if (cvis < 100000 && stage == 1) {
            if (consumed1 >= 10000 && consumed2 >= 10000) {
                cvis++;
                consumed1 = consumed1 - 10000;
                consumed2 = consumed2 - 10000;
                full = 1;
            }
        }
        if (cvis < 1000000 && stage == 2) {
            if (consumed1 >= 10000 && consumed2 >= 10000) {
                cvis++;
                consumed1 = consumed1 - 10000;
                consumed2 = consumed2 - 10000;
                full = 1;
            }
        }
        if (cvis < 100000 && stage == 3) {
            if (consumed1 >= 10000 && consumed2 >= 10000) {
                cvis++;
                consumed1 = consumed1 - 10000;
                consumed2 = consumed2 - 10000;
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
            state = true;
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
            state = true;
        }

        if (stage == 1) {
            if (ivis1 >= 100 && ivis2 >= 100) {
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
                ivis1 = ivis1 - takeTics;
                ivis2 = ivis2 - takeTics;
                consumed1 = consumed1 + takeTics;
                consumed2 = consumed2 + takeTics;
            }
        }
        if (stage == 2) {
            if (ivis1 >= 250 && ivis2 >= 250) {
                int takeTics = 2200;
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
                ivis1 = ivis1 - takeTics;
                ivis2 = ivis2 - takeTics;
                consumed1 = consumed1 + takeTics;
                consumed2 = consumed2 + takeTics;

            }
        }
        if (stage == 3) {
            if (ivis1 >= 500 && ivis2 >= 500) {
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
                ivis1 = ivis1 - takeTics;
                ivis2 = ivis2 - takeTics;
                consumed1 = consumed1 + takeTics;
                consumed2 = consumed2 + takeTics;

            }
        }




    }

    public void save() {
        YamlConfiguration config = getConfig();
        config.set("ivis1", ivis1);
        config.set("ivis2", ivis2);
        config.set("charge", charge);
        config.set("consumed1", consumed1);
        config.set("consumed2", consumed2);
        config.set("tics", tics);
        config.set("stage", stage);
        config.set("cvis", cvis);
        config.set("type", type);
        config.set("itype1", itype1);
        config.set("itype2", itype2);
        saveConfig(config);
    }

    private void setID(int i) {
        id = i;
    }

    private File getFile() {
        File dir = new File(VisCraft.getStatic().getDataFolder() + File.separator + "Condensers");
        File file = new File(VisCraft.getStatic().getDataFolder() + File.separator + "Condensers" + File.separator + id + ".con");
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                VisCraft.logger.severe("[VisCraft] failed to create Condenser " + id + "'s file!");
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
                VisCraft.logger.severe("[VisCraft] failed to create Condenser List file!");
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
            VisCraft.logger.severe("[VisCraft] failed to find Condenser " + id + "'s file!");
        } catch (IOException ex) {
            VisCraft.logger.severe("[VisCraft] failed to load Condenser " + id + "'s file!");
        } catch (InvalidConfigurationException ex) {
            VisCraft.logger.severe("[VisCraft] You fudged up Condenser " + id + "'s file!");
        }
        return config;
    }

    private void deleteConfig() {
        getFile().delete();
    }

    public void turnOn() {
        state = true;
    }

    public void takeIVis(User user) {
        if (!user.hasPermission()) {
            user.getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
            return;
        }
        if (Config.ownership) {
            if (!user.getName().toLowerCase().equals(getOwner().toLowerCase()) && !getOwner().equals("_VISNULL_")) {
                user.getPlayer().sendMessage("§4You do not own this §5Condenser§4.");
            }
            return;
        }
        int userVis = 0;
        String string = "";
        String color = "";
        int amount;
        if (!itype1.equals("_VISNULL_")) {

            userVis = 0;
            string = "";
            color = "";
            if (itype1.equals("fire")) {
                string = "Fire Infused Vis";
                color = "§4";
                userVis = user.getFireVis();
            }
            if (itype1.equals("water")) {
                string = "Water Infused Vis";
                color = "§1";
                userVis = user.getWaterVis();
            }
            if (itype1.equals("earth")) {
                string = "Earth Infused Vis";
                color = "§2";
                userVis = user.getEarthVis();
            }
            if (itype1.equals("air")) {
                string = "Air Infused Vi";
                color = "§6";
                userVis = user.getAirVis();
            }
            if (userVis > 500000) {
                user.getPlayer().sendMessage("§4You already carry to much " + color + string + "§4.");
                return;
            }
            if ((userVis + ivis1) > 500000) {
                if (itype1.equals("fire")) {
                    user.setFireVis(500000);
                }
                if (itype1.equals("water")) {
                    user.setWaterVis(500000);
                }
                if (itype1.equals("earth")) {
                    user.setEarthVis(500000);
                }
                if (itype1.equals("air")) {
                    user.setAirVis(500000);
                }

                ivis1 = ivis1 - (500000 - userVis);
                amount = 500000 - userVis;
            } else {
                if (itype1.equals("fire")) {
                    user.addFireVis(ivis1);
                }
                if (itype1.equals("water")) {
                    user.addWaterVis(ivis1);
                }
                if (itype1.equals("earth")) {
                    user.addEarthVis(ivis1);
                }
                if (itype1.equals("air")) {
                    user.addAirVis(ivis1);
                }
                amount = ivis1;
                ivis1 = 0;
            }
            user.getPlayer().sendMessage("§eYou have withdrawn §5" + amount + " " + color + string + "§e.");
        } else {
            user.getPlayer().sendMessage("§4This machine has §50 Infused Vis§4 in slot 1.");
        }


        if (!itype2.equals("_VISNULL_")) {

            userVis = 0;
            string = "";
            color = "";
            if (itype2.equals("fire")) {
                string = "Fire Infused Vis";
                color = "§4";
                userVis = user.getFireVis();
            }
            if (itype2.equals("water")) {
                string = "Water Infused Vis";
                color = "§1";
                userVis = user.getWaterVis();
            }
            if (itype2.equals("earth")) {
                string = "Earth Infused Vis";
                color = "§2";
                userVis = user.getEarthVis();
            }
            if (itype2.equals("air")) {
                string = "Air Infused Vi";
                color = "§6";
                userVis = user.getAirVis();
            }
            if (userVis > 500000) {
                user.getPlayer().sendMessage("§4You already carry to much " + color + string + "§4.");
                return;
            }
            amount = 0;
            if ((userVis + ivis2) > 500000) {
                if (itype2.equals("fire")) {
                    user.setFireVis(500000);
                }
                if (itype2.equals("water")) {
                    user.setWaterVis(500000);
                }
                if (itype2.equals("earth")) {
                    user.setEarthVis(500000);
                }
                if (itype2.equals("air")) {
                    user.setAirVis(500000);
                }

                ivis2 = ivis2 - (500000 - userVis);
                amount = 500000 - userVis;
            } else {
                if (itype2.equals("fire")) {
                    user.addFireVis(ivis2);
                }
                if (itype2.equals("water")) {
                    user.addWaterVis(ivis2);
                }
                if (itype2.equals("earth")) {
                    user.addEarthVis(ivis2);
                }
                if (itype2.equals("air")) {
                    user.addAirVis(ivis2);
                }
                amount = ivis2;
                ivis2 = 0;
            }
            user.getPlayer().sendMessage("§eYou have withdrawn §5" + amount + " " + color + string + "§e.");
        } else {
            user.getPlayer().sendMessage("§4This machine has §50 Infused Vis§4 in slot 2");
        }



        if (!state) {
            state = true;
        }

        save();

    }

    public void extract(User user) {
        if (!user.hasPermission()) {
            user.getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
            return;
        }
        if (Config.ownership) {
            if (!user.getName().toLowerCase().equals(getOwner().toLowerCase()) && !getOwner().equals("_VISNULL_")) {
                user.getPlayer().sendMessage("§4You do not own this §5Condenser§4.");
            }
            return;
        }
        if (!type.equals("_VISNULL_")) {

            int userVis = 0;
            String string = "";
            String color = "";
            if (type.equals("dark")) {
                string = "Dark Condensed Vis";
                color = "§8";
                userVis = user.getDarkVis();
            }
            if (type.equals("pure")) {
                string = "Pure Condensed Vis";
                color = "§f";
                userVis = user.getPureVis();
            }
            if (type.equals("brilliant")) {
                string = "Brilliant Condensed Vis";
                color = "§a";
                userVis = user.getBrilliantVis();
            }
            if (type.equals("ethereal")) {
                string = "Etheral Condensed Vis";
                color = "§b";
                userVis = user.getEtherealVis();
            }
            if (type.equals("unstable")) {
                string = "Unstable Condensed Vis";
                color = "§c";
                userVis = user.getUnstableVis();
            }
            if (userVis > 500000) {
                user.getPlayer().sendMessage("§4You already carry to much " + color + string + "§4.");
                return;
            }
            int amount;
            if ((userVis + cvis) > 500000) {
                if (type.equals("dark")) {
                    user.setDarkVis(500000);
                }
                if (type.equals("pure")) {
                    user.setPureVis(500000);
                }
                if (type.equals("brilliant")) {
                    user.setBrilliantVis(500000);
                }
                if (type.equals("ethereal")) {
                    user.setEtherealVis(500000);
                }
                if (type.equals("unstable")) {
                    user.setUnstableVis(500000);
                }

                cvis = cvis - (500000 - userVis);
                amount = 500000 - userVis;
            } else {
                if (type.equals("dark")) {
                    user.addDarkVis(cvis);
                }
                if (type.equals("pure")) {
                    user.addPureVis(cvis);
                }
                if (type.equals("brilliant")) {
                    user.addBrilliantVis(cvis);
                }
                if (type.equals("ethereal")) {
                    user.addEtherealVis(cvis);
                }
                if (type.equals("unstable")) {
                    user.addUnstableVis(cvis);
                }
                amount = cvis;
                cvis = 0;
            }
            if (!state) {
                state = true;
            }
            user.getPlayer().sendMessage("§eYou have withdrawn " + color + amount + " " + string + "§e.");
        } else {

            user.getPlayer().sendMessage("§4This §5Condenser§4 has no §5Condensed Vis§4.");


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
            VisCraft.logger.severe("[VisCraft] failed to save Condenser " + id + "'s file!");
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
        if (b1.getTypeId() == 112 && b2.getTypeId() == 112 && b3.getTypeId() == 54) {
            return true;
        }
        return false;
    }

    public int getCVis() {
        save();
        return cvis;
    }

    public int getIvis1() {
        save();
        return ivis1;
    }

    public int getIvis2() {
        save();
        return ivis2;
    }

    public String getType() {
        String string = "";
        if (type.equals("dark")) {
            string = "Dark Condensed Vis";
        }
        if (type.equals("pure")) {
            string = "Pure Condensed Vis";
        }
        if (type.equals("brilliant")) {
            string = "Brilliant Condensed Vis";
        }
        if (type.equals("ethereal")) {
            string = "Ethereal Condensed Vis";
        }
        if (type.equals("unstable")) {
            string = "Unstable Condensed Vis";
        }
        return string;

    }

    public String getColor() {
        String color = "";
        if (type.equals("dark")) {
            color = "§8";
        }
        if (type.equals("pure")) {
            color = "§f";
        }
        if (type.equals("brilliant")) {
            color = "§a";
        }
        if (type.equals("ethereal")) {
            color = "§b";
        }
        if (type.equals("unstable")) {
            color = "§c";
        }
        return color;

    }

    public String getIType1() {
        String string = "";
        if (itype1.equals("fire")) {
            string = "Fire Infused Vis";
        }
        if (itype1.equals("water")) {
            string = "Water Infused Vis";
        }
        if (itype1.equals("earth")) {
            string = "Earth Infused Vis";
        }
        if (itype1.equals("air")) {
            string = "Air Infused Vis";
        }
        return string;

    }

    public String getIColor1() {
        String color = "";
        if (itype1.equals("fire")) {
            color = "§4";
        }
        if (itype1.equals("water")) {
            color = "§1";
        }
        if (itype1.equals("earth")) {
            color = "§2";
        }
        if (itype1.equals("air")) {
            color = "§6";
        }
        return color;

    }

    public String getIType2() {
        String string = "";
        if (itype2.equals("fire")) {
            string = "Fire Infused Vis";
        }
        if (itype2.equals("water")) {
            string = "Water Infused Vis";
        }
        if (itype2.equals("earth")) {
            string = "Earth Infused Vis";
        }
        if (itype2.equals("air")) {
            string = "Air Infused Vis";
        }
        return string;

    }

    public String getIColor2() {
        String color = "";
        if (itype2.equals("fire")) {
            color = "§4";
        }
        if (itype2.equals("water")) {
            color = "§1";
        }
        if (itype2.equals("earth")) {
            color = "§2";
        }
        if (itype2.equals("air")) {
            color = "§6";
        }
        return color;

    }

    public void announce(User user) {
        user.getPlayer().sendMessage("§eThis §5Condenser§e has,");
        if (!itype1.equals("_VISNULL_")) {
            user.getPlayer().sendMessage(getIColor1() + ivis1 + " " + getIType1() + " §ein slot 1");
        } else {
            user.getPlayer().sendMessage("§50 Infused Vis§e in slot 1");
        }
        if (!itype2.equals("_VISNULL_")) {
            user.getPlayer().sendMessage(getIColor2() + ivis2 + " " + getIType2() + " §ein slot 2");
        } else {
            user.getPlayer().sendMessage("§50 Infused Vis§e in slot 2");
        }
        if (!type.equals("_VISNULL_")) {
            user.getPlayer().sendMessage(getColor() + cvis + " " + getType() + " §e");
        } else {
            user.getPlayer().sendMessage("§50 Condensed Vis§e");
        }
        user.getPlayer().sendMessage("§eAnd §2Powered§e: §b" + state + "§e.");
    }

    public void setCVis(int amount) {
        cvis = amount;
        save();
    }

    public void addCVis(int amount) {
        setCVis(getCVis() + amount);
    }

    public void subtractVis(int amount) {
        setCVis(getCVis() - amount);
    }

    public void setIvis1(int amount) {
        ivis1 = amount;
        save();
    }

    public void setIType1(String string) {
        itype1 = string;
        save();
    }

    public void addIvis1(int amount) {
        setIvis1(getIvis1() + amount);
    }

    public void subtractIvis1(int amount) {
        setIvis1(getIvis1() - amount);
    }

    public void setIvis2(int amount) {
        ivis2 = amount;
        save();
    }

    public void setIType2(String string) {
        itype2 = string;
        save();
    }

    public void setType(String string) {
        type = string;
        save();
    }

    public void addIvis2(int amount) {
        setIvis2(getIvis2() + amount);
    }

    public void subtractIvis2(int amount) {
        setIvis2(getIvis2() - amount);
    }

    public void upgrade(User user) {
        if (stage == 3) {
            user.getPlayer().sendMessage("§4This §5Condenser§4 is already at §bStage 3§4.");
            return;
        }
        VisLocation v = new VisLocation();
        ItemStack[] items = new ItemStack[4];
        if (stage == 1) {
            items[0] = new ItemStack(112, 50);
            items[1] = new ItemStack(121, 32);
            items[2] = new ItemStack(264, 20);
            items[3] = new ItemStack(331, 96);
            if (!(InventoryWorkaround.containsItem(user.getPlayer().getInventory(), false, items) && user.hasUnstableVis(1000))) {
                user.getPlayer().sendMessage("§4You need,");
                user.getPlayer().sendMessage("§420 §bDiamond§4, 50 §bNether Brick§4, 32 §bEndStone§4, and 96§b Redstone");
                user.getPlayer().sendMessage("§51000 §cUnstable Condensed Vis");
                user.getPlayer().sendMessage("§4To upgrade this §5Condenser§4 to §bStage 2§4.");
                return;
            }
            InventoryWorkaround.removeItem(user.getPlayer().getInventory(), true, items);
            user.subtractUnstableVis(1000);
            stage = 2;
            v.loadFromString(signBlock());
            ((Sign) v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getState()).setLine(1, "Stage 2");
            ((Sign) v.convertToLocation().getWorld().getBlockAt(v.convertToLocation()).getState()).update();
            save();
            return;
        }
        items[0] = new ItemStack(112, 64);
        items[1] = new ItemStack(121, 64);
        items[2] = new ItemStack(264, 25);
        items[3] = new ItemStack(331, 128);
        if (!(InventoryWorkaround.containsItem(user.getPlayer().getInventory(), false, items) && user.hasUnstableVis(10000))) {
            user.getPlayer().sendMessage("§4You need,");
            user.getPlayer().sendMessage("§425 §bDiamond§4, 64 §bNether Brick§4, 64 §bEndStone§4, and 128§b Redstone");
            user.getPlayer().sendMessage("§510000 §cUnstable Condensed Vis");
            user.getPlayer().sendMessage("§4To upgrade this §5Condenser§4 to §bStage 3§4.");
            return;
        }
        InventoryWorkaround.removeItem(user.getPlayer().getInventory(), true, items);
        user.subtractUnstableVis(10000);
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
        Machines.condensers.remove(id);
        Machines.machines.remove(id);
        deleteConfig();
        return true;
    }

    public void inputIVis(User user) {
        if (!user.hasPermission()) {
            user.getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
            return;
        }
        if (Config.ownership) {
            if (!user.getName().toLowerCase().equals(getOwner().toLowerCase()) && !getOwner().equals("_VISNULL_")) {
                user.getPlayer().sendMessage("§4You do not own this §5Condenser§4.");
            }
            return;
        }
        if (user.getPlayer().getItemInHand().getTypeId() == 0) {
            return;
        }
        int userVis;
        int check = 0;
        if (!itype1.equals("_VISNULL_") && user.getPlayer().getItemInHand().getTypeId() == 281) {
            userVis = 0;
            if (itype1.equals("air")) {
                userVis = user.getAirVis();
            }
            if (itype1.equals("fire")) {
                userVis = user.getFireVis();
            }
            if (itype1.equals("water")) {
                userVis = user.getWaterVis();
            }
            if (itype1.equals("earth")) {
                userVis = user.getEarthVis();
            }
            if (userVis < 1) {
                user.getPlayer().sendMessage("§4You do not have enough " + getIColor1() + getIType1() + "§4 to fill slot 1");
            } else {
                ivis1 = ivis1 + userVis;
                if (itype1.equals("air")) {
                    user.setAirVis(0);
                }
                if (itype1.equals("fire")) {
                    user.setFireVis(0);
                }
                if (itype1.equals("water")) {
                    user.setWaterVis(0);
                }
                if (itype1.equals("earth")) {
                    user.setEarthVis(0);
                }
                user.getPlayer().sendMessage("§eYou supplied the §5Condenser's§e first slot with " + getIColor1() + userVis + " " + getIType1() + "§e");
            }
            check = 1;
        }
        if (!itype2.equals("_VISNULL_") && user.getPlayer().getItemInHand().getTypeId() == 281) {
            userVis = 0;
            if (itype2.equals("air")) {
                userVis = user.getAirVis();
            }
            if (itype2.equals("fire")) {
                userVis = user.getFireVis();
            }
            if (itype2.equals("water")) {
                userVis = user.getWaterVis();
            }
            if (itype2.equals("earth")) {
                userVis = user.getEarthVis();
            }
            if (userVis < 1) {
                user.getPlayer().sendMessage("§4You do not have enough " + getIColor2() + getIType2() + "§4 to fill slot 2");
            } else {
                ivis2 = ivis2 + userVis;
                if (itype2.equals("air")) {
                    user.setAirVis(0);
                }
                if (itype2.equals("fire")) {
                    user.setFireVis(0);
                }
                if (itype2.equals("water")) {
                    user.setWaterVis(0);
                }
                if (itype2.equals("earth")) {
                    user.setEarthVis(0);
                }
                user.getPlayer().sendMessage("§eYou supplied the §5Condenser's§e second slot with " + getIColor2() + userVis + " " + getIType2() + "§e");

            }
            check = 1;
        }
        if (check == 1) {
            return;
        }
        int iid = user.getPlayer().getItemInHand().getTypeId();
        if (itype1.equals("_VISNULL_")) {
            if (iid == 268 && !itype2.equals("fire")) {
                if (user.hasFireVis(1)) {
                    user.getPlayer().sendMessage("§eYou supplied the §5Condenser's§e first slot with §4" + user.getFireVis() + " Fire Vis§e");
                    ivis1 = user.getFireVis();
                    user.setFireVis(0);
                    itype1 = "fire";
                    return;
                } else {
                    user.getPlayer().sendMessage("§4You do not have any §4Fire Vis§e");
                    return;
                }
            }
            if (iid == 269 && !itype2.equals("water")) {
                if (user.hasWaterVis(1)) {
                    user.getPlayer().sendMessage("§eYou supplied the §5Condenser's§e first slot with §1" + user.getWaterVis() + " Water Vis§e");
                    ivis1 = user.getWaterVis();
                    user.setWaterVis(0);
                    itype1 = "water";
                    return;
                } else {
                    user.getPlayer().sendMessage("§4You do not have any §1Water Vis§e");
                    return;
                }
            }
            if (iid == 270 && !itype2.equals("earth")) {
                if (user.hasEarthVis(1)) {
                    user.getPlayer().sendMessage("§eYou supplied the §5Condenser's§e first slot with §2" + user.getEarthVis() + " Earth Vis§e");
                    ivis1 = user.getEarthVis();
                    user.setEarthVis(0);
                    itype1 = "earth";
                    return;
                } else {
                    user.getPlayer().sendMessage("§4You do not have any §2Earth Vis§e");
                    return;
                }
            }
            if (iid == 271 && !itype2.equals("air")) {
                if (user.hasAirVis(1)) {
                    user.getPlayer().sendMessage("§eYou supplied the §5Condenser's§e first slot with §6" + user.getWaterVis() + " Air Vis§e");
                    ivis1 = user.getAirVis();
                    user.setAirVis(0);
                    itype1 = "air";
                    return;
                } else {
                    user.getPlayer().sendMessage("§4You do not have any §6Air Vis§e");
                    return;
                }
            }
        }



        if (itype2.equals("_VISNULL_")) {
            if (iid == 268 && !itype1.equals("fire")) {
                if (user.hasFireVis(1)) {
                    user.getPlayer().sendMessage("§eYou supplied the §5Condenser's§e second slot with §4" + user.getFireVis() + " Fire Vis§e");
                    ivis2 = user.getFireVis();
                    user.setFireVis(0);
                    itype2 = "fire";
                    return;
                } else {
                    user.getPlayer().sendMessage("§4You do not have any §4Fire Vis§e");
                    return;
                }
            }
            if (iid == 269 && !itype1.equals("water")) {
                if (user.hasWaterVis(1)) {
                    user.getPlayer().sendMessage("§eYou supplied the §5Condenser's§e second slot with §1" + user.getWaterVis() + " Water Vis§e");
                    ivis2 = user.getWaterVis();
                    user.setWaterVis(0);
                    itype2 = "water";
                    return;
                } else {
                    user.getPlayer().sendMessage("§4You do not have any §1Water Vis§e");
                    return;
                }
            }
            if (iid == 270 && !itype1.equals("earth")) {
                if (user.hasEarthVis(1)) {
                    user.getPlayer().sendMessage("§eYou supplied the §5Condenser's§e second slot with §2" + user.getEarthVis() + " Earth Vis§e");
                    ivis2 = user.getEarthVis();
                    user.setEarthVis(0);
                    itype2 = "earth";
                    return;
                } else {
                    user.getPlayer().sendMessage("§4You do not have any §2Earth Vis§e");
                    return;
                }
            }
            if (iid == 271 && !itype1.equals("air")) {
                if (user.hasAirVis(1)) {
                    user.getPlayer().sendMessage("§eYou supplied the §5Condenser's§e second slot with §6" + user.getWaterVis() + " Air Vis§e");
                    ivis2 = user.getAirVis();
                    user.setAirVis(0);
                    itype2 = "air";
                    return;
                } else {
                    user.getPlayer().sendMessage("§4You do not have any §6Air Vis§e");
                    return;
                }
            }
        }
    }

    public void newCondenser(User user, Location location) {
        ItemStack[] items = new ItemStack[4];
        items[0] = new ItemStack(112, 25);
        items[1] = new ItemStack(121, 16);
        items[2] = new ItemStack(264, 10);
        items[3] = new ItemStack(331, 64);
        if (!(InventoryWorkaround.containsItem(user.getPlayer().getInventory(), false, items) && user.hasFireVis(1000))) {
            user.getPlayer().sendMessage("§4You need,");
            user.getPlayer().sendMessage("§410 §bDiamond§4, 25 §bNether Brick§4, 16 §bEndStone§4, and 64§b Redstone");
            user.getPlayer().sendMessage("§51,000 Fire Infused Vis§4");
            user.getPlayer().sendMessage("§4To construct a §5Condenser§4.");
            return;
        }
        if (!checkBlocks(location)) {
            user.getPlayer().sendMessage("§4The machine is obstructed.");
            return;
        }
        InventoryWorkaround.removeItem(user.getPlayer().getInventory(), false, items);
        user.subtractFireVis(1000);
        form(location);
        newConfig(user);
        storeProtectedBlocks();
        storeSignBlock();
        storeFuelBlocks();
        Machines.condensers.put(id, this);
        Machine machine = new Machine();
        machine.Machine(this);
        Machines.machines.put(id, machine);
        Machines.ticCondensers.add(this);
        user.getPlayer().sendMessage("§eThe §5Condenser §eforms before your eyes.");
    }

    public void loadCondenser(int fid) {
        id = fid;
        loadConfig();
        storeProtectedBlocks();
        storeSignBlock();
        storeFuelBlocks();
        Machines.condensers.put(id, this);
        Machine machine = new Machine();
        machine.Machine(this);
        Machines.machines.put(id, machine);
        Machines.ticCondensers.add(this);
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
        List<String> list = netherBlocks();
        VisLocation v = new VisLocation();
        for (int ctr = 0; ctr < list.size(); ctr++) {
            v.loadFromString(list.get(ctr));
            l.getWorld().getBlockAt(v.convertToLocation()).setTypeId(112);
        }

        list = endBlocks();
        for (int ctr = 0; ctr < list.size(); ctr++) {
            v.loadFromString(list.get(ctr));
            l.getWorld().getBlockAt(v.convertToLocation()).setTypeId(121);
        }
        v.loadFromString(chestBlock());
        l.getWorld().getBlockAt(v.convertToLocation()).setData(((byte) 3), true);
        v.loadFromString(signBlock());
        l.getWorld().getBlockAt(v.convertToLocation()).setTypeIdAndData(68, ((byte) 3), true);
        Sign sign = (Sign) l.getWorld().getBlockAt(v.convertToLocation()).getState();
        sign.setLine(0, "§5--Condenser--");
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
        config.set("cvis", 0);
        config.set("ivis1", 0);
        config.set("ivis2", 0);
        config.set("type", "_VISNULL_");
        config.set("itype1", "_VISNULL_");
        config.set("itype2", "_VISNULL_");
        config.set("charge", 0);
        config.set("consumed1", 0);
        config.set("consumed2", 0);
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
        cvis = config.getInt("cvis", 0);
        ivis1 = config.getInt("ivis1", 0);
        ivis2 = config.getInt("ivis2", 0);
        type = config.getString("type", "_VISNULL_");
        itype1 = config.getString("itype1", "_VISNULL_");
        itype2 = config.getString("itype2", "_VISNULL_");
        charge = config.getInt("charge", 0);
        tics = config.getInt("tics", 0);
        consumed1 = config.getInt("consumed1", 0);
        consumed2 = config.getInt("consumed2", 0);
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

    private List<String> netherBlocks() {
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

    private List<String> endBlocks() {
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
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY(), l.getBlockZ() + 2));
        list.add(v.toString());
        v.loadFromLocation(new Location(l.getWorld(), l.getBlockX() + 1, l.getBlockY() - 2, l.getBlockZ() + 3));
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
