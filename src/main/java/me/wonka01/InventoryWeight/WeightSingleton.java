package me.wonka01.InventoryWeight;

import java.util.HashMap;
import java.util.UUID;

public class WeightSingleton {

    private static WeightSingleton instance = null;
    public HashMap<UUID, PlayerWeight> weightMap;

    private WeightSingleton(){
        weightMap = new HashMap<UUID, PlayerWeight>();
    }

    public static HashMap<UUID, PlayerWeight> getPlayerWeightMap(){
        if(instance == null){
            instance = new WeightSingleton();
        }
        return instance.weightMap;
    }
}
