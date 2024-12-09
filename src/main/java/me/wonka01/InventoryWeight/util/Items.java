package me.wonka01.InventoryWeight.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Items {
    static int getCustomModelData(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return -1;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasCustomModelData()) {
            return meta.getCustomModelData();
        }
        return -1;
    }
}
