package me.wonka01.InventoryWeight.configuration;


import me.wonka01.InventoryWeight.InventoryWeight;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LanguageConfig {

    private static LanguageConfig config;
    private YamlConfiguration yamlConfiguration;
    private MessagesModel messages;


    public static LanguageConfig getConfig() {
        if (config == null) {
            config = new LanguageConfig();
        }
        return config;
    }

    private InventoryWeight plugin;
    private File configFile;

    public LanguageConfig() {
        plugin = InventoryWeight.getPlugin(InventoryWeight.class);
        configFile = new File(plugin.getDataFolder(), "messages.yml");

        configFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("messages.yml", false);
        }

        yamlConfiguration = new YamlConfiguration();
        try {
            yamlConfiguration.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MessagesModel getMessages()
    {
        return messages;
    }

    public void setUpLanguageConfig()
    {
        String noPermission = yamlConfiguration.getString("noPermission");
        String invalidCommand = yamlConfiguration.getString("invalidCommand");
        String invalidMaterial = yamlConfiguration.getString("invalidMaterial");
        String itemWeight = yamlConfiguration.getString("itemWeight");
        String weight = yamlConfiguration.getString("weight");
        String speed = yamlConfiguration.getString("speed");
        String reloadCommand = yamlConfiguration.getString("reloadCommand");
        String helpMessage = yamlConfiguration.getString("helpMessage");

        messages = new MessagesModel(noPermission, invalidCommand, invalidMaterial, itemWeight,
                        weight, speed, reloadCommand, helpMessage);
        config = this;
    }
}