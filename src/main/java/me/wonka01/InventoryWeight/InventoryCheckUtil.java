package me.wonka01.InventoryWeight;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InventoryCheckUtil {

    public static HashMap<String, Integer> mapOfWeights = new HashMap<String, Integer>();
    public static int defaultWeight;

    public static int getInventoryWeight(ItemStack[] items)
    {
        int itemCount = 0;
        for(ItemStack item : items){
            if(item == null){
                continue;
            }
            int stackSize = item.getAmount();
            itemCount += (stackSize * getItemWeight(item.getType().toString()));
        }
        return itemCount;
    }

    public static int getItemWeight(String itemName){
        if( mapOfWeights.containsKey(itemName)){
            return mapOfWeights.get(itemName);
        } else {
            return defaultWeight;
        }
    }
}
