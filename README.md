# RogueSword

Rogue Sword is an item that gives a 30 second speed effect for 1 mana when right-clicked.

## The Item

An item where `minecraft:custom_data.id` is "rogue_sword:rogue_sword" is considered as a rogue sword.

An example command that gives a rogue sword to yourslef:

```mcfunction
/give @s golden_sword[custom_data={id:"rogue_sword:rogue_sword"}]
```

Another example command for the original design:

```mcfunction
/give @s golden_sword[custom_data={id:"rogue_sword:rogue_sword"},item_name={text:"Rogue Sword"},rarity=uncommon]
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

### `manaConsumption`

Mana consumption per use.
