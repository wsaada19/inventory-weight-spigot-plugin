package me.wonka01.InventoryWeight.playerweight;

import org.bukkit.Material;
import org.bukkit.entity.Player;

// Class that represents the item limit with the limit and the permission needed for it to be counted
public class ItemLimit {
    private int limit;
    private String permission;
    private String material;
    private int customModelId;

    public ItemLimit(int limit, String permission, String material) {
        this.limit = limit;
        this.permission = permission;
        this.material = material;
        this.customModelId = -1;
    }

    public ItemLimit(int limit, String permission, int customModelId) {
        this.limit = limit;
        this.permission = permission;
        this.material = "MODELID";
        this.customModelId = customModelId;
    }

    public int getLimit() {
        return limit;
    }

    public int getCustomModelId() {
        return customModelId;
    }

    public String getMaterial() {
        return material.toUpperCase();
    }

    public String getPermission() {
        return permission;
    }

    public boolean hasPermssion(Player player) {
        if (permission == null || permission.isEmpty()) {
            return true;
        }

        return player.hasPermission(permission);
    }
}
