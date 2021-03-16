package me.wonka01.InventoryWeight.commands;

import me.wonka01.InventoryWeight.InventoryWeight;
import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCommand extends SubCommand {

    @Override
    public void onCommand(Player player, String[] args) {

        if (!player.hasPermission("inventoryweight.reload")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getNoPermission()));
            return;
        }

        JavaPlugin.getPlugin(InventoryWeight.class).reloadConfiguration();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getReloadCommand()));

    }

    @Override
    public String name() {
        return "reload";
    }

    @Override
    public String info() {
        return "/iw reload";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
