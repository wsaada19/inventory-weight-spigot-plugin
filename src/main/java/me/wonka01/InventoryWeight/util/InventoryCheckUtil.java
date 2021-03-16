package me.wonka01.InventoryWeight.util;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

// TODO refactor into a Singleton class
public class InventoryCheckUtil {

    public static HashMap<String, Double> mapOfWeightsByMaterial = new HashMap<String, Double>();
    public static HashMap<String, Double> mapOfWeightsByDisplayName = new HashMap<String, Double>();
    public static HashMap<String, Double> mapOfWeightsByLore = new HashMap<String, Double>();
    public static double defaultWeight;

    public static double getInventoryWeight(ItemStack[] items) {
        double totalWeight = 0;
        for (ItemStack item : items) {
            if (item == null) {
                continue;
            }
            int stackSize = item.getAmount();
            totalWeight += (stackSize * getItemWeight(item));
        }
        return totalWeight;
    }

    public static double getItemWeight(ItemStack item) {
        String name = item.getItemMeta().getDisplayName();
        String type = item.getType().toString();
        String lore = "";
        if (item.getItemMeta().hasLore()) {
            lore = convertListToSingleString(item.getItemMeta().getLore());
        }

        if (mapOfWeightsByDisplayName.containsKey(name)) {
            return mapOfWeightsByDisplayName.get(name);
        } else if (mapOfWeightsByLore.containsKey(lore)) {
            return mapOfWeightsByLore.get(lore);
        }
        if (mapOfWeightsByMaterial.containsKey(type)) {
            return mapOfWeightsByMaterial.get(type);
        } else {
            return defaultWeight;
        }
    }

    public static String convertListToSingleString(List<String> lore) {
        StringBuilder builder = new StringBuilder();
        for (String element : lore) {
            builder.append(element);
        }
        return builder.toString();
    }
}
