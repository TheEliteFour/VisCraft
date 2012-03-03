package net.aesircraft.VisCraft.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.aesircraft.VisCraft.Machines.*;
import net.aesircraft.VisCraft.VisCraft;

public class Machines {

    public static HashMap<String, Integer> protectedBlocks = new HashMap<String, Integer>();
    public static List<String> signBlocks = new ArrayList<String>();
    public static List<Integer> safeSpawns = new ArrayList<Integer>();
    public static HashMap<String, Integer> fuelBlocks = new HashMap<String, Integer>();
    public static HashMap<Integer, Extractor> extractors = new HashMap<Integer, Extractor>();
    public static HashMap<Integer, Condenser> condensers = new HashMap<Integer, Condenser>();
    public static HashMap<Integer, Collector> collectors = new HashMap<Integer, Collector>();
    public static HashMap<Integer, Infuser> infusers = new HashMap<Integer, Infuser>();
    public static List<Extractor> ticExtractors = new ArrayList<Extractor>();
    public static List<Infuser> ticInfusers = new ArrayList<Infuser>();
    public static List<Condenser> ticCondensers = new ArrayList<Condenser>();
    public static HashMap<Integer, Machine> machines = new HashMap<Integer, Machine>();

    public static void ticExtractors() {
        for (Extractor e : ticExtractors) {
            e.tic();
        }
    }

    public static void ticCondensers() {
        for (Condenser c : ticCondensers) {
            c.tic();
        }
    }

    public static void ticInfusers() {
        for (Infuser i : ticInfusers) {
            i.tic();
        }
    }

    public static void loadExtractors() {
        File d = new File(VisCraft.getStatic().getDataFolder().toString() + File.separator + "Extractors");
        String[] list = d.list();
        Extractor extractor;
        String[] name;
        if (list == null) {
            return;
        }
        if (list.length < 1) {
            return;
        }
        if (list[0] == null) {
            return;
        }
        if (list[0].equals("")) {
            return;
        }
        for (int ctr = 0; ctr < list.length; ctr++) {
            extractor = new Extractor();
            name = list[ctr].split("\\.(?=[^\\.]+$)");
            extractor.loadExtractor(Integer.parseInt(name[0]));
        }
    }

    public static void loadInfusers() {
        File d = new File(VisCraft.getStatic().getDataFolder().toString() + File.separator + "Infusers");
        String[] list = d.list();
        Infuser infuser;
        String[] name;
        if (list == null) {
            return;
        }
        if (list.length < 1) {
            return;
        }
        if (list[0] == null) {
            return;
        }
        if (list[0].equals("")) {
            return;
        }
        for (int ctr = 0; ctr < list.length; ctr++) {
            infuser = new Infuser();
            name = list[ctr].split("\\.(?=[^\\.]+$)");
            infuser.loadInfuser(Integer.parseInt(name[0]));
        }
    }

    public static void loadCondensers() {
        File d = new File(VisCraft.getStatic().getDataFolder().toString() + File.separator + "Condensers");
        String[] list = d.list();
        Condenser condenser;
        String[] name;
        if (list == null) {
            return;
        }
        if (list.length < 1) {
            return;
        }
        if (list[0] == null) {
            return;
        }
        if (list[0].equals("")) {
            return;
        }
        for (int ctr = 0; ctr < list.length; ctr++) {
            condenser = new Condenser();
            name = list[ctr].split("\\.(?=[^\\.]+$)");
            condenser.loadCondenser(Integer.parseInt(name[0]));
        }
    }

    public static void loadCollectors() {
        File d = new File(VisCraft.getStatic().getDataFolder().toString() + File.separator + "Collectors");
        String[] list = d.list();
        Collector collector;
        String[] name;
        if (list == null) {
            return;
        }
        if (list.length < 1) {
            return;
        }
        if (list[0] == null) {
            return;
        }
        if (list[0].equals("")) {
            return;
        }
        for (int ctr = 0; ctr < list.length; ctr++) {
            collector = new Collector();
            name = list[ctr].split("\\.(?=[^\\.]+$)");
            collector.loadCollector(Integer.parseInt(name[0]));
        }
    }
}
