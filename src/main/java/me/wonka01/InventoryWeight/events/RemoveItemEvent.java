package me.wonka01.InventoryWeight.events;

import me.wonka01.InventoryWeight.InventoryCheckUtil;
import me.wonka01.InventoryWeight.InventoryWeight;
import me.wonka01.InventoryWeight.WeightSingleton;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public class RemoveItemEvent implements Listener {

    ArrayList<UUID> list = new ArrayList<UUID>();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        if(event.getMessage().contains("/give")){
            list.add(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onEntityDropEvent(PlayerDropItemEvent event){

        Player player = event.getPlayer();
        if(player.getGameMode().equals(GameMode.CREATIVE)){
            return;
        }

        if(player.hasPermission("inventoryweight.off")) {
            return;
        }

        if(list.contains(player.getUniqueId())){
            list.remove(player.getUniqueId());
            return;
        }

        int amountDropped = event.getItemDrop().getItemStack().getAmount();

        double weight = InventoryCheckUtil.getItemWeight(event.getItemDrop().getItemStack().getType().toString());

        double oldWeight = WeightSingleton.getPlayerWeightMap().get(player.getUniqueId()).getWeight();


        WeightSingleton.getPlayerWeightMap().get(player.getUniqueId()).setWeight(oldWeight - (amountDropped * weight));

        if(JavaPlugin.getPlugin(InventoryWeight.class).showWeightChange){
            player.sendMessage(ChatColor.GREEN + "Your weight has fallen from " + oldWeight + " to " + (oldWeight - (amountDropped * weight)));
        }
    }
}
