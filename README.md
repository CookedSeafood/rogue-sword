# RogueSword

A sword that gives a 30 second speed effect for 1 mana when right-clicked.

A "Rogue Sword" is an item where `minecraft:item_name` is "Rogue Sword".

An example command to get it:

```mcfunction
/give @s golden_sword[item_name="Rogue Sword"]
```

Get the original design of it:

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
