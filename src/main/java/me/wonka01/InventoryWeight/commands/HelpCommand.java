package me.wonka01.InventoryWeight.commands;

import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpCommand extends SubCommand {
    @Override
    public void onCommand(Player player, String[] args) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getHelpMessage()));
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public String info() {
        return "/iw help";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
