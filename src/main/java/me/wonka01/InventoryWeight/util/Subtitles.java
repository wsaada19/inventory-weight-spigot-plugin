package me.wonka01.InventoryWeight.util;

import org.bukkit.entity.Player;

public class Subtitles {

    public void showSubtitle(Player player, String title, String subtitle) {
        int fadeIn = 10; // The duration of the fade-in effect (in ticks)
        int stay = 40; // How long the subtitle stays on screen (in ticks)
        int fadeOut = 20; // The duration of the fade-out effect (in ticks)

        // Send the title with the subtitle to the player
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }
}
