package me.wonka01.InventoryWeight.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public interface SubCommand {
    void onCommand(Player player, String[] args);

    void onCommand(CommandSender player, String[] args);
}
