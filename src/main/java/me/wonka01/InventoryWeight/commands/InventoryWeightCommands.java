package me.wonka01.InventoryWeight.commands;

import me.wonka01.InventoryWeight.InventoryWeight;
import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryWeightCommands implements CommandExecutor, TabCompleter {

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
        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    LanguageConfig.getConfig().getMessages().getHelpMessage()));
            return true;
        }
        String sub = args[0];

        if (subCommands.containsKey(sub)) {
            subCommands.get(sub).onCommand(sender, args);
            return true;
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    LanguageConfig.getConfig().getMessages().getInvalidCommand()));
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (String sub : subCommands.keySet()) {
                if (sub.startsWith(args[0].toLowerCase())) {
                    completions.add(sub);
                }
            }
        }
        return completions;
    }
}
