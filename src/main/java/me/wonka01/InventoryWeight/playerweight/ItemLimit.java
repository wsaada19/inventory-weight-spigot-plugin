package me.wonka01.InventoryWeight.playerweight;

import org.bukkit.entity.Player;

// Class that represents the item limit with the limit and the permission needed for it to be counted
public class ItemLimit {
    private int limit;
    private String permission;

    public ItemLimit(int limit, String permission) {
        this.limit = limit;
        this.permission = permission;
    }

    public int getLimit() {
        return limit;
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