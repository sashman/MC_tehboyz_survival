package com.github.sashman.MC_tehboyz_survival;

import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.world.ChunkEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebeaninternal.server.cluster.mcast.Message;





public class MC_tehboyz_survival extends JavaPlugin implements Listener {
	public static int MAX_PLAYERS = 2;
	
	public static String welcome_msg = "Welcome to the tehboyz survival mod! Type /ready if you are ready to participate";
	public static String game_start_msg = "Game will start shortly!";
	
	
	/**
	 * List of players actually taking part in the game
	 */
	private ArrayList<Player> players_playing = new ArrayList<Player>();
	
	Logger log;

	public void onEnable(){ 
		log = this.getLogger();
		 getServer().getPluginManager().registerEvents(this, this);
	}
	 
	public void onDisable(){ 

	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("ready")){
			
			if (sender instanceof Player) {
				
				List<Player> players = ((Player) sender).getWorld()
				.getPlayers();
				
				if(!players_playing.contains((Player) sender)){
					players_playing.add((Player) sender);
					
					
					for (Player player : players) {
						player.sendMessage(ChatColor.AQUA + ((Player) sender).getName()
								+ " is ready to play! (" + players_playing.size() + "/"
								+ MAX_PLAYERS + ")");
					}
				}
				
				if(players_playing.size()>= MAX_PLAYERS){
					for (Player player : players) {
						player.sendMessage(ChatColor.AQUA + game_start_msg);
					}
				}

			}
			return true;

		} // If this has happened the function will break and return true. if this hasn't happened the a value of false will be returned.
		return false; 
	}
	
	@EventHandler(priority = EventPriority.HIGH)
    public void highLogin(PlayerLoginEvent event) {

    }
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer(); 
        player.sendMessage(ChatColor.AQUA + welcome_msg);

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(player.getMaxHealth());
        player.setGameMode(GameMode.CREATIVE);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		event.setCancelled(true);
	}
	
}
