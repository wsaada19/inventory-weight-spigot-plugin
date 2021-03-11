package me.wonka01.InventoryWeight;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class InventoryCheckUtil {

    public static HashMap<String, Double> mapOfWeights = new HashMap<String, Double>();
    public static HashMap<String, Double> mapOfWeightsByDisplayName = new HashMap<String, Double>();
    public static HashMap<String, Double> mapOfWeightsByLore = new HashMap<String, Double>();
    public static double defaultWeight;

    public static double getInventoryWeight(ItemStack[] items)
    {
        double itemCount = 0;
        for(ItemStack item : items){
            if(item == null){
                continue;
            }
            int stackSize = item.getAmount();

            String loreString = "";
            if(item.getItemMeta().hasLore()){
                loreString = convertListToSingleString(item.getItemMeta().getLore());
            }

            itemCount += (stackSize * getItemWeight(item.getType().toString(), item.getItemMeta().getDisplayName(), loreString));
        }
        return itemCount;
    }

    public static double getItemWeight(String itemName, String displayName, String itemLore){
        if(mapOfWeightsByDisplayName.containsKey(displayName))
        {
            return mapOfWeightsByDisplayName.get(displayName);
        } else if (mapOfWeightsByLore.containsKey(itemLore)){
            return mapOfWeightsByLore.get(itemLore);
        }
        if( mapOfWeights.containsKey(itemName)){
            return mapOfWeights.get(itemName);
        } else {
            return defaultWeight;
        }
    }

    public static String convertListToSingleString(List<String> lore) {
        StringBuilder builder = new StringBuilder();
        for(String element : lore) {
            builder.append(element);
        }
        return builder.toString();
    }
}
