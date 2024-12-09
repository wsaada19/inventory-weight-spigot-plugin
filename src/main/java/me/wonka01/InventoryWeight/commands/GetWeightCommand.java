package me.wonka01.InventoryWeight.commands;

import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import me.wonka01.InventoryWeight.util.InventoryCheckUtil;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GetWeightCommand implements SubCommand {
    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "You must be a player to use this command"));
            return;
        }
        Player player = (Player) sender;

        if (args.length < 2) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.AIR) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        LanguageConfig.getConfig().getMessages().getItemWeight() + " " + 0));
            } else {
                double weight = InventoryCheckUtil.getItemWeight(item);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        LanguageConfig.getConfig().getMessages().getItemWeight() + " " + weight));
            }
        } else {
            String materialAllCaps = args[1].toUpperCase();
            if (Material.getMaterial(materialAllCaps) == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        LanguageConfig.getConfig().getMessages().getInvalidMaterial()));
                return;
            }

            double weight = InventoryCheckUtil.getItemWeight(new ItemStack(Material.getMaterial(materialAllCaps)));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    LanguageConfig.getConfig().getMessages().getItemWeight() + " " + weight));
        }
    }

}
