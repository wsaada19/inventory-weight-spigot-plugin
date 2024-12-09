package me.wonka01.InventoryWeight.commands;

import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements SubCommand {
    public void onCommand(CommandSender sender, String[] args) {
        sender.sendMessage(
                ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getHelpMessage()));
    }
}
