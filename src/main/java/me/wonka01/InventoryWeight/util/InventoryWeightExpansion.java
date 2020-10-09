package me.wonka01.InventoryWeight.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.wonka01.InventoryWeight.PlayerWeight;
import me.wonka01.InventoryWeight.WeightSingleton;
import org.bukkit.OfflinePlayer;

public class InventoryWeightExpansion extends PlaceholderExpansion {

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getAuthor(){
        return "wonka01";
    }

    @Override
    public String getIdentifier(){
        return "inventoryweight";
    }

    @Override
    public String getVersion(){
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier){

        if(identifier.equals("weight")){
            if(WeightSingleton.getPlayerWeightMap().containsKey(player.getUniqueId())){
                PlayerWeight weight = WeightSingleton.getPlayerWeightMap().get(player.getUniqueId());
                return String.valueOf(weight.getWeight());
            }
            return "0";
        }

        // %example_placeholder2%
        if(identifier.equals("maxweight")){
            if(WeightSingleton.getPlayerWeightMap().containsKey(player.getUniqueId())){
                PlayerWeight weight = WeightSingleton.getPlayerWeightMap().get(player.getUniqueId());
                return String.valueOf(weight.getMaxWeight());
            }
            return String.valueOf(PlayerWeight.defaultMaxCapacity);
        }

        // We return null if an invalid placeholder (f.e. %example_placeholder3%)
        // was provided
        return null;
    }
}

