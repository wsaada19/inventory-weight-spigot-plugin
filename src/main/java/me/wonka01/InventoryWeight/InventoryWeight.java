package me.wonka01.InventoryWeight;

import me.wonka01.InventoryWeight.commands.InventoryWeightCommands;
import me.wonka01.InventoryWeight.configuration.LanguageConfig;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public class InventoryWeight extends JavaPlugin {

    private InventoryWeightCommands commands;
    private LanguageConfig languageConfig;

    public boolean showWeightChange;

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
            @Override
            public void run() {
                HashMap<UUID, PlayerWeight> mapReference = PlayerWeightMap.getPlayerWeightMap();
                Iterator hmIterator = mapReference.entrySet().iterator();

                while (hmIterator.hasNext()) {
                    Map.Entry element = (Map.Entry) hmIterator.next();
                    UUID playerId = (UUID) element.getKey();
                    PlayerWeight playerWeight = (PlayerWeight) element.getValue();
                    Server server = getServer();
                    if (server.getPlayer(playerId) != null) {
                        Inventory inv = server.getPlayer(playerId).getInventory();
                        if (inv != null) {
                            playerWeight.setWeight(InventoryCheckUtil.getInventoryWeight(inv.getContents()));
                        }
                    }
                }
            }
        }, 0L, (timer * 20));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new InventoryWeightExpansion().register();
        }

        // add all players to map
        for(Player player : this.getServer().getOnlinePlayers()) {
            if (player.hasPermission("inventoryweight.off")) {
                return;
            }

            Set<PermissionAttachmentInfo> set = player.getEffectivePermissions();
            Iterator iterator = set.iterator();

            double inventoryWeight = InventoryCheckUtil.getInventoryWeight(player.getInventory().getContents());

            PlayerWeight playerData = new PlayerWeight(inventoryWeight, player.getUniqueId());

            while (iterator.hasNext()) {
                PermissionAttachmentInfo info = (PermissionAttachmentInfo) iterator.next();

                if (info.getPermission().contains("inventoryweight.maxweight.")) {
                    String amount = info.getPermission().substring(26);
                    playerData.setMaxWeight(Integer.parseInt(amount));
                }
            }

            PlayerWeightMap.getPlayerWeightMap().put(player.getUniqueId(), playerData);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
        //Fired when server disables this plugin
    }

    private void initConfig() {

        boolean disableMovement = getConfig().getBoolean("disableMovement");
        int capacity = getConfig().getInt("weightLimit");
        showWeightChange = getConfig().getBoolean("showWeightChange");

        float minWeight = (float) getConfig().getDouble("minWalkSpeed");
        float maxWeight = (float) getConfig().getDouble("maxWalkSpeed");

        List<?> matWeights = getConfig().getList("materialWeights");
        List<?> nameWeights = getConfig().getList("customItemWeights");
        List<?> loreWeights = getConfig().getList("customLoreWeights");

        InventoryCheckUtil.defaultWeight = getConfig().getDouble("defaultWeight");
        if(matWeights != null) {
            for (Object item : matWeights) {
                LinkedHashMap<?, ?> map = (LinkedHashMap) item;
                double weight = getDoubleFromConfigValue(map.get("weight"));
                String upperCaseMaterial = ((String) map.get("material")).toUpperCase();
                InventoryCheckUtil.mapOfWeightsByMaterial.put(upperCaseMaterial, weight);
            }
        }

        if(nameWeights != null) {
            for (Object item : nameWeights) {
                LinkedHashMap<?, ?> map = (LinkedHashMap) item;
                double weight = getDoubleFromConfigValue(map.get("weight"));
                String itemName = (String) map.get("name");

                InventoryCheckUtil.mapOfWeightsByDisplayName.put(itemName, weight);
            }
        }

        if(loreWeights != null) {
            for (Object item : loreWeights) {
                LinkedHashMap<?, ?> map = (LinkedHashMap) item;
                double weight = getDoubleFromConfigValue(map.get("weight"));
                String itemName = (String) map.get("name");

                InventoryCheckUtil.mapOfWeightsByLore.put(itemName, weight);
            }
        }

        InventoryCheckUtil.loreTag = getConfig().getString("loreTag");
        PlayerWeight.initialize(disableMovement, capacity, minWeight, maxWeight);
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
        getServer().getPluginManager().registerEvents(new AddItemEvent(), this);
        getServer().getPluginManager().registerEvents(new RemoveItemEvent(), this);
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
