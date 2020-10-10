package me.wonka01.InventoryWeight;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerWeight {

    private static boolean disableMovement;
    public static int defaultMaxCapacity;
    private static float minSpeed;
    private static float maxSpeed;
    private static boolean disableJump;
    private static int jumpLimit;

    private double weight;
    private UUID playerId;
    private int maxCapacity;

    public PlayerWeight(double weight, UUID id){
        this.weight = weight;
        this.playerId = id;
        maxCapacity = defaultMaxCapacity;

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
        if(player.hasPermission("inventoryweight.off")) {
            return;
        }

        if(weight > maxCapacity){
            handleMaxCapacity(player);
            return;
        }

        if(disableJump && weight > jumpLimit) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 250));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 250));
        } else {
            player.removePotionEffect(PotionEffectType.JUMP);
            player.removePotionEffect(PotionEffectType.SLOW);
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
        if(disableMovement){
            player.setWalkSpeed(0);
        } else {
            player.setWalkSpeed(minSpeed);
        }

        if(disableJump) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 250));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 250));

        }
    }

    public String getSpeedDisplay(){
        String speedDisplay = "";
        double ratio =  weight / (double) maxCapacity;
        int result = 20 - (int)(ratio * 20.0);
        for(int i = 0; i < 20; i++){
            if(i < result){
                speedDisplay += ChatColor.GREEN + "|";
            } else {
                speedDisplay += ChatColor.RED + "|";
            }
        }
        return speedDisplay;

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

    public static void initialize( boolean disableMovement, int capacity,
                                   float min, float max, boolean disableJump, int jumpLimit){
        PlayerWeight.disableMovement = disableMovement;
        defaultMaxCapacity = capacity;
        minSpeed = min;
        maxSpeed = max;
        PlayerWeight.disableJump = disableJump;
        PlayerWeight.jumpLimit = jumpLimit;
    }
}
