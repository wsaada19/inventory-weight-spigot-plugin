package me.wonka01.InventoryWeight.commands;

import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import me.wonka01.InventoryWeight.PlayerWeight;
import me.wonka01.InventoryWeight.WeightSingleton;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WeightCommand extends SubCommand  {

    @Override
    public void onCommand(Player player, String[] args) {
        PlayerWeight playerWeight = WeightSingleton.getPlayerWeightMap().get(player.getUniqueId());

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',LanguageConfig.getConfig().getMessages().getWeight() + ": " + ChatColor.GREEN + playerWeight.getWeight() + ChatColor.WHITE + " / " + ChatColor.RED + playerWeight.getMaxWeight()));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',LanguageConfig.getConfig().getMessages().getSpeed() + ": " + ChatColor.GREEN + playerWeight.getPercentage() + "%"));
        player.sendMessage(ChatColor.WHITE + "[" + playerWeight.getSpeedDisplay() + ChatColor.WHITE + "]");
    }

    @Override
    public String name() {
        return "weight";
    }

    @Override
    public String info() {
        return "/iw weight";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
