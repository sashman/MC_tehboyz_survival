MC_tehboyz_survival
===================

Minecraft mod featuring game play mechanics in the style of Battle Royale

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

##### TODO

* Release v0.1
	* Decrease the size of the world every day (or time period)
	
* Release v0.2
	* Spectators

###### States
1. Player lobby stage, players entering
2. Starting invinciblility stage, players teleported to starting locations
3. Game stage, players surviving to the last man alive

