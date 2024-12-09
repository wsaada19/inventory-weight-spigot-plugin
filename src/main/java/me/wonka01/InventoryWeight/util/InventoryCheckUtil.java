package me.wonka01.InventoryWeight.util;

import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import me.wonka01.InventoryWeight.playerweight.ItemLimit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InventoryCheckUtil {
    public static HashMap<String, Double> mapOfWeightsByMaterial = new HashMap<String, Double>();
    public static HashMap<String, Double> mapOfWeightsByDisplayName = new HashMap<String, Double>();
    public static HashMap<String, Double> mapOfWeightsByLore = new HashMap<String, Double>();
    public static HashMap<Integer, Double> mapOfWeightsByCustomModelId = new HashMap<Integer, Double>();
    public static ArrayList<ItemLimit> mapOfItemLimits = new ArrayList<ItemLimit>();
    public static boolean armorOnlyMode = false;
    public static String loreTag = "";
    public static String capacityTag = "";
    public static double defaultWeight;

    public static Map<String, Double> getInventoryWeight(ItemStack[] items, Player player) {
        double totalWeight = 0;
        double totalIncreasedCapacity = 0;
        HashMap<String, Integer> countMap = new HashMap<String, Integer>();

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

                int customModelId = Items.getCustomModelData(item);

                if (customModelId != -1) {
                    String modelString = String.valueOf(customModelId);
                    if (countMap.containsKey(modelString)) {
                        countMap.put(modelString,
                                countMap.get(modelString) + item.getAmount());
                    } else {
                        countMap.put(modelString, item.getAmount());
                    }
                }

                if (countMap.containsKey(item.getType().toString())) {
                    countMap.put(item.getType().toString(), countMap.get(item.getType().toString()) + item.getAmount());
                } else {
                    countMap.put(item.getType().toString(), item.getAmount());
                }

                int stackSize = item.getAmount();
                double weight = getItemWeight(item);
                if (item.getType().toString().contains("SHULKER_BOX")) {
                    BlockStateMeta im = (BlockStateMeta) item.getItemMeta();
                    if (im.getBlockState() instanceof ShulkerBox) {
                        ShulkerBox shulker = (ShulkerBox) im.getBlockState();
                        ItemStack[] shulkerItems = shulker.getInventory().getContents();
                        for (ItemStack shulkerItem : shulkerItems) {
                            if (shulkerItem == null) {
                                continue;
                            }
                            int shulkerStackSize = shulkerItem.getAmount();
                            double shulkerWeight = getItemWeight(shulkerItem);
                            totalWeight += ((double) shulkerStackSize * shulkerWeight);
                        }
                    }
                }
                if (weight < 0) {
                    totalIncreasedCapacity += weight * -1.0;
                    continue;
                }

                totalWeight += ((double) stackSize * weight);
            }
        }
        Map<String, Double> weightMap = new HashMap<String, Double>();

        Map<String, Integer> finalCountMap = new HashMap<String, Integer>();
        mapOfItemLimits.forEach((value) -> {
            String itemName = value.getMaterial();
            String modelId = String.valueOf(value.getCustomModelId());
            countMap.forEach((key2, value2) -> {
                if (key2.contains(itemName)) {
                    if (finalCountMap.containsKey(itemName)) {
                        finalCountMap.put(itemName, finalCountMap.get(itemName) + value2);
                    } else {
                        finalCountMap.put(itemName, value2);
                    }
                } else if (key2.equalsIgnoreCase(modelId)) {
                    if (finalCountMap.containsKey(modelId)) {
                        finalCountMap.put(modelId, finalCountMap.get(modelId) + value2);
                    } else {
                        finalCountMap.put(modelId, value2);
                    }
                }
            });
        });

        finalCountMap.forEach((key, value) -> {
            for (ItemLimit itemLimit : mapOfItemLimits) {
                if (itemLimit.getMaterial().contains(key)
                        || key.contains(String.valueOf(itemLimit.getCustomModelId()))) {
                    if (value > itemLimit.getLimit() && itemLimit.hasPermssion(player)) {
                        weightMap.put("overLimit", 1.0);
                        return;
                    }
                }
            }
        });

        weightMap.put("totalWeight", totalWeight);
        weightMap.put("totalIncreasedCapacity", totalIncreasedCapacity);
        if (!weightMap.containsKey("overLimit")) {
            weightMap.put("overLimit", 0.0);
        }
        return weightMap;
    }

    public static double getItemWeight(ItemStack item) {
        String name = item.getItemMeta().getDisplayName();
        String type = item.getType().toString();
        String lore = "";
        int modelId = Items.getCustomModelData(item);

        if (modelId != -1 && mapOfWeightsByCustomModelId.containsKey(modelId)) {
            return mapOfWeightsByCustomModelId.get(modelId);
        }

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
