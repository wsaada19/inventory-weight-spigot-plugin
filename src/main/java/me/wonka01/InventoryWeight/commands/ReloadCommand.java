package me.wonka01.InventoryWeight.commands;

import me.wonka01.InventoryWeight.InventoryWeight;
import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCommand implements SubCommand {

    public void onCommand(Player player, String[] args) {

        if (!player.hasPermission("inventoryweight.reload")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    LanguageConfig.getConfig().getMessages().getNoPermission()));
            return;
        }

        JavaPlugin.getPlugin(InventoryWeight.class).reloadConfiguration();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                LanguageConfig.getConfig().getMessages().getReloadCommand()));

    }

    public void onCommand(CommandSender sender, String[] args) {
        throw new NotImplementedException();
    }

}
