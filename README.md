# EssentialsWarpGUI
![Banner](https://public.marcely.de/data/img/products/warpgui/banner.png)

## Features
* Stability:
  * Full support for all versions, starting at 1.8.8
  * Better support for modern versions (1.13+)
  * Full support for legacy Essentials and EssentialsX
  * Clean and modular code-base
  * Lightweight, no bullshit included
  * Multi-threading support (less stress on your main thread -> almost no effect on the TPS)
* Functionality:
  * It just works
  * Pretty straightforward, don't waste time by configuring tons of things
  * Does not replace the warps functionality of Essentials. Builds on top of it
  * No need to recreate the warps. This plugin automatically detects them
  * Each warp is being stored in its own file -> easily migrate and modify them
  * Fully customizable (even all the messages)
  * Multi-page support. Add as many warps as you want
  * Cute sounds that get played at certain events (can be disabled)
  * GUI looks decent, can be easily and fully customized as well
  * Hex colors support (1.16+ only, e.g. &#FF00FF)
* Warp button customizations
  * Change the display name in the GUI
  * Modify the icon
  * Add lore (text below the name)
  * Forcefully move the warps around where ever you like
  
## Screenshots
![Screenshot 1](https://public.marcely.de/data/img/products/warpgui/2023-03-13_18.30.40%20-%20Kopie.png)
![Screenshot 2](https://public.marcely.de/data/img/products/warpgui/2023-03-13_17.49.18.png)
![Screenshot 3](https://public.marcely.de/data/img/products/warpgui/2023-03-13_17.49.09.png)

## Permissions
* Essentials permissions aren't being touched by this plugin.
* However, the player won't see the warps for which he doesn't have the permissions for in the GUI.
* This means, that players must either have `essentials.warps.<warpname>` (grants access to specific warps) or `essentials.warps.*` (grants access to all warps).
* There is also an Essentials configuration with which you are able to check the permission check completely (called `per-warp-permission`).
* For further help, look at this permissions list or contact Essentials's support.
* To access the `/warpcfg` command, you will need the `warpcfg.cfg` permission.

## Item Syntax
* There is a specific syntax required for the icon parameter of the /kitcfg icon <kit> <icon> command.
* Following features are available with it:
  * It is possible to use legacy material ids that were used past 1.12 (Not recommended)
  * It is possible to use material names, even from newer or past versions
  * Ability to have custom head textures:
    * player_head:<player name>
    * player_head:<texture>
  * Ability to dye leather armor using HEX colors:
    * leather_chestplate:#FF00FF
* Examples:
  * `stone`
  * `1` (turns into stone)
  * `1:1` (turns into granite)
  * `iron_pickaxe:50` (adds some damage to the pickaxe)
  * `player_head:Notch` (Notch's head)
  * `player_head:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA0ODMxZjdhN2Q4ZjYyNGM5NjMzOTk2ZTM3OThlZGFkNDlhNWQ5YmNkMThlY2Y3NWJmYWU2NmJlNDhhMGE2YiJ9fX0=` (displays a globe, taken from the "other -> value" section at [minecraft-heads.com](https://minecraft-heads.com/custom-heads/decoration/60444-globe-with-base))
  * `leather_chestplate:#FFFF00` (yellow chestplate)
* You may look at e.g. [this website](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html) for a list of all available materials. Materials that do not exist with your Minecraft version obviously won't work.

## Contributions
We are open for contributions! For that, simply create a pull request for the `master` branch. Any pull request for any other branch will be rejected.
