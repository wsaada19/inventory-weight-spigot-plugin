package me.wonka01.InventoryWeight.playerweight;

import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import me.wonka01.InventoryWeight.events.FreezePlayerEvent;
import me.wonka01.InventoryWeight.util.WorldList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerWeight {

    public static int defaultMaxCapacity;
    private static boolean disableMovement;
    private static float minSpeed;
    private static float maxSpeed;

    private double weight;
    private double increasedCapacity;
    private UUID playerId;
    private int maxCapacity;
    private boolean isPlayerFrozen;

    public PlayerWeight(double weight, UUID id) {
        this.weight = weight;
        this.playerId = id;
        maxCapacity = defaultMaxCapacity;
        increasedCapacity = 0.0;
        isPlayerFrozen = false;
        changeSpeed();
    }

    public static void initialize(boolean disableMovement, int capacity, float min, float max) {
        PlayerWeight.disableMovement = disableMovement;
        defaultMaxCapacity = capacity;
        minSpeed = min;
        maxSpeed = max;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double newWeight) {
        weight = newWeight;
        changeSpeed();
    }

    public void setIncreasedCapacity(double capacity) {
        increasedCapacity = capacity;
        changeSpeed();
    }

    public double getMaxWeight() {
        return maxCapacity + increasedCapacity;
    }

    public void setMaxWeight(int max) {
        maxCapacity = max;
    }

    private void changeSpeed() {
        Player player = Bukkit.getPlayer(playerId);
        if (isPluginDisabledForUserOrWorld(player)) {
            player.setWalkSpeed(0.20f);
            return;
        }

        if (weight > this.getMaxWeight()) {
            handleMaxCapacity(player);
            return;
        } else {
            if (isPlayerFrozen) {
                FreezePlayerEvent.unfreezePlayer(playerId);
                isPlayerFrozen = false;
            }
        }

        float weightRatio = (float) weight / (float) this.getMaxWeight();

        float weightFloat = maxSpeed - (weightRatio * (maxSpeed - minSpeed));

        if (weight == 0) {
            weightFloat = maxSpeed;
        }
        player.setWalkSpeed(weightFloat);
    }

    private boolean isPluginDisabledForUserOrWorld(Player player) {
        WorldList worldList = WorldList.getInstance();
        if (player == null) {
            return true;
        } else if (player.hasPermission("inventoryweight.off")) {
            return true;
        } else if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return true;
        } else
            return !(worldList.isInventoryWeightEnabled(player.getWorld().getName()));
    }

    private void handleMaxCapacity(Player player) {
        if (disableMovement && !isPlayerFrozen) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    LanguageConfig.getConfig().getMessages().getCantMoveMessage()));
            FreezePlayerEvent.freezePlayer(playerId);
            isPlayerFrozen = true;
        } else {
            player.setWalkSpeed(minSpeed);
        }
    }

    public String getSpeedDisplay() {
        StringBuilder speedDisplay = new StringBuilder();
        double ratio = weight / this.getMaxWeight();
        int result = 20 - (int) (ratio * 20.0);
        for (int i = 0; i < 20; i++) {
            if (i < result) {
                speedDisplay.append(ChatColor.GREEN);
                speedDisplay.append("|");
            } else {
                speedDisplay.append(ChatColor.RED);
                speedDisplay.append("|");
            }
        }
        return speedDisplay.toString();
    }

    public int getPercentage() {
        double ratio = weight / this.getMaxWeight();
        int finalRatio = (100 - (int) (ratio * 100.0));
        if (finalRatio < 0) {
            return 0;
        } else {
            return finalRatio;
        }
    }
}
