package me.wonka01.InventoryWeight.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.wonka01.InventoryWeight.PlayerWeight;
import me.wonka01.InventoryWeight.WeightSingleton;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
        return "1.17";
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

        if(identifier.equals("speed")){
            if(player.isOnline()){
                Player onlinePlayer = (Player)player;
                float speed = onlinePlayer.getWalkSpeed();
                return String.valueOf(speed);
            }
            return "0.0";
        }

        if(identifier.equals("inventorybar")){
            if(WeightSingleton.getPlayerWeightMap().containsKey(player.getUniqueId())){
                PlayerWeight weight = WeightSingleton.getPlayerWeightMap().get(player.getUniqueId());
                return weight.getSpeedDisplay();
            }
            return "";
        }

        // We return null if an invalid placeholder (f.e. %example_placeholder3%)
        // was provided
        return null;
    }
}

