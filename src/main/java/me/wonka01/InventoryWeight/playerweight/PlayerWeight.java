package me.wonka01.InventoryWeight.playerweight;

import me.wonka01.InventoryWeight.events.FreezePlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerWeight {

    private static boolean disableMovement;
    public static int defaultMaxCapacity;
    private static float minSpeed;
    private static float maxSpeed;

    private double weight;
    private UUID playerId;
    private int maxCapacity;
    private boolean isDisableMovement;

    public PlayerWeight(double weight, UUID id){
        this.weight = weight;
        this.playerId = id;
        maxCapacity = defaultMaxCapacity;
        isDisableMovement = false;
        changeSpeed();
    }

    public double getWeight(){return weight;}

    public void setMaxWeight(int max) {
        maxCapacity = max;
        changeSpeed();
    }
    public int getMaxWeight()
    {
        return maxCapacity;
    }

    public void setWeight(double newWeight){
        weight = newWeight;
        changeSpeed();
    }

    private void changeSpeed()
    {
        Player player = Bukkit.getServer().getPlayer(playerId);

        if(weight > maxCapacity){
            handleMaxCapacity(player);
            return;
        } else {
            if(isDisableMovement) {
                FreezePlayerEvent.unfreezePlayer(playerId);
            }
        }

        float weightRatio = (float)weight / (float)maxCapacity;

        float weightFloat = maxSpeed - (weightRatio * (maxSpeed - minSpeed));

        if(weight == 0) {
            weightFloat = maxSpeed;
        }
        player.setWalkSpeed(weightFloat);
    }

    private void handleMaxCapacity(Player player)
    {
        if(disableMovement && !isDisableMovement){
            player.sendMessage(ChatColor.RED + "You can't carry your weight anymore, you're going to need to drop some items!");
            FreezePlayerEvent.freezePlayer(playerId);
            isDisableMovement = true;
        } else {
            player.setWalkSpeed(minSpeed);
        }
    }

    public String getSpeedDisplay(){
        StringBuilder speedDisplay = new StringBuilder();
        double ratio =  weight / (double) maxCapacity;
        int result = 20 - (int)(ratio * 20.0);
        for(int i = 0; i < 20; i++){
            if(i < result){
                speedDisplay.append(ChatColor.GREEN );
                speedDisplay.append("|");
            } else {
                speedDisplay.append(ChatColor.RED );
                speedDisplay.append("|");
            }
        }
        return speedDisplay.toString();
    }

    public int getPercentage(){
        double ratio =  weight / (double) maxCapacity;
        int finalRatio =  (100 - (int)(ratio * 100.0));
        if(finalRatio < 0) {
            return 0;
        } else {
            return finalRatio;
        }
    }

    public static void initialize( boolean disableMovement, int capacity, float min, float max){
        PlayerWeight.disableMovement = disableMovement;
        defaultMaxCapacity = capacity;
        minSpeed = min;
        maxSpeed = max;
    }
}
