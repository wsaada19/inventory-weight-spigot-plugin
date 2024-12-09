package me.wonka01.InventoryWeight.commands;

import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import me.wonka01.InventoryWeight.util.InventoryCheckUtil;
import me.wonka01.InventoryWeight.util.MaterialUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class SetWeightCommand implements SubCommand {

    public void onCommand(CommandSender sender, String[] args) {

        if (!sender.hasPermission("inventoryweight.set") && !(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(LanguageConfig.getConfig().getMessages().getNoPermission());
            return;
        }

        if (args.length < 3 || !args[2].matches("\\d+(\\.\\d{1,2})?")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    LanguageConfig.getConfig().getMessages().getInvalidCommand()));
            return;
        }

        if (!MaterialUtil.isMaterialValid(args[1])) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    LanguageConfig.getConfig().getMessages().getInvalidMaterial()));
            return;
        }
        String materialAllCaps = args[1].toUpperCase();

        double weight = Double.parseDouble(args[2]);

        InventoryCheckUtil.mapOfWeightsByMaterial.put(materialAllCaps, weight);
        sender.sendMessage(ChatColor.GREEN + "Set the weight of " + materialAllCaps + " to " + weight);
    }
}
