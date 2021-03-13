package me.wonka01.InventoryWeight.util;

import org.bukkit.Material;

public class MaterialUtil {
    public static boolean isMaterialValid(String materialName) {
        String materialAllCaps = materialName.toUpperCase();
        return (Material.getMaterial(materialAllCaps) != null);

    }
}
