package me.wonka01.InventoryWeight.events;

import me.wonka01.InventoryWeight.InventoryWeight;
import me.wonka01.InventoryWeight.playerweight.PlayerWeight;
import me.wonka01.InventoryWeight.playerweight.PlayerWeightMap;
import me.wonka01.InventoryWeight.util.InventoryCheckUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AddItemEvent implements Listener {

    @EventHandler
    public void onEntityPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (player.getGameMode().equals(GameMode.CREATIVE) || player.hasPermission("inventoryweight.off")) {
            return;
        }

        ItemStack itemPickedUp = event.getItem().getItemStack();

        double weight = InventoryCheckUtil.getItemWeight(itemPickedUp);

        double amount = (itemPickedUp.getAmount() * weight);

        if (PlayerWeightMap.getPlayerWeightMap().get(player.getUniqueId()) == null) {
            double totalWeight = InventoryCheckUtil.getInventoryWeight(player.getInventory().getContents());
            PlayerWeightMap.getPlayerWeightMap().put(player.getUniqueId(), new PlayerWeight(totalWeight, player.getUniqueId()));
        }

        double oldWeight = PlayerWeightMap.getPlayerWeightMap().get(player.getUniqueId()).getWeight();

        PlayerWeightMap.getPlayerWeightMap().get(player.getUniqueId()).setWeight(oldWeight + amount);
        if (JavaPlugin.getPlugin(InventoryWeight.class).showWeightChange) {
            player.sendMessage(ChatColor.RED + "Your weight has risen from " + oldWeight + " to " + (amount + oldWeight));
        }

    }
}
