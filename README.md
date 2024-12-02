# Inventory Weight Plugin Documentation

## Overview

The Inventory Weight plugin for Minecraft ([Spigot](https://www.spigotmc.org/) 1.12 - 1.21) enables server administrators to impose weight constraints on a player's inventory. This affects the player’s movement speed and can restrict movement entirely if the inventory weight exceeds a specified threshold. Check out the plugin on the [Spigot resource page](https://www.spigotmc.org/resources/inventory-weight-1-12-0-1-15.70929/) to learn more and download the plugin.

### Key Features

- Adjustable weight limits for inventory items.
- Customizable movement speed based on inventory weight.
- Option to disable player movement when the weight limit is exceeded.
- Supports custom weight for specific items, item types, and lore tags.
- Configurable update intervals and world-specific behavior.
- Item limits to control the quantity of specific items.

---

1. [Configuration](#Configuration)
2. [Commands](#Commands)
3. [Permissions](#Permissions)
4. [Placeholders](#Placeholders)

## Configuration

The plugin configuration is managed through a YAML file called config.yml. Below is a breakdown of the available options.

### 1. **Weight Limit Configuration**

```yaml
weightLimit: 500
disableMovement: true
```

- **`weightLimit`**: Sets the maximum weight a player can carry before their speed will no longer decrease. This allows for a broader range of speed variance as the weight limit increases. Default is `500`.
- **`disableMovement`**: If `true`, once the player reaches or exceeds the weight limit, they will be unable to move until they reduce their weight. Default is `true`.
- **`blindAtMax: true`**: If `true`, the player will be blinded when they reach the weight limit. Default is `false`. This can be beneficial because player's can not sprint when they're blinded. Which prevents the player from "jump sprinting" which can bypass the speed reduction.

### 2. **Player Movement Speed**

```yaml
maxWalkSpeed: 0.30
minWalkSpeed: 0.05
beginSlowdown: 0.0
```

- **`maxWalkSpeed`**: Defines the player's walking speed when their inventory is empty or under the `beginSlowdown` threshold. Default is `0.30`.
- **`minWalkSpeed`**: Defines the player's walking speed once the inventory weight reaches or exceeds the `weightLimit`. Default is `0.05`.
- **`beginSlowdown`**: Sets the percentage of the weight limit at which the player’s speed will begin to decrease. For example, if set to `0.5`, speed reduction will start when the player reaches 50% of the weight limit. Default is `0.0`, meaning slowdown begins immediately.

### 3. **Update Frequency**

```yaml
checkInventoryTimer: 2
```

- **`checkInventoryTimer`**: Specifies the time in seconds for how frequently the plugin will recalculate the player’s inventory weight. Default is `2` seconds.

### 4. **World-Specific Behavior**

```yaml
worlds: []
```

- **`worlds`**: List of world names where the plugin should be active. Leave empty to apply to all worlds. Example:
  ```yaml
  worlds:
    - world
    - nether
  ```

### 5. **Armor-Only Weight Calculation**

```yaml
armorOnly: false
```

- **`armorOnly`**: When set to `true`, only the armor slots will contribute to the player’s total inventory weight. Default is `false`.

### 6. **Item Weights and Custom Weights**

When defining item weights, you can set custom weights for specific materials, custom item names, and lore tags. When there are conflicts the order of importance is as follows: lore tags, custom item name, material then default weight.

#### Default Item Weight

```yaml
defaultWeight: 1
```

- **`defaultWeight`**: This sets the default weight applied to any item that is not specifically listed in the configuration. Default is `1`.

#### Material Weights

```yaml
materialWeights:
  - material: IRON_PICKAXE
    weight: 5
  - material: IRON_SWORD
    weight: 10
```

- **`materialWeights`**: Define custom weights for specific materials. A full list of available materials can be found [here](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html).

#### Custom Item Weights by Name

```yaml
customItemWeights:
  - name: Diamond Sword of Doom
    weight: 20
```

- **`customItemWeights`**: Custom item names can have specific weights, even if they share the same material type as other items.

### 7. **Lore Tags for Custom Weight**

```yaml
loreTag: 'Weight:'
capacityTag: 'Capacity:'
```

- **`loreTag`**: Add a custom lore line to any item with the format `Weight: X` where `X` is the weight value. The plugin will recognize this lore and use it to set the item’s weight.
- **`capacityTag`**: Custom lore tags can also be used to indicate an item’s carrying capacity.

### 8. **Item Limits**

```yaml
itemLimits:
  - material: SHULKER_BOX
    limit: 5
```

- **`itemLimits`**: Limits the number of specific items a player can carry. For example, a player can carry no more than `5` shulker boxes. If they exceed this limit, they will be treated as if they’ve exceeded the max weight. This format checks if materials have the same name, so GREEN_SHULKER_BOX will be included in the count.

---

## Commands

The plugin offers a set of commands to interact with the inventory weight system. Below is a list of available commands and their usage:

| Command                       | Description                                                                                                                                                                                                                                        |
| ----------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`/iw`**                     | Returns a help message, which can be configured in the `messages.yml` file.                                                                                                                                                                        |
| **`/iw help`**                | Same as `/iw`, returns the help message.                                                                                                                                                                                                           |
| **`/iw weight`**              | Displays your current weight and movement speed. This command will not work if: <ul><li>The user has the `inventoryweight.off` permission.</li><li>The user is in creative mode.</li><li>Weight is disabled in the world the user is in.</li></ul> |
| **`/iw get`**                 | Returns the weight of the item in your main hand.                                                                                                                                                                                                  |
| **`/iw get [Material Name]`** | Displays the default weight for the specified material. For example, `/iw get IRON_SWORD` will return the default weight for an iron sword.                                                                                                        |
| **`/iw reload`**              | Reloads the `config.yml` file. This allows administrators to make changes to the configuration without restarting the server.                                                                                                                      |

---

## Permissions

- **`inventoryweight.reload`**: Grants permission for a player to reload the `config.yml`.
- **`inventoryweight.off`**: The player with this permission will not be affected by the Inventory Weight plugin.
- **`inventoryweight.maxweight.[maxweight value]`**: Allows the player’s maximum weight to be set to the specified value. For example, if a player has `inventoryweight.maxweight.100`, their max weight will be `100`, overriding the default weight limit.

---

## Placeholders

The following placeholders can be used with plugins that support PlaceholderAPI:

- **`%inventoryweight_weight%`**: Displays the player’s current inventory weight.
- **`%inventoryweight_maxweight%`**: Displays the maximum weight limit for the player.
- **`%inventoryweight_speed%`**: Displays the player’s current movement speed.
- **`%inventoryweight_inventorybar%`**: Displays a visual bar representing the player's inventory weight.
- **`%inventoryweight_weightPercentage%`**: Displays the percentage of the player’s current weight relative to their maximum weight.

---

## Example Use Cases

1. **Survival Servers**: Add realism to survival gameplay by limiting how much players can carry, encouraging strategic inventory management.
2. **Role-Playing Servers**: Use the plugin to simulate encumbrance mechanics, adding depth to role-playing scenarios.
3. **Challenge Servers**: Create unique challenges where players must manage their inventories carefully to avoid being slowed down or immobilized.

---

## Conclusion

The Inventory Weight plugin provides a flexible system for managing player inventory weight and speed, with highly customizable options for materials, custom items, and world-specific configurations. Whether used for immersion, balance, or challenge, it enhances gameplay by adding depth to the inventory system.

For more information or to download the plugin, visit the [plugin page](https://www.spigotmc.org/resources/inventory-weight-1-12-1-21.70929/).

---
