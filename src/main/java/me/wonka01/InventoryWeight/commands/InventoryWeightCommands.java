package me.wonka01.InventoryWeight.commands;

import me.wonka01.InventoryWeight.InventoryWeight;
import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class InventoryWeightCommands implements CommandExecutor {

    private InventoryWeight plugin = JavaPlugin.getPlugin(InventoryWeight.class);
    private HashMap<String, SubCommand> subCommands;
    private final String main = "inventoryweight";

    public void setup() {

        plugin.getCommand(main).setExecutor(this);
        subCommands = new HashMap<String, SubCommand>();
        subCommands.put("weight", new WeightCommand());
        subCommands.put("help", new HelpCommand());
        subCommands.put("get", new GetWeightCommand());
        subCommands.put("reload", new ReloadCommand());
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getHelpMessage()));
            return true;
        }
        String sub = args[0];

        if (subCommands.containsKey(sub)) {
            subCommands.get(sub).onCommand(player, args);
            return true;
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', LanguageConfig.getConfig().getMessages().getInvalidCommand()));
            return true;
        }
    }
}
