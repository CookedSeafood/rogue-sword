# RogueSword

Rogue Sword is a sword that gives a 30 second speed effect for 1 mana when right-clicked.

## The Item

An item where `minecraft:item_name` is "Rogue Sword" is considered as a rogue sword.

An example command that gives rogue sword to yourslef:

```mcfunction
/give @s golden_sword[item_name="Rogue Sword"]
```

Another example command for the original design:

```mcfunction
/give @s golden_sword[item_name="Rogue Sword",rarity=uncommon]
```

## Configuration

Below is a template config file `config/rogue-sword.json` filled with default values. You may only need to write the lines you would like to modify.

```json
{
  "manaConsumption": 1,
  "speedDuration": 600,
  "speedAmplifier": 0,
  "speedAmbient": false,
  "speedShowParticles": true,
  "speedShowIcon": true
}
```

- `manaConsumption`: Mana consumption per use.
