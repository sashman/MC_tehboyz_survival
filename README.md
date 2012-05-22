MC_tehboyz_survival
===================

Minecraft mod featuring game play mechanics in the style of Battle Royale

### Version v0.1 (sashman, hugecannon)

* Number of participating players is capped to a maximum number in the config file
* Initially everybody is in creative mode
* Block placing and creature spawning is disabled until the game starts
* Once all players send **/ready** message, the game can begin
* After a short countdown, participants are teleported in spread locations
* Set participants to survival mode
* World bounds are present and shrink every set time period
* A number of game properties are available to be chnaged in the config file


##### DONE

* Release v0.1
	* Cap server to a specified number of participating players
	* Once all players send **/ready** message, change state
	* Start in invicibility stage, countdown for x seconds of invincibility
	* Before game has began, no one is allowed to place/break blocks
	* Teleport players in spread locations using a spread algorithm
	* Put players in vulnerable state after invincibility timer is up
	* Allow block placing, desctruction, etc.
	* Make a config file, specifying number of participants etc.
	* Put bounds on world
 	* Decrease the size of the world every day (or time period)

##### TODO
	
* Release v0.2
	* Spectators - Perhaps use http://dev.bukkit.org/server-mods/watcher/
	* Varying punishments for being outside the bounds
	* Once a player dies, remove him from participants and kick from server
	* If a player rejoins the server, set him as a spectator
 	* All non-participants are set as spectators when the game starts

* Release v0.3
	* Team based games

###### States
1. Player lobby stage, players entering
2. Starting invinciblility stage, players teleported to starting locations
3. Game stage, players surviving to the last man alive

