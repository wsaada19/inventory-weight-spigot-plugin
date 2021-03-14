package me.wonka01.InventoryWeight.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.wonka01.InventoryWeight.playerweight.PlayerWeight;
import me.wonka01.InventoryWeight.playerweight.PlayerWeightMap;
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
        return "1.2";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier){

        if(identifier.equals("weight")){
            if(PlayerWeightMap.getPlayerWeightMap().containsKey(player.getUniqueId())){
                PlayerWeight weight = PlayerWeightMap.getPlayerWeightMap().get(player.getUniqueId());
                return String.valueOf(weight.getWeight());
            }
            return "0";
        }

        // %example_placeholder2%
        if(identifier.equals("maxweight")){
            if(PlayerWeightMap.getPlayerWeightMap().containsKey(player.getUniqueId())){
                PlayerWeight weight = PlayerWeightMap.getPlayerWeightMap().get(player.getUniqueId());
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

        if(identifier.equals("weightPercentage")) {
            if(PlayerWeightMap.getPlayerWeightMap().containsKey(player.getUniqueId())){
                PlayerWeight weight = PlayerWeightMap.getPlayerWeightMap().get(player.getUniqueId());
                double weightPercentage = weight.getWeight() / weight.getMaxWeight();
                return (weightPercentage + "%");
            }
            return "0.0%";
        }

        if(identifier.equals("inventorybar")){
            if(PlayerWeightMap.getPlayerWeightMap().containsKey(player.getUniqueId())){
                PlayerWeight weight = PlayerWeightMap.getPlayerWeightMap().get(player.getUniqueId());
                return weight.getSpeedDisplay();
            }
            return "";
        }
        return null;
    }
}

