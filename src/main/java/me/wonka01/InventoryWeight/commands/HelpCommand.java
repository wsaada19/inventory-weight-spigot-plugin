package me.wonka01.InventoryWeight.commands;

import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements SubCommand {
    public void onCommand(Player player, String[] args) {
        player.sendMessage(
                ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getHelpMessage()));
    }

    public void onCommand(CommandSender sender, String[] args) {
        throw new NotImplementedException();
    }

}
