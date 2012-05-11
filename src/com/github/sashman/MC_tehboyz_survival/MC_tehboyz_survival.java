package com.github.sashman.MC_tehboyz_survival;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;





public class MC_tehboyz_survival extends JavaPlugin{
	public static final int MAX_PLAYERS = 3;
	private int player_count = 0;
	
	Logger log;

	public void onEnable(){ 
		log = this.getLogger();
		
	}
	 
	public void onDisable(){ 

	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("ready")){ // If the player typed /basic then do the following...
			player_count++;
			List<Player> players = ((Player)sender).getWorld().getPlayers();
			for (Player player : players) {
				player.sendMessage(((Player)sender).getName() + " is ready to play! ("+ player_count + "/" + MAX_PLAYERS+ ")");
			}
			
			return true;
		} //If this has happened the function will break and return true. if this hasn't happened the a value of false will be returned.
		return false; 
	}
	
}
