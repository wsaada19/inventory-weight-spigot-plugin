package me.wonka01.InventoryWeight;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InventoryCheckUtil {

    public static HashMap<String, Double> mapOfWeights = new HashMap<String, Double>();
    public static double defaultWeight;

    public static double getInventoryWeight(ItemStack[] items)
    {
        double itemCount = 0;
        for(ItemStack item : items){
            if(item == null){
                continue;
            }
            int stackSize = item.getAmount();
            itemCount += (stackSize * getItemWeight(item.getType().toString()));
        }
        return itemCount;
    }

    public static double getItemWeight(String itemName){
        if( mapOfWeights.containsKey(itemName)){
            return mapOfWeights.get(itemName);
        } else {
            return defaultWeight;
        }
    }
}
