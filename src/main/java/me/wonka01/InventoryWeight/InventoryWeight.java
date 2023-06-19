package me.wonka01.InventoryWeight;

import me.wonka01.InventoryWeight.commands.InventoryWeightCommands;
import me.wonka01.InventoryWeight.configuration.LanguageConfig;
import me.wonka01.InventoryWeight.events.FreezePlayerEvent;
import me.wonka01.InventoryWeight.events.JoinEvent;
import me.wonka01.InventoryWeight.playerweight.PlayerWeight;
import me.wonka01.InventoryWeight.playerweight.PlayerWeightMap;
import me.wonka01.InventoryWeight.util.InventoryCheckUtil;
import me.wonka01.InventoryWeight.util.InventoryWeightExpansion;
import me.wonka01.InventoryWeight.util.WorldList;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;
import java.util.Map.Entry;

public class InventoryWeight extends JavaPlugin {

    public boolean showWeightChange;
    private InventoryWeightCommands commands;
    private LanguageConfig languageConfig;

    @Override
    public void onEnable() {
        getLogger().info("onEnable is called!");
        registerEvents();

        commands = new InventoryWeightCommands();
        commands.setup();

        saveDefaultConfig();
        initConfig();
        setUpMessageConfig();

        BukkitScheduler scheduler = getServer().getScheduler();
        int timer = getConfig().getInt("checkInventoryTime");
        if (timer < 1) {
            timer = 3;
        }

        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                HashMap<UUID, PlayerWeight> mapReference = PlayerWeightMap.getPlayerWeightMap();
                Iterator<Entry<UUID, PlayerWeight>> hmIterator = mapReference.entrySet().iterator();

                while (hmIterator.hasNext()) {
                    Entry<UUID, PlayerWeight> element = hmIterator.next();
                    UUID playerId = element.getKey();
                    PlayerWeight playerWeight = element.getValue();
                    Server server = getServer();
                    if (server.getPlayer(playerId) != null) {
                        Inventory inv = server.getPlayer(playerId).getInventory();
                        if (inv != null) {
                            Map<String, Double> weightMap = InventoryCheckUtil.getInventoryWeight(inv.getContents());
                            playerWeight.setWeight(weightMap.get("totalWeight"));
                            playerWeight.setIncreasedCapacity(weightMap.get("totalIncreasedCapacity"));
                        }
                    }
                }
            }
        }, 0L, (timer * 20));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new InventoryWeightExpansion().register();
        }

        // add all players to map
        for (Player player : this.getServer().getOnlinePlayers()) {
            JoinEvent.addPlayerToWeightMap(player);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

    private void initConfig() {

        boolean disableMovement = getConfig().getBoolean("disableMovement");
        int capacity = getConfig().getInt("weightLimit");
        showWeightChange = getConfig().getBoolean("showWeightChange");
        boolean armorOnly = getConfig().getBoolean("armorOnly");
        InventoryCheckUtil.armorOnlyMode = armorOnly;

        float minWeight = (float) getConfig().getDouble("minWalkSpeed");
        float maxWeight = (float) getConfig().getDouble("maxWalkSpeed");

        List<?> matWeights = getConfig().getList("materialWeights");
        List<?> nameWeights = getConfig().getList("customItemWeights");
        List<?> loreWeights = getConfig().getList("customLoreWeights");

        InventoryCheckUtil.defaultWeight = getConfig().getDouble("defaultWeight");
        if (matWeights != null) {
            for (Object item : matWeights) {
                LinkedHashMap<?, ?> map = (LinkedHashMap) item;
                double weight = getDoubleFromConfigValue(map.get("weight"));
                String upperCaseMaterial = ((String) map.get("material")).toUpperCase();
                InventoryCheckUtil.mapOfWeightsByMaterial.put(upperCaseMaterial, weight);
            }
        }

        if (nameWeights != null) {
            for (Object item : nameWeights) {
                LinkedHashMap<?, ?> map = (LinkedHashMap) item;
                double weight = getDoubleFromConfigValue(map.get("weight"));
                String itemName = (String) map.get("name");

                InventoryCheckUtil.mapOfWeightsByDisplayName.put(itemName, weight);
            }
        }

        if (loreWeights != null) {
            for (Object item : loreWeights) {
                LinkedHashMap<?, ?> map = (LinkedHashMap) item;
                double weight = getDoubleFromConfigValue(map.get("weight"));
                String itemName = (String) map.get("name");

                InventoryCheckUtil.mapOfWeightsByLore.put(itemName, weight);
            }
        }

        InventoryCheckUtil.loreTag = getConfig().getString("loreTag");
        InventoryCheckUtil.capacityTag = getConfig().getString("capacityTag");

        PlayerWeight.initialize(disableMovement, capacity, minWeight, maxWeight);

        List<String> worlds = getConfig().getStringList("worlds");
        WorldList.initializeWorldList(worlds);
    }

    private double getDoubleFromConfigValue(Object weight) {
        if (weight instanceof Double) {
            return (Double) weight;
        } else {
            int tempInt = (Integer) weight;
            return (double) tempInt;
        }
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new FreezePlayerEvent(), this);
    }

    private void setUpMessageConfig() {
        languageConfig = new LanguageConfig();
        languageConfig.setUpLanguageConfig();
    }

    public void reloadConfiguration() {
        reloadConfig();
        setUpMessageConfig();
        initConfig();
    }
}
