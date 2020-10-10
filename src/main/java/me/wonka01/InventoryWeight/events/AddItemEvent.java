package me.wonka01.InventoryWeight.events;

import me.wonka01.InventoryWeight.InventoryCheckUtil;
import me.wonka01.InventoryWeight.InventoryWeight;
import me.wonka01.InventoryWeight.PlayerWeight;
import me.wonka01.InventoryWeight.WeightSingleton;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AddItemEvent implements Listener {

    @EventHandler
    public void onEntityPickup(EntityPickupItemEvent event){
        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player)event.getEntity();

        if(player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        if(player.hasPermission("inventoryweight.off")) {
            return;
        }

        ItemStack itemPickedUp = event.getItem().getItemStack();

        double weight = InventoryCheckUtil.getItemWeight(itemPickedUp.getType().toString(), itemPickedUp.getItemMeta().getDisplayName());

        double amount = (itemPickedUp.getAmount() * weight);

        double itemCount = InventoryCheckUtil.getInventoryWeight(player.getInventory().getContents());

        if(WeightSingleton.getPlayerWeightMap().get(player.getUniqueId()) == null){
            WeightSingleton.getPlayerWeightMap().put(player.getUniqueId(), new PlayerWeight(itemCount, player.getUniqueId()));
        }

        double oldWeight = WeightSingleton.getPlayerWeightMap().get(player.getUniqueId()).getWeight();


        WeightSingleton.getPlayerWeightMap().get(player.getUniqueId()).setWeight(oldWeight + amount);
        if(JavaPlugin.getPlugin(InventoryWeight.class).showWeightChange){
            player.sendMessage(ChatColor.RED + "Your weight has risen from " + oldWeight + " to " + (amount+oldWeight));
        }

    }
}
