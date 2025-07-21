# RogueSword

A **rogue sword** is a casting utility that is used to get temporary speed effect.

| Statistics ||
| - | - |
| Melee Damage | 4 |
| Mana Consumption| 1 |
| Rarity | Uncommon |

## Usage

### Melee Attack

Same as a [golden sword](https://minecraft.wiki/w/Golden_Sword).

### Speed Effect

Pressing use while holding a rogue sword in main hand give the player speed effect with amplifier 1 for 30 seconds and consumes 1 mana.

#### Mana Consumption with Ultilization

| Base Mana Consumption | Utilization I | Utilization II | Utilization III | Utilization IV | Utilization V |
| :-: | :-: | :-: | :-: | :-: | :-: |
| 1.0 | 0.9 | 0.8 | 0.7 | 0.6 | 0.5 |

## Data Powered

An item where `minecraft:custom_data.id` is "rogue_sword:rogue_sword" is considered as an rogue sword.

### Give Command

```mcfunction
/give @s minecraft:golden_sword[custom_data={id:"rogue_sword:rogue_sword"},item_name={text:"Rogue Sword"},rarity="uncommon"]
```

### Loot Table Entry

```json
{
    "type": "minecraft:item",
    "functions": [
        {
            "function": "minecraft:set_components",
            "components": {
            "minecraft:custom_data": {
                "id": "rogue_sword:rogue_sword"
            },
            "minecraft:rarity": "uncommon"
            }
        },
        {
            "function": "minecraft:set_name",
            "name": {
                "text": "Rogue Sword"
            },
            "target": "item_name"
        }
    ],
    "name": "minecraft:golden_sword"
}
```

## Configuration

Below is a template config file `config/rogue-sword.json` filled with default values. You may only need to write the lines you would like to modify.

```json
{
  "manaConsumption": 1.0,
  "speedDuration": 600,
  "speedAmplifier": 0,
  "speedAmbient": false,
  "speedShowParticles": true,
  "speedShowIcon": true
}
```
