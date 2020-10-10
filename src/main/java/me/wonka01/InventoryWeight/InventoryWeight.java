package me.wonka01.InventoryWeight;

import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import me.wonka01.InventoryWeight.commands.InventoryWeightCommands;
import me.wonka01.InventoryWeight.events.AddItemEvent;
import me.wonka01.InventoryWeight.events.InventoryCloseEvent;
import me.wonka01.InventoryWeight.events.JoinEvent;
import me.wonka01.InventoryWeight.events.RemoveItemEvent;
import me.wonka01.InventoryWeight.util.InventoryWeightExpansion;
import org.bukkit.Bukkit;
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
        initializeLayers();
        setUpMessageConfig();

        BukkitScheduler scheduler = getServer().getScheduler();
        int timer = getConfig().getInt("checkInventoryTime");
        if(timer < 1){
            timer = 3;
        }

        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                HashMap<UUID, PlayerWeight> mapReference = WeightSingleton.getPlayerWeightMap();
                Iterator hmIterator = mapReference.entrySet().iterator();

                while(hmIterator.hasNext()){
                    Map.Entry element = (Map.Entry)hmIterator.next();
                    UUID playerId = (UUID) element.getKey();
                    PlayerWeight playerWeight = (PlayerWeight)element.getValue();
                    playerWeight.setWeight(InventoryCheckUtil.getInventoryWeight(getServer().getPlayer(playerId).getInventory().getContents()));

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

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public void initializeLayers(){

        boolean disableMovement = getConfig().getBoolean("disableMovement");
        boolean disableJump = getConfig().getBoolean("disableJump");
        int capacity = getConfig().getInt("weightLimit");
        int jumpLimit = getConfig().getInt("jumpLimit");

        float minWeight = (float)getConfig().getDouble("minWalkSpeed");
        float maxWeight = (float)getConfig().getDouble("maxWalkSpeed");

        List<?> matWeights = getConfig().getList("materialWeights");
        List<?> nameWeights = getConfig().getList("customItemWeights");

        InventoryCheckUtil.defaultWeight = getConfig().getDouble("defaultWeight");

        for(Object item: matWeights){
            LinkedHashMap<?, ?> map = (LinkedHashMap)item;
            double weight = getDoubleFromConfigValue(map.get("weight"));
            String upperCaseMaterial = ((String)map.get("material")).toUpperCase();
            InventoryCheckUtil.mapOfWeights.put(upperCaseMaterial,  weight);
        }

        for(Object item: nameWeights){
            LinkedHashMap<?, ?> map = (LinkedHashMap)item;
            double weight = getDoubleFromConfigValue(map.get("weight"));
            String itemName = (String)map.get("name");

            InventoryCheckUtil.mapOfWeightsByDisplayName.put(itemName, weight);
        }

        PlayerWeight.initialize(disableMovement, capacity, minWeight, maxWeight, disableJump, jumpLimit);
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
        HashMap<String, Double> weights = InventoryCheckUtil.mapOfWeights;
        Iterator hmIterator = weights.entrySet().iterator();
        List<LinkedHashMap<String, Object>> weightsToSave = new ArrayList<LinkedHashMap<String, Object>>();

        while(hmIterator.hasNext()){
            Map.Entry element = (Map.Entry)hmIterator.next();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("material", element.getKey());
            map.put("weight", element.getValue());
            weightsToSave.add(map);
        }

        HashMap<String, Double> namedWeights = InventoryCheckUtil.mapOfWeightsByDisplayName;
        Iterator nwIterator = namedWeights.entrySet().iterator();
        List<LinkedHashMap<String, Object>> namedWeightsToSave = new ArrayList<LinkedHashMap<String, Object>>();

        while(nwIterator.hasNext()){
            Map.Entry element = (Map.Entry)nwIterator.next();
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("name", element.getKey());
            map.put("weight", element.getValue());
            namedWeightsToSave.add(map);
        }

        getConfig().set("customItemWeights", namedWeightsToSave);
        saveConfig();
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new AddItemEvent(), this);
        getServer().getPluginManager().registerEvents(new RemoveItemEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseEvent(), this);
    }

    public void setUpMessageConfig(){
        showWeightChange = getConfig().getBoolean("showWeightChange");

        languageConfig = new LanguageConfig();
        languageConfig.setUpLanguageConfig();
    }

    public void reloadConfiguration(){
        reloadConfig();
        initializeLayers();
    }

}
