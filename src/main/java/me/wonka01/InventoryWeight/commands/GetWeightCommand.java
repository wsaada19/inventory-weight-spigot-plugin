package me.wonka01.InventoryWeight.commands;

import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import me.wonka01.InventoryWeight.InventoryCheckUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GetWeightCommand extends SubCommand {
    @Override
    public void onCommand(Player player, String[] args) {

        if(args.length < 2) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getInvalidCommand()));
            return;
        }

        String materialAllCaps = args[1].toUpperCase();
        if(Material.getMaterial(materialAllCaps) == null){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getInvalidMaterial()));
            return;
        }

        double weight = InventoryCheckUtil.getItemWeight(args[1]);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',LanguageConfig.getConfig().getMessages().getItemWeight() + " " + weight));
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
