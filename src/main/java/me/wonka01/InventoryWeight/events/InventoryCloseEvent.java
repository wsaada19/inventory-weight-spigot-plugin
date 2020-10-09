package me.wonka01.InventoryWeight.events;

import me.wonka01.InventoryWeight.InventoryCheckUtil;
import me.wonka01.InventoryWeight.PlayerWeight;
import me.wonka01.InventoryWeight.WeightSingleton;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InventoryCloseEvent implements Listener {

    @EventHandler
    public void onInventoryClose(org.bukkit.event.inventory.InventoryCloseEvent event)
    {
        if(!(event.getPlayer() instanceof Player)){
            return;
        }
        Player player = (Player) event.getPlayer();

        if(player.getGameMode().equals(GameMode.CREATIVE)){
            return;
        }

        if(player.hasPermission("inventoryweight.off"))
        {
            return;
        }

        int itemCount = InventoryCheckUtil.getInventoryWeight(player.getInventory().getContents());

        if(WeightSingleton.getPlayerWeightMap().get(player.getUniqueId()) == null){
            WeightSingleton.getPlayerWeightMap().put(player.getUniqueId(), new PlayerWeight(itemCount, player.getUniqueId()));
        }

        if(itemCount == WeightSingleton.getPlayerWeightMap().get(player.getUniqueId()).getWeight()){
            return;
        }

        WeightSingleton.getPlayerWeightMap().get(player.getUniqueId()).setWeight(itemCount);
    }
}
