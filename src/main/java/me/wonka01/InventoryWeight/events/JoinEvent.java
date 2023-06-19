package me.wonka01.InventoryWeight.events;

import me.wonka01.InventoryWeight.playerweight.PlayerWeight;
import me.wonka01.InventoryWeight.playerweight.PlayerWeightMap;
import me.wonka01.InventoryWeight.util.InventoryCheckUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;

public class JoinEvent implements Listener {

    public static void addPlayerToWeightMap(Player player) {
        Set<PermissionAttachmentInfo> set = player.getEffectivePermissions();
        Iterator iterator = set.iterator();

        // ItemStack item2 = new ItemStack(Material.ACACIA_BOAT, 1);
        // ItemMeta meta2 = item2.getItemMeta();
        // List<String> lore2 = new ArrayList<String>();
        // lore2.add("Capacity: 20");
        // meta2.setLore(lore2);
        // item2.setItemMeta(meta2);
        // player.getInventory().addItem(item2);

        Map<String, Double> weightMap = InventoryCheckUtil.getInventoryWeight(player.getInventory().getContents());
        double inventoryWeight = weightMap.get("totalWeight");

        PlayerWeight playerData = new PlayerWeight(inventoryWeight, player.getUniqueId());
        playerData.setIncreasedCapacity(weightMap.get("totalIncreasedCapacity"));

        while (iterator.hasNext()) {
            PermissionAttachmentInfo info = (PermissionAttachmentInfo) iterator.next();
            if (info.getPermission().contains("inventoryweight.maxweight.")) {
                String amount = info.getPermission().substring(26);
                playerData.setMaxWeight(Integer.parseInt(amount));
                break;
            }
        }

        PlayerWeightMap.getPlayerWeightMap().put(player.getUniqueId(), playerData);
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        addPlayerToWeightMap(player);
    }

    @EventHandler
    public void playerLogoutEvent(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        PlayerWeightMap.getPlayerWeightMap().remove(playerId);
        event.getPlayer().setWalkSpeed(.2f);
    }

}
