package me.wonka01.InventoryWeight.util;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InventoryCheckUtil {
    public static HashMap<String, Double> mapOfWeightsByMaterial = new HashMap<String, Double>();
    public static HashMap<String, Double> mapOfWeightsByDisplayName = new HashMap<String, Double>();
    public static HashMap<String, Double> mapOfWeightsByLore = new HashMap<String, Double>();
    public static boolean armorOnlyMode = false;
    public static String loreTag = "";
    public static String capacityTag = "";
    public static double defaultWeight;

    public static Map<String, Double> getInventoryWeight(ItemStack[] items) {
        double totalWeight = 0;
        double totalIncreasedCapacity = 0;
        if (armorOnlyMode) {
            for (int i = 0; i < items.length; i++) {
                if (items[i] == null || (i < 36 || i > 40)) {
                    continue;
                }
                int stackSize = items[i].getAmount();
                double weight = getItemWeight(items[i]);
                totalWeight += ((double) stackSize * weight);
            }
        } else {
            for (ItemStack item : items) {
                if (item == null) {
                    continue;
                }
                int stackSize = item.getAmount();
                double weight = getItemWeight(item);
                if (weight < 0) {
                    totalIncreasedCapacity += weight * -1.0;
                    continue;
                }
                totalWeight += ((double) stackSize * weight);
            }
        }

        Map<String, Double> weightMap = new HashMap<String, Double>();
        weightMap.put("totalWeight", totalWeight);
        weightMap.put("totalIncreasedCapacity", totalIncreasedCapacity);
        return weightMap;
    }

    public static double getItemWeight(ItemStack item) {
        String name = item.getItemMeta().getDisplayName();
        String type = item.getType().toString();
        String lore = "";
        if (item.getItemMeta().hasLore()) {
            double weight = getWeightFromTag(item.getItemMeta().getLore());
            if (weight != 0) {
                return weight;
            }
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

    public static double getWeightFromTag(List<String> lore) {
        double weight = 0;
        for (String line : lore) {
            if (line.contains(loreTag)) {
                Pattern pattern = Pattern.compile("(?<=\\s)-?\\d+(?:\\.\\d+)?");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    weight = Double.parseDouble(matcher.group(0));
                }
            }

            if (line.contains(capacityTag)) {
                Pattern pattern = Pattern.compile("(?<=\\s)-?\\d+(?:\\.\\d+)?");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    weight = -1 * Double.parseDouble(matcher.group(0));
                }
            }
        }
        return weight;
    }
}
