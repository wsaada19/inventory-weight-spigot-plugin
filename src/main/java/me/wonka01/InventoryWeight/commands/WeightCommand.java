package me.wonka01.InventoryWeight.commands;

import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import me.wonka01.InventoryWeight.playerweight.PlayerWeight;
import me.wonka01.InventoryWeight.playerweight.PlayerWeightMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WeightCommand extends SubCommand {

    @Override
    public void onCommand(Player player, String[] args) {

        if (player.hasPermission("inventoryweight.off")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getNoPermission()));
            return;
        }
        PlayerWeight playerWeight = PlayerWeightMap.getPlayerWeightMap().get(player.getUniqueId());

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getWeight() + ": &a" + playerWeight.getWeight() + " &f / &c" + playerWeight.getMaxWeight()));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getSpeed() + ": &a" + playerWeight.getPercentage() + "%"));
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
