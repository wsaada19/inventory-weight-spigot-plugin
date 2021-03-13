package me.wonka01.InventoryWeight.playerweight;

import java.util.HashMap;
import java.util.UUID;

public class PlayerWeightMap {

    private static PlayerWeightMap instance = null;
    public HashMap<UUID, PlayerWeight> weightMap;

    private PlayerWeightMap(){
        weightMap = new HashMap<UUID, PlayerWeight>();
    }

    public static HashMap<UUID, PlayerWeight> getPlayerWeightMap(){
        if(instance == null){
            instance = new PlayerWeightMap();
        }
        return instance.weightMap;
    }
}
