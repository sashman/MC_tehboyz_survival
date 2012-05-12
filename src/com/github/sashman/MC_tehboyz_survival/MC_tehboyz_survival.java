package com.github.sashman.MC_tehboyz_survival;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.plugin.java.JavaPlugin;





public class MC_tehboyz_survival extends JavaPlugin implements Listener {
	
	public static enum GameState {Lobby, PreGame, Game};
	private GameState current_state = GameState.Lobby;
	
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
	
	private void broadcast_msg(List<Player> players, String msg){
		for (Player player : players) {
			player.sendMessage(ChatColor.AQUA
					+ msg);
		}
	}
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {

		switch (current_state) {
		case Lobby:

			/* Command /ready */
			if (cmd.getName().equalsIgnoreCase("ready")) {
				
				/* Only player can issue */
				if (sender instanceof Player) {
					List<Player> players = ((Player) sender).getWorld()
							.getPlayers();
					if (!players_playing.contains((Player) sender)) {
						players_playing.add((Player) sender);
						broadcast_msg(players,  ((Player) sender).getName()								
								+ " is ready to play! ("
								+ players_playing.size() + "/"
								+ MAX_PLAYERS + ")");
					}

					/* If enough players are ready, start the game, change to PreGame state*/
					if (players_playing.size() >= MAX_PLAYERS) {
						for (Player player : players) {
							player.sendMessage(ChatColor.AQUA + game_start_msg);
						}
						current_state = GameState.PreGame;
					}

				}
				return true;

			}

			break;

		case Game:
			break;

		default:
			break;
		}

		return false;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onMapInitializeEvent(MapInitializeEvent event){
		
	}
	
	
	@EventHandler(priority = EventPriority.HIGH)
    public void highLogin(PlayerLoginEvent event) {

    }
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		switch (current_state) {
		case Lobby:
			
			/* Creative mode for every newly joined player  */
			Player player = event.getPlayer(); 
	        player.sendMessage(ChatColor.AQUA + welcome_msg);
	        player.setGameMode(GameMode.CREATIVE);
			
			break;
		case Game:
			
			
			break;
		default:
			break;
		}	
		
		
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		/* Disallow world editing in lobby state*/
		switch (current_state) {
		case Lobby:
			event.setCancelled(true);
			break;
		default:
			break;
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		/* Disallow world editing in lobby state*/
		switch (current_state) {
		case Lobby:
			event.setCancelled(true);
			break;
		default:
			break;
		}
	}
	
}
