# Team with your friends and colect all the items needed to win!

Inspired in the map created by lorgon111 (twitter)

### How to configure:

```yaml
Game:
  #Game difficulty, can be 0, 1 or 2.
  Difficulty: 1
  #If true players need to complete all the bingo card, if false only one line or row.
  Fullcard: false
  #Sets the end of the game, no one wins if it reachs the time.
  EndTime: '25:00'
World:
  #Spawnpoint, can be set with /setspawn.
  Spawn: world, 0, 100, 0, 90, 0
  #Don't modify!
  OldWorld: '0'
```
* Set a spawnpoint with /setspawn
* Once everyone is ready start the game with /start

### Features:

    Custom world where you can find any biome.
    Very high dungeon spawn rates.
    Up to 4 teams.
    Bingo card GUI with /bingo
    Supports 1.8-1.12.2
    Customizable messages.


### Instructions:

    Select a team with /team
    Wait until the OP starts the game.
    You will be randomly teleported to the game world.
    When the game starts you'll start by searching items for the bingo card.
    Once one team completes a line, row or all the card the game ends.

### Commands:

    /bingo - Open the bingo card
    /team - Select a team
    /start - Start the game (perm: bingo.start)
    /setspawn - Set the spawnpoint (perm: bingo.setspawn)

### Bingo card:

![](https://i.gyazo.com/dc8e0e6e0c331e0159e59d438f197c42.png)
