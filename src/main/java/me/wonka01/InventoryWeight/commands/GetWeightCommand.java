package me.wonka01.InventoryWeight.commands;

import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import me.wonka01.InventoryWeight.util.InventoryCheckUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GetWeightCommand extends SubCommand {
    @Override
    public void onCommand(Player player, String[] args) {

        if (args.length < 2) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.AIR) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getItemWeight() + " " + 0));
            } else {
                double weight = InventoryCheckUtil.getItemWeight(item);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getItemWeight() + " " + weight));
            }
        } else {
            String materialAllCaps = args[1].toUpperCase();
            if (Material.getMaterial(materialAllCaps) == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getInvalidMaterial()));
                return;
            }

            double weight = InventoryCheckUtil.mapOfWeightsByMaterial.get(materialAllCaps);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getItemWeight() + " " + weight));
        }
    }

    @Override
    public String name() {
        return "get";
    }

    @Override
    public String info() {
        return "/iw get [Material Name]";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
