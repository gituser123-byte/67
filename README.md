# Advancement Enchant Multiplier — Fabric 1.21.11

## Gameplay
- With 0 completed advancements, newly crafted supported gear starts at level 1.
- With 1 advancement, it starts at level 2.
- With 2 advancements, level 4.
- With 3 advancements, level 8.
- Every newly completed advancement doubles every enchantment on items in the
  player's inventory, hotbar, offhand and armor slots.
- Cap: 32,768.

## Starter enchantments
Pickaxes: Efficiency, Unbreaking, Fortune
Axes: Efficiency, Sharpness, Unbreaking
Shovels/Hoes: Efficiency, Unbreaking
Swords: Sharpness, Looting, Unbreaking
Armor: Protection, Unbreaking, plus helmet/boot extras
Bows/Crossbows/Tridents and several utility tools are also supported.

## Build on GitHub
Upload all files. If `.github` is skipped, create
`.github/workflows/build.yml` manually using the included file.
Open Actions > Build Mod > Run workflow.
