package me.wonka01.InventoryWeight.events;

import me.wonka01.InventoryWeight.InventoryCheckUtil;
import me.wonka01.InventoryWeight.PlayerWeight;
import me.wonka01.InventoryWeight.WeightSingleton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class JoinEvent implements Listener {

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event){

        Player player = event.getPlayer();

        Set<PermissionAttachmentInfo> set = player.getEffectivePermissions();
        Iterator iterator = set.iterator();

        double inventoryWeight = InventoryCheckUtil.getInventoryWeight(player.getInventory().getContents());

        PlayerWeight playerData = new PlayerWeight(inventoryWeight, player.getUniqueId());

        while(iterator.hasNext())
        {
            PermissionAttachmentInfo info = (PermissionAttachmentInfo) iterator.next();

            if(info.getPermission().contains("inventoryweight.maxweight."))
            {
                String amount = info.getPermission().substring(26);
                Bukkit.getServer().getConsoleSender().sendMessage(amount);
                playerData.setMaxWeight(Integer.parseInt(amount));
            }
        }
        WeightSingleton.getPlayerWeightMap().put(player.getUniqueId(), playerData);
    }

    @EventHandler
    public void playerLogoutEvent(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        WeightSingleton.getPlayerWeightMap().remove(playerId);
        event.getPlayer().setWalkSpeed((float).2);
    }

}
