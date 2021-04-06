rubataga's Everyhunt Plugin

**Hunters**
- Hunters are able to use the Tracking Compass.
- Hunters can track entities and players.

**Runners**
- The purpose of being a runner is
    - being listed as a runner in the /eh teams command
    - being available in the compass-right-click cycle (see Tracking Compass below)
- Players can be tracked regardless of if they are or aren't a runner.
- A player can be a hunter AND runner at the same time.

**Tracking Compass**
- Hunters use tracking compass to, track their target.
- Right-clicking an entity tracks them.
    - If a hunter doesn't want to lose their target, it is recommended that they lock their compass using the GUI (see below)
    - Any entities can be tracked, so mobs and players. Again, locking the compass will prevent hunters from accidentally tracking something else.
- Right-clicking the air cycles through targeting runners.
- Right-clicking the air after your target dies resets the compass.
- Sneaking+Right-clicking opens up the GUI
    - The compass in the center gives information about the target. Get a new compass by clicking the compass
    - The dirt/bedrock block on the top locks/unlocks your compass. Bedrock = locked, Dirt = unlocked. Using /eh track still works with a locked compass.
    - None of the other buttons do anything yet
- If a hunter's target dies, their compass will continue to track the location of the death until it is reset via a right-click, a new target, or /eh recal
    - Lock the compass in order to keep track of death location.
- If a hunter's target enters a portal, their compass will continue to track the location of the portal they last entered.
    - [NEW] The compass works in the Nether! 

**commands:**
- /eh addhunter [player] - makes a player into a hunter
- /eh addrunner [player] - makes a player into a runner
- /eh remove [player] - removes all of a player's roles
- /eh track [player] - lets a hunter track another player
    - /eh track @p tracks the closest player
- /eh compass [player] - gives a hunter a new tracking compass
- /eh teams - view all current hunters and runners
- /eh recal - recalibrates the compass to world spawn (only hunters should use this, any other players using this causes errors ATM)
- /eh gui - pulls up the gui

Any [player] can be a specific player, or a target selector (@a). If @a is used, then all players are affected.
Players can use commands on themselves without including [player], or OPs/console can direct commands at players using [player].
