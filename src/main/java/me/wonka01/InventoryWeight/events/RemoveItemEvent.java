package me.wonka01.InventoryWeight.events;

import me.wonka01.InventoryWeight.InventoryWeight;
import me.wonka01.InventoryWeight.playerweight.PlayerWeightMap;
import me.wonka01.InventoryWeight.util.InventoryCheckUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public class RemoveItemEvent implements Listener {
    @EventHandler
    public void onEntityDropEvent(PlayerDropItemEvent event) {

        Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE) || player.hasPermission("inventoryweight.off")) {
            return;
        }

        ItemStack itemDropped = event.getItemDrop().getItemStack();
        int amountDropped = itemDropped.getAmount();

        double weight = InventoryCheckUtil.getItemWeight(itemDropped);

        double oldWeight = PlayerWeightMap.getPlayerWeightMap().get(player.getUniqueId()).getWeight();


        PlayerWeightMap.getPlayerWeightMap().get(player.getUniqueId()).setWeight(oldWeight - (amountDropped * weight));

        if (JavaPlugin.getPlugin(InventoryWeight.class).showWeightChange) {
            player.sendMessage(ChatColor.GREEN + "Your weight has fallen from " + oldWeight + " to " + (oldWeight - (amountDropped * weight)));
        }
    }
}
