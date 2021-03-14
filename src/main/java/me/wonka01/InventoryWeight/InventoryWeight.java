package me.wonka01.InventoryWeight;

import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import me.wonka01.InventoryWeight.commands.InventoryWeightCommands;
import me.wonka01.InventoryWeight.events.AddItemEvent;
import me.wonka01.InventoryWeight.events.FreezePlayerEvent;
import me.wonka01.InventoryWeight.events.JoinEvent;
import me.wonka01.InventoryWeight.events.RemoveItemEvent;
import me.wonka01.InventoryWeight.playerweight.PlayerWeight;
import me.wonka01.InventoryWeight.playerweight.PlayerWeightMap;
import me.wonka01.InventoryWeight.util.InventoryCheckUtil;
import me.wonka01.InventoryWeight.util.InventoryWeightExpansion;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public class InventoryWeight extends JavaPlugin {

    private InventoryWeightCommands commands;
    private LanguageConfig languageConfig;

    public boolean showWeightChange;

    @Override
    public void onEnable(){
        getLogger().info("onEnable is called!");
        registerEvents();

        commands = new InventoryWeightCommands();
        commands.setup();

        loadConfig();
        initConfig();
        setUpMessageConfig();

        BukkitScheduler scheduler = getServer().getScheduler();
        int timer = getConfig().getInt("checkInventoryTime");
        if(timer < 1){
            timer = 3;
        }

        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                HashMap<UUID, PlayerWeight> mapReference = PlayerWeightMap.getPlayerWeightMap();
                Iterator hmIterator = mapReference.entrySet().iterator();

                while(hmIterator.hasNext()){
                    Map.Entry element = (Map.Entry)hmIterator.next();
                    UUID playerId = (UUID) element.getKey();
                    PlayerWeight playerWeight = (PlayerWeight)element.getValue();
                    Server server = getServer();
                    if(server.getPlayer(playerId) != null ){
                        Inventory inv = server.getPlayer(playerId).getInventory();
                        if(inv != null){
                            playerWeight.setWeight(InventoryCheckUtil.getInventoryWeight(inv.getContents()));
                        }
                    }
                }
            }
        },0L, (timer * 20));

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new InventoryWeightExpansion().register();
        }
    }

    @Override
    public void onDisable(){
        getLogger().info("onDisable is called!");
        //Fired when server disables this plugin
        saveConfiguration();
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void initConfig(){

        boolean disableMovement = getConfig().getBoolean("disableMovement");
        int capacity = getConfig().getInt("weightLimit");

        float minWeight = (float)getConfig().getDouble("minWalkSpeed");
        float maxWeight = (float)getConfig().getDouble("maxWalkSpeed");

        List<?> matWeights = getConfig().getList("materialWeights");
        List<?> nameWeights = getConfig().getList("customItemWeights");
        List<?> loreWeights = getConfig().getList("customLoreWeights");

        InventoryCheckUtil.defaultWeight = getConfig().getDouble("defaultWeight");

        for(Object item: matWeights){
            LinkedHashMap<?, ?> map = (LinkedHashMap)item;
            double weight = getDoubleFromConfigValue(map.get("weight"));
            String upperCaseMaterial = ((String)map.get("material")).toUpperCase();
            InventoryCheckUtil.mapOfWeightsByMaterial.put(upperCaseMaterial,  weight);
        }

        for(Object item: nameWeights){
            LinkedHashMap<?, ?> map = (LinkedHashMap)item;
            double weight = getDoubleFromConfigValue(map.get("weight"));
            String itemName = (String)map.get("name");

            InventoryCheckUtil.mapOfWeightsByDisplayName.put(itemName, weight);
        }

        for(Object item: loreWeights){
            LinkedHashMap<?, ?> map = (LinkedHashMap)item;
            double weight = getDoubleFromConfigValue(map.get("weight"));
            String itemName = (String)map.get("name");

            InventoryCheckUtil.mapOfWeightsByLore.put(itemName, weight);
        }

        PlayerWeight.initialize(disableMovement, capacity, minWeight, maxWeight);
    }

    private double getDoubleFromConfigValue(Object weight)
    {
        if(weight instanceof Double)
        {
            return (Double)weight;
        } else {
            int tempInt = (Integer)weight;
            return (double)tempInt;
        }
    }

    private void saveConfiguration()
    {
        HashMap<String, Double> weights = InventoryCheckUtil.mapOfWeightsByMaterial;
        Iterator hmIterator = weights.entrySet().iterator();
        List<LinkedHashMap<String, Object>> weightsToSave = new ArrayList<LinkedHashMap<String, Object>>();

        while(hmIterator.hasNext()){
            Map.Entry element = (Map.Entry)hmIterator.next();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("material", element.getKey());
            map.put("weight", element.getValue());
            weightsToSave.add(map);
        }

        getConfig().set("materialWeights", weightsToSave);
        saveConfig();
    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new AddItemEvent(), this);
        getServer().getPluginManager().registerEvents(new RemoveItemEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new FreezePlayerEvent(), this);
    }

    private void setUpMessageConfig(){
        showWeightChange = getConfig().getBoolean("showWeightChange");
        languageConfig = new LanguageConfig();
        languageConfig.setUpLanguageConfig();
    }

    public void reloadConfiguration(){
        reloadConfig();
        initConfig();
    }

}
