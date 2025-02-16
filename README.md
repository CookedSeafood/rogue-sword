# RogueSword

A sword that gives a 30 second speed effect for 16777216 mana when right-clicked.

A "Rogue Sword" is an item where `minecraft:item_name` is "Rogue Sword".

An example command to get it:

```mcfunction
/give @s golden_sword[item_name="Rogue Sword"]
```

Get the original design of it:

```mcfunction
/give @s golden_sword[item_name="Rogue Sword",rarity=uncommon]
```

## Config

Below is a template config file `config/roguesword.json` filled with default values. You may only need to write the lines you would like to modify.

```json
{
  "manaConsumption": 16777216,
  "statusEffectDuration": 600,
  "statusEffectAmplifier": 0,
  "statusEffectAmbient": false,
  "statusEffectShowParticles": true,
  "statusEffectShowIcon": true
}
```

- `manaConsumption`: Amount of mana will be consumed per use.
