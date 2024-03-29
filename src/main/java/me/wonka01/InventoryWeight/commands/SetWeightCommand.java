package me.wonka01.InventoryWeight.commands;

import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import me.wonka01.InventoryWeight.util.InventoryCheckUtil;
import me.wonka01.InventoryWeight.util.MaterialUtil;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWeightCommand implements SubCommand {

    public void onCommand(Player player, String[] args) {

        if (!player.hasPermission("inventoryweight.set")) {
            player.sendMessage(LanguageConfig.getConfig().getMessages().getNoPermission());
            return;
        }

        if (args.length < 3 || !args[2].matches("\\d+(\\.\\d{1,2})?")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    LanguageConfig.getConfig().getMessages().getInvalidCommand()));
            return;
        }

        if (!MaterialUtil.isMaterialValid(args[1])) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    LanguageConfig.getConfig().getMessages().getInvalidMaterial()));
            return;
        }
        String materialAllCaps = args[1].toUpperCase();

        double weight = Double.parseDouble(args[2]);

        InventoryCheckUtil.mapOfWeightsByMaterial.put(materialAllCaps, weight);
        player.sendMessage(ChatColor.GREEN + "Set the weight of " + materialAllCaps + " to " + weight);
    }

    public void onCommand(CommandSender sender, String[] args) {
        throw new NotImplementedException();
    }

}
