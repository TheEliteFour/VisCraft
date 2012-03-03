package net.aesircraft.VisCraft.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.aesircraft.VisCraft.Config;
import net.aesircraft.VisCraft.Data.Machines;
import net.aesircraft.VisCraft.Machines.*;
import net.aesircraft.VisCraft.VisCraft;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class User {

    private Player player;
    private String name;

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    private void setPlayer(Player p) {
        player = p;
    }

    private void setName() {
        name = player.getName();
    }

    public void load(Player player) {
        setPlayer(player);
        setName();
    }

    private YamlConfiguration getFile() {
        File dir = new File(VisCraft.getStatic().getDataFolder() + File.separator + "Users");
        File file = new File(VisCraft.getStatic().getDataFolder() + File.separator + "Users" + File.separator + name.toLowerCase() + ".usr");
        if (!dir.exists()) {
            dir.mkdir();
        }
        boolean newFile = false;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                VisCraft.logger.severe("[VisCraft] failed to create " + name + "'s file!");
            }
            newFile = true;
        }

        YamlConfiguration config = new YamlConfiguration();
        if (newFile) {
            try {
                config.load(file);
            } catch (FileNotFoundException ex) {
                VisCraft.logger.severe("[VisCraft] failed to find " + name + "'s file!");
            } catch (IOException ex) {
                VisCraft.logger.severe("[VisCraft] failed to load " + name + "'s file!");
            } catch (InvalidConfigurationException ex) {
                VisCraft.logger.severe("[VisCraft] you fudged up your " + name + "'s file!");
            }
            config.set("vis", 0);
            config.set("fire-vis", 0);
            config.set("water-vis", 0);
            config.set("earth-vis", 0);
            config.set("air-vis", 0);
            config.set("dark-vis", 0);
            config.set("pure-vis", 0);
            config.set("brilliant-vis", 0);
            config.set("ethereal-vis", 0);
            config.set("unstable-vis", 0);
            try {
                config.save(file);
            } catch (IOException ex) {
                VisCraft.logger.severe("[VisCraft] failed to write " + name + "'s file!");
            }
        }
        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
            VisCraft.logger.severe("[VisCraft] failed to find " + name + "'s file!");
        } catch (IOException ex) {
            VisCraft.logger.severe("[VisCraft] failed to load " + name + "'s file!");
        } catch (InvalidConfigurationException ex) {
            VisCraft.logger.severe("[VisCraft] you fudged up your " + name + "'s file!");
        }
        return config;
    }

    private void saveFile(YamlConfiguration config) {
        try {
            config.save(new File(VisCraft.getStatic().getDataFolder() + File.separator + "Users" + File.separator + name.toLowerCase() + ".usr"));
        } catch (IOException ex) {
            VisCraft.logger.severe("[VisCraft] failed to write " + name + "'s file!");
        }
    }

    public int getVis() {
        YamlConfiguration config = getFile();
        return config.getInt("vis");
    }

    public void setVis(int amount) {
        YamlConfiguration config = getFile();
        config.set("vis", amount);
        saveFile(config);
    }

    public boolean hasVis(int amount) {
        return (amount <= getVis());
    }

    public boolean hasRoomForVis(int amount) {
        return (amount <= (500000 - getVis()));
    }

    public void addVis(int amount) {
        setVis(getVis() + amount);
    }

    public void subtractVis(int amount) {
        if (amount > getVis()) {
            amount = getVis();
        }
        setVis(getVis() - amount);
    }

    public int getFireVis() {
        YamlConfiguration config = getFile();
        return config.getInt("fire-vis");
    }

    public void setFireVis(int amount) {
        YamlConfiguration config = getFile();
        config.set("fire-vis", amount);
        saveFile(config);
    }

    public boolean hasFireVis(int amount) {
        return (amount <= getFireVis());
    }

    public boolean hasRoomForFireVis(int amount) {
        return (amount <= (500000 - getFireVis()));
    }

    public void addFireVis(int amount) {
        setFireVis(getFireVis() + amount);
    }

    public void subtractFireVis(int amount) {
        if (amount > getFireVis()) {
            amount = getFireVis();
        }
        setFireVis(getFireVis() - amount);
    }

    public int getWaterVis() {
        YamlConfiguration config = getFile();
        return config.getInt("water-vis");
    }

    public void setWaterVis(int amount) {
        YamlConfiguration config = getFile();
        config.set("water-vis", amount);
        saveFile(config);
    }

    public boolean hasWaterVis(int amount) {
        return (amount <= getWaterVis());
    }

    public boolean hasRoomForWaterVis(int amount) {
        return (amount <= (500000 - getWaterVis()));
    }

    public void addWaterVis(int amount) {
        setWaterVis(getWaterVis() + amount);
    }

    public void subtractWaterVis(int amount) {
        if (amount > getWaterVis()) {
            amount = getWaterVis();
        }
        setWaterVis(getWaterVis() - amount);
    }

    public int getEarthVis() {
        YamlConfiguration config = getFile();
        return config.getInt("earth-vis");
    }

    public void setEarthVis(int amount) {
        YamlConfiguration config = getFile();
        config.set("earth-vis", amount);
        saveFile(config);
    }

    public boolean hasEarthVis(int amount) {
        return (amount <= getEarthVis());
    }

    public boolean hasRoomForEarthVis(int amount) {
        return (amount <= (500000 - getEarthVis()));
    }

    public void addEarthVis(int amount) {
        setEarthVis(getEarthVis() + amount);
    }

    public void subtractEarthVis(int amount) {
        if (amount > getEarthVis()) {
            amount = getEarthVis();
        }
        setEarthVis(getEarthVis() - amount);
    }

    public int getAirVis() {
        YamlConfiguration config = getFile();
        return config.getInt("air-vis");
    }

    public void setAirVis(int amount) {
        YamlConfiguration config = getFile();
        config.set("air-vis", amount);
        saveFile(config);
    }

    public boolean hasAirVis(int amount) {
        return (amount <= getAirVis());
    }

    public boolean hasRoomForAirVis(int amount) {
        return (amount <= (500000 - getAirVis()));
    }

    public void addAirVis(int amount) {
        setAirVis(getAirVis() + amount);
    }

    public void subtractAirVis(int amount) {
        if (amount > getAirVis()) {
            amount = getAirVis();
        }
        setAirVis(getAirVis() - amount);
    }

    public int getDarkVis() {
        YamlConfiguration config = getFile();
        return config.getInt("dark-vis");
    }

    public void setDarkVis(int amount) {
        YamlConfiguration config = getFile();
        config.set("dark-vis", amount);
        saveFile(config);
    }

    public boolean hasDarkVis(int amount) {
        return (amount <= getDarkVis());
    }

    public boolean hasRoomForDarkVis(int amount) {
        return (amount <= (500000 - getDarkVis()));
    }

    public void addDarkVis(int amount) {
        setDarkVis(getDarkVis() + amount);
    }

    public void subtractDarkVis(int amount) {
        if (amount > getDarkVis()) {
            amount = getDarkVis();
        }
        setDarkVis(getDarkVis() - amount);
    }

    public int getPureVis() {
        YamlConfiguration config = getFile();
        return config.getInt("pure-vis");
    }

    public void setPureVis(int amount) {
        YamlConfiguration config = getFile();
        config.set("pure-vis", amount);
        saveFile(config);
    }

    public boolean hasPureVis(int amount) {
        return (amount <= getPureVis());
    }

    public boolean hasRoomForPureVis(int amount) {
        return (amount <= (500000 - getPureVis()));
    }

    public void addPureVis(int amount) {
        setPureVis(getPureVis() + amount);
    }

    public void subtractPureVis(int amount) {
        if (amount > getPureVis()) {
            amount = getPureVis();
        }
        setPureVis(getPureVis() - amount);
    }

    public int getBrilliantVis() {
        YamlConfiguration config = getFile();
        return config.getInt("brilliant-vis");
    }

    public void setBrilliantVis(int amount) {
        YamlConfiguration config = getFile();
        config.set("brilliant-vis", amount);
        saveFile(config);
    }

    public boolean hasBrilliantVis(int amount) {
        return (amount <= getBrilliantVis());
    }

    public boolean hasRoomForBrilliantVis(int amount) {
        return (amount <= (500000 - getBrilliantVis()));
    }

    public void addBrilliantVis(int amount) {
        setBrilliantVis(getBrilliantVis() + amount);
    }

    public void subtractBrilliantVis(int amount) {
        if (amount > getBrilliantVis()) {
            amount = getBrilliantVis();
        }
        setBrilliantVis(getBrilliantVis() - amount);
    }

    public int getEtherealVis() {
        YamlConfiguration config = getFile();
        return config.getInt("ethereal-vis");
    }

    public void setEtherealVis(int amount) {
        YamlConfiguration config = getFile();
        config.set("ethereal-vis", amount);
        saveFile(config);
    }

    public boolean hasEtherealVis(int amount) {
        return (amount <= getEtherealVis());
    }

    public boolean hasRoomForEtherealVis(int amount) {
        return (amount <= (500000 - getEtherealVis()));
    }

    public void addEtherealVis(int amount) {
        setEtherealVis(getEtherealVis() + amount);
    }

    public void subtractEtherealVis(int amount) {
        if (amount > getEtherealVis()) {
            amount = getEtherealVis();
        }
        setEtherealVis(getEtherealVis() - amount);
    }

    public int getUnstableVis() {
        YamlConfiguration config = getFile();
        return config.getInt("unstable-vis");
    }

    public void setUnstableVis(int amount) {
        YamlConfiguration config = getFile();
        config.set("unstable-vis", amount);
        saveFile(config);
    }

    public boolean hasUnstableVis(int amount) {
        return (amount <= getUnstableVis());
    }

    public boolean hasRoomForUnstableVis(int amount) {
        return (amount <= (500000 - getUnstableVis()));
    }

    public void addUnstableVis(int amount) {
        setUnstableVis(getUnstableVis() + amount);
    }

    public void subtractUnstableVis(int amount) {
        if (amount > getUnstableVis()) {
            amount = getUnstableVis();
        }
        setUnstableVis(getUnstableVis() - amount);
    }

    public boolean hasPermission() {
        if (hasAdminPermission()) {
            return true;
        }
        if (!Config.permissions) {
            return true;
        }
        return VisCraft.permission.has(player, "viscraft.user");
    }

    public boolean hasAdminPermission() {
        if (!Config.permissions && player.isOp()) {
            return true;
        }
        if (!Config.permissions && !player.isOp()) {
            return false;
        }
        return VisCraft.permission.has(player, "viscraft.admin");
    }

    public void showVis() {
        player.sendMessage("§eCurrent §5Vis§e: §5" + getVis());
    }

    public void showInfusedVis() {
        player.sendMessage("§eCurrent §5Infused Vis§e. §5");
        player.sendMessage("§e----------------------------");
        player.sendMessage("§4Fire Infused Vis§e: §5" + getFireVis());
        player.sendMessage("§1Water Infused Vis§e: §5" + getWaterVis());
        player.sendMessage("§2Earth Infused Vis§e: §5" + getEarthVis());
        player.sendMessage("§6Air Infused Vis§e: §5" + getAirVis());
    }

    public void showCondensedVis() {
        player.sendMessage("§eCurrent §5Condensed Vis§e. §5");
        player.sendMessage("§e----------------------------");
        player.sendMessage("§8Dark Condensed Vis§e: §5" + getDarkVis());
        player.sendMessage("§fPure Condensed Vis§e: §5" + getPureVis());
        player.sendMessage("§aBrilliant Condensed Vis§e: §5" + getBrilliantVis());
        player.sendMessage("§bEthereal Condensed Vis§e: §5" + getEtherealVis());
        player.sendMessage("§cUnstable Condensed Vis§e: §5" + getUnstableVis());
    }

    public void died() {
        if (hasPermission()) {
            if (Config.loss) {
                setVis(0);
                setEarthVis(0);
                setWaterVis(0);
                setFireVis(0);
                setAirVis(0);
                setDarkVis(0);
                setPureVis(0);
                setBrilliantVis(0);
                setEtherealVis(0);
                if (Config.explosions) {
                    int vis = getUnstableVis();
                    if (vis > 0) {
                        float power = 1;
                        if (vis > 100) {
                            power = 2;
                        }
                        if (vis > 500) {
                            power = 3;
                        }
                        if (vis > 1000) {
                            power = 4;
                        }
                        if (vis > 5000) {
                            power = 5;
                        }
                        if (vis > 10000) {
                            power = 10;
                        }
                        if (vis > 100000) {
                            power = 20;
                        }
                        player.getWorld().createExplosion(player.getLocation(), power);
                    }
                }
                setUnstableVis(0);
                player.sendMessage("§4Your §5Vis§4 has scattered to the depths of §5The Void§4.");
            }
        }
    }

    public void checkInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        Location l = e.getClickedBlock().getLocation();
        Extractor ext = new Extractor();
        Infuser inf = new Infuser();
        Condenser con = new Condenser();
        Collector col = new Collector();
        if (ext.checkSignature(l)) {
            if (!hasPermission()) {
                getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
                return;
            }
            ext.newExtractor(this, l);
        }
        if (inf.checkSignature(l)) {
            if (!hasPermission()) {
                getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
                return;
            }
            inf.newInfuser(this, l);
        }
        if (con.checkSignature(l)) {
            if (!hasPermission()) {
                getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
                return;
            }
            con.newCondenser(this, l);
        }
        if (col.checkSignature(l)) {
            if (!hasPermission()) {
                getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
                return;
            }
            col.newCollector(this, l);
        }
        interactGate(e);

    }

    private void interactGate(PlayerInteractEvent e) {


        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getTypeId() != 0) {
                if (e.getClickedBlock().getTypeId() == 20) {
                    Rune rune = new Rune();
                    if (rune.checkSignature(e.getClickedBlock().getLocation())) {
                        rune.executeRune(this, e.getClickedBlock().getLocation());
                        return;
                    }
                }
                if (e.getClickedBlock().getTypeId() == 68) {
                    if (((Sign) e.getClickedBlock().getState()).getLine(0).equals("§5--Extractor--")) {
                        if (!hasPermission()) {
                            getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
                            return;
                        }
                        int id = Integer.parseInt(((Sign) e.getClickedBlock().getState()).getLine(2));
                        Extractor extractor = Machines.extractors.get(id);
                        if (e.getPlayer().getItemInHand() != null) {
                            if (e.getPlayer().getItemInHand().getTypeId() == 374) {
                                extractor.extract(this);
                                return;
                            }
                            if (e.getPlayer().getItemInHand().getTypeId() == 369) {
                                extractor.upgrade(this);
                                return;
                            }
                        }
                        extractor.announce(this);
                    }


                    if (((Sign) e.getClickedBlock().getState()).getLine(0).equals("§5--Infuser--")) {
                        if (!hasPermission()) {
                            getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
                            return;
                        }
                        int id = Integer.parseInt(((Sign) e.getClickedBlock().getState()).getLine(2));
                        Infuser infuser = Machines.infusers.get(id);
                        if (e.getPlayer().getItemInHand() != null) {
                            if (e.getPlayer().getItemInHand().getTypeId() == 374) {
                                infuser.extract(this);
                                return;
                            }
                            if (e.getPlayer().getItemInHand().getTypeId() == 325) {
                                infuser.takeVis(this);
                                return;
                            }
                            if (e.getPlayer().getItemInHand().getTypeId() == 281) {
                                infuser.inputVis(this);
                                return;
                            }
                            if (e.getPlayer().getItemInHand().getTypeId() == 369) {
                                infuser.upgrade(this);
                                return;
                            }
                            if (e.getPlayer().getItemInHand().getTypeId() == 280) {
                                infuser.clearType(this);
                                return;
                            }
                        }
                        infuser.announce(this);
                    }




                    if (((Sign) e.getClickedBlock().getState()).getLine(0).equals("§5--Condenser--")) {
                        if (!hasPermission()) {
                            getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
                            return;
                        }
                        int id = Integer.parseInt(((Sign) e.getClickedBlock().getState()).getLine(2));
                        Condenser condenser = Machines.condensers.get(id);
                        if (e.getPlayer().getItemInHand() != null) {
                            if (e.getPlayer().getItemInHand().getTypeId() == 374) {
                                condenser.extract(this);
                                return;
                            }
                            if (e.getPlayer().getItemInHand().getTypeId() == 325) {
                                condenser.takeIVis(this);
                                return;
                            }
                            if (e.getPlayer().getItemInHand().getTypeId() == 369) {
                                condenser.upgrade(this);
                                return;
                            }
                            condenser.inputIVis(this);
                            if (e.getPlayer().getItemInHand().getTypeId() == 0) {
                                condenser.announce(this);
                            }
                            return;

                        }
                        condenser.announce(this);
                    }

                    if (((Sign) e.getClickedBlock().getState()).getLine(0).equals("§5--Collector--")) {
                        if (!hasPermission()) {
                            getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
                            return;
                        }
                        int id = Integer.parseInt(((Sign) e.getClickedBlock().getState()).getLine(2));
                        Collector collector = Machines.collectors.get(id);
                        if (e.getPlayer().getItemInHand() != null) {
                            collector.extract(this);
                        }

                    }


                }
            }
        }
    }

    public void checkRemove(BlockBreakEvent e) {
        if (e.getBlock() != null) {
            if (e.getBlock().getTypeId() != 0) {
                if (e.getBlock().getTypeId() == 68) {
                    if (((Sign) e.getBlock().getState()).getLine(0).equals("§5--Extractor--")) {
                        removeExtractor(((Sign) e.getBlock().getState()), e);
                    }
                    if (((Sign) e.getBlock().getState()).getLine(0).equals("§5--Condenser--")) {
                        removeCondenser(((Sign) e.getBlock().getState()), e);
                    }
                    if (((Sign) e.getBlock().getState()).getLine(0).equals("§5--Infuser--")) {
                        removeInfuser(((Sign) e.getBlock().getState()), e);
                    }
                    if (((Sign) e.getBlock().getState()).getLine(0).equals("§5--Collector--")) {
                        removeExtractor(((Sign) e.getBlock().getState()), e);
                    }
                }
            }
        }
    }

    public void removeExtractor(Sign sign, BlockBreakEvent e) {
        if (!hasPermission()) {
            getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
            return;
        }
        int id = Integer.parseInt(sign.getLine(2));
        Extractor extractor = Machines.extractors.get(id);
        if (extractor.remove(this)) {
            getPlayer().sendMessage("§eYou have removed this §5Extractor§4.");
            return;
        }
        getPlayer().sendMessage("§4You have failed to remove this §5Extractor§4.");
        e.setCancelled(true);
    }

    public void removeCollector(Sign sign, BlockBreakEvent e) {
        if (!hasPermission()) {
            getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
            return;
        }
        int id = Integer.parseInt(sign.getLine(2));
        Collector collector = Machines.collectors.get(id);
        if (collector.remove(this)) {
            getPlayer().sendMessage("§eYou have removed this §5Collector§4.");
            return;
        }
        getPlayer().sendMessage("§4You have failed to remove this §5Collector§4.");
        e.setCancelled(true);
    }

    public void removeCondenser(Sign sign, BlockBreakEvent e) {
        if (!hasPermission()) {
            getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
            return;
        }
        int id = Integer.parseInt(sign.getLine(2));
        Condenser condenser = Machines.condensers.get(id);
        if (condenser.remove(this)) {
            getPlayer().sendMessage("§eYou have removed this §5Condenser§4.");
            return;
        }
        getPlayer().sendMessage("§4You have failed to remove this §5Condenser§4.");
        e.setCancelled(true);
    }

    public void removeInfuser(Sign sign, BlockBreakEvent e) {
        if (!hasPermission()) {
            getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
            return;
        }
        int id = Integer.parseInt(sign.getLine(2));
        Infuser infuser = Machines.infusers.get(id);
        if (infuser.remove(this)) {
            getPlayer().sendMessage("§eYou have removed this §5Infuser§4.");
            return;
        }
        getPlayer().sendMessage("§4You have failed to remove this §5Infuser§4.");
        e.setCancelled(true);
    }

    public boolean hasRunePermission(String color) {
        if (!hasPermission()) {
            getPlayer().sendMessage("§4You do not have permission to use §5VisCraft§4.");
            return false;
        }
        if (hasAdminPermission()) {
            return true;
        }
        if (!Config.permissions) {
            return true;
        }
        return VisCraft.permission.has(player, "viscraft." + color);

    }
}
