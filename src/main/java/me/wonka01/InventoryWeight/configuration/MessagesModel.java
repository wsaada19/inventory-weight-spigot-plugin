package me.wonka01.InventoryWeight.configuration;

public class MessagesModel {

    private String noPermission;
    private String invalidCommand;
    private String invalidMaterial;
    private String itemWeight;
    private String weight;
    private String speed;
    private String reloadCommand;
    private String helpMessage;


    public MessagesModel(String noPermission, String invalidCommand, String invalidMaterial, String itemWeight,
                         String weight, String speed, String reloadCommand, String helpMessage)
    {
        this.noPermission = noPermission;
        this.invalidCommand = invalidCommand;
        this.invalidMaterial = invalidMaterial;
        this.itemWeight = itemWeight;
        this.weight = weight;
        this.speed = speed;
        this.reloadCommand = reloadCommand;
        this.helpMessage = helpMessage;

    }

    public String getNoPermission() {
        return noPermission;
    }

    public String getInvalidCommand() {
        return invalidCommand;
    }

    public String getInvalidMaterial() {
        return invalidMaterial;
    }

    public String getItemWeight() {
        return itemWeight;
    }

    public String getWeight() {
        return weight;
    }

    public String getSpeed() {
        return speed;
    }

    public String getReloadCommand() {
        return reloadCommand;
    }

    public String getHelpMessage() {return helpMessage;}
}