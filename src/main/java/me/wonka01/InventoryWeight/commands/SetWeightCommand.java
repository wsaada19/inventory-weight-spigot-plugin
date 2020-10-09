package me.wonka01.InventoryWeight.commands;

import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import me.wonka01.InventoryWeight.InventoryCheckUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;


public class SetWeightCommand extends SubCommand {

    @Override
    public void onCommand(Player player, String[] args) {

        if(!player.hasPermission("inventoryweight.set")){
            player.sendMessage(LanguageConfig.getConfig().getMessages().getNoPermission());
            return;
        }

        if(args.length < 3 || !args[2].matches("-?\\d+")){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getInvalidCommand()));
            return;
        }

        if(Material.getMaterial(args[1]) == null){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getNoPermission()));
            return;
        }

        int weight = Integer.parseInt(args[2]);
        if(InventoryCheckUtil.mapOfWeights.containsKey(args[1])){
            InventoryCheckUtil.mapOfWeights.remove(args[1]);
        }
        InventoryCheckUtil.mapOfWeights.put(args[1], weight);
        player.sendMessage(ChatColor.GREEN + "Set the weight of " + args[1] + " to " + weight);
    }

    @Override
    public String name() {
        return "set";
    }

    @Override
    public String info() {
        return "/iw set [Material Name] [weight]";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
