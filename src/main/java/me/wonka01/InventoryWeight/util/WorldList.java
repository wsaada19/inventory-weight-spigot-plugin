package me.wonka01.InventoryWeight.util;

import java.util.ArrayList;
import java.util.List;

public class WorldList {

    private static WorldList instance;

    private List<String> worlds = new ArrayList<String>();

    public static void initializeWorldList(List<String> worlds) {
        instance = new WorldList(worlds);
    }

    public static WorldList getInstance() {
        if(instance == null) {
            return new WorldList(new ArrayList<String>());
        } else {
            return instance;
        }
    }

    private WorldList(List<String> worlds){
        this.worlds.addAll(worlds);
    }

    public boolean isInventoryWeightEnabled(String world) {
        return (worlds.isEmpty() || worlds.contains(world));
    }
}
