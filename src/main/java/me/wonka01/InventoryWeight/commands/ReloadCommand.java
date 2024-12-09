package me.wonka01.InventoryWeight.commands;

import me.wonka01.InventoryWeight.InventoryWeight;
import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCommand implements SubCommand {

    public void onCommand(CommandSender sender, String[] args) {

        if (!sender.hasPermission("inventoryweight.reload") && !(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    LanguageConfig.getConfig().getMessages().getNoPermission()));
            return;
        }

        JavaPlugin.getPlugin(InventoryWeight.class).reloadConfiguration();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                LanguageConfig.getConfig().getMessages().getReloadCommand()));

    }
}
