package me.wonka01.InventoryWeight.commands;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    void onCommand(CommandSender player, String[] args);
}
