package me.wonka01.InventoryWeight.events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

public class FreezePlayerEvent implements Listener {
    private static Set<UUID> playersOverWeightLimit = new HashSet<UUID>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (playersOverWeightLimit.contains(e.getPlayer().getUniqueId())) {
            if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getTo().getZ()) {
                Location loc = e.getFrom();
                e.getPlayer().teleport(loc.setDirection(e.getTo().getDirection()));
            }
        }
    }

    public static void freezePlayer(UUID id) {
        playersOverWeightLimit.add(id);
    }

    public static void unfreezePlayer(UUID id) {
        playersOverWeightLimit.remove(id);
    }
}
