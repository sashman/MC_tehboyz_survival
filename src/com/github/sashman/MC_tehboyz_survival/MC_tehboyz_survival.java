package com.github.sashman.MC_tehboyz_survival;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	private static final String CONFIG_LOCATION = "plugins/survival.config";
	private static final String[] CONFIG_KEYS = new String[]{	"MAX_PLAYERS", 
																"WORLD_SIZE", 
																"BOUNDS_CHANGE_TIME"};
	
	public static enum GameState {Lobby, PreGame, Game};
	private GameState current_state = GameState.Lobby;
	
	public static int MAX_PLAYERS = 1;
	public static int WORLD_SIZE = 2048; //TODO Not a scoob what a decent default value is.
	public static int BOUNDS_CHANGE_TIME = 10; // In mins?
	
	public static int COUNTDOWN_SEC = 10;
	
	private static int[] SPAWN_LOCATION = {0,0,0};
	
	//how far from the centre the players will be teleported
	private static int TELEPORT_RADIUS = 10;
	//radain difference between each player
	private static float TELEPORT_RADIAN_OFFSET = (float) (MAX_PLAYERS/(Math.PI*2));
	
	public static String welcome_msg = "Welcome to the tehboyz survival mod! Type /ready if you are ready to participate";
	public static String game_start_msg = "Game will start shortly! Prepare to be teleported...";
	
	
	/**
	 * List of players actually taking part in the game
	 */
	private ArrayList<Player> players_playing = new ArrayList<Player>();
	
	Logger log;

	public void onEnable(){
		readFromConfig();
		
		log = this.getLogger();
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	
	/* Reads from config file. 
	 * If file does not exist then creates and writes default values to file
	 * Checks for presence of each of the config keys. If any are missing from the file
	 * then they are written to the file with default values and alerted to console.
	 */
	private void readFromConfig() {
		File f = new File(CONFIG_LOCATION);
		String pattern = "^[a-zA-Z0-9_]+\\s+[a-zA-Z0-9]+$"; //Matches key<whitespace>val
		HashSet<String> keys = new HashSet<String>();
		for(String key: CONFIG_KEYS){
			keys.add(key);
		}
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(f));
			String s = br.readLine();
			System.out.println("Reading config file.");
			while(s != null){
				if(Pattern.matches(pattern, s)){
					String[] keyval = s.split(" ");
					//TODO need to do verification of values
					if(keys.contains(keyval[0])){
						keys.remove(keyval[0]);
						
						if(keyval[0].equals("MAX_PLAYERS"))
							MAX_PLAYERS = new Integer(keyval[1]);
						if(keyval[0].equals("WORLD_SIZE"))
							WORLD_SIZE = new Integer(keyval[1]);
						if(keyval[0].equals("BOUNDS_CHANGE_TIME"))
							BOUNDS_CHANGE_TIME = new Integer(keyval[1]);
					}else{
						System.out.println("Key: '" + keyval[0] + "' not recognised.");
					}
				}
				s = br.readLine();
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Config file not found at :'" + f.getAbsolutePath() + "'. Creating file and using default values.");
			try {
				f.createNewFile();
			} catch (IOException e1) {
				System.out.println("Unable to create config file at: " + f.getAbsolutePath());
			}
		} catch (IOException e) {
			System.out.println("Unable to read from config file. Using default values.");
		}
		
		if(!keys.isEmpty()){
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new FileWriter(f));
				System.out.println("The following keys were not in the config file:");
				for(String k: keys){
					System.out.println(k);
					if(k.equals("MAX_PLAYERS"))
						bw.write(k + " " + MAX_PLAYERS + "\n");
					if(k.equals("WORLD_SIZE"))
						bw.write(k + " " + WORLD_SIZE + "\n");
					if(k.equals("BOUNDS_CHANGE_TIME"))	
						bw.write(k + " " + BOUNDS_CHANGE_TIME + "\n");
				}
				bw.flush();
			} catch (IOException e) {
				System.out.println("Unable to write to config file at: " + f.getAbsolutePath());
			}
		}
		System.out.println("Reading from config file complete");
		System.out.println("Values:");
		for(String k: CONFIG_KEYS){
			if(k.equals("MAX_PLAYERS"))
				System.out.println(k + "=" + MAX_PLAYERS);
			if(k.equals("WORLD_SIZE"))
				System.out.println(k + "=" + WORLD_SIZE);
			if(k.equals("BOUNDS_CHANGE_TIME"))	
				System.out.println(k + "=" + BOUNDS_CHANGE_TIME);
		}
		
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
						dispatchCounter();
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
	
	private void dispatchCounter() {

		/* Start new thread used for the game countdown */
		this.getServer().getScheduler()
				.scheduleSyncDelayedTask(this, new Runnable() {
					public void run() {

						int count = COUNTDOWN_SEC;
						while (count > 0) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							getServer().broadcastMessage(ChatColor.AQUA + "" + count-- + " sec left");

						}
						getServer().broadcastMessage(ChatColor.AQUA + "Game has began!");
						current_state = GameState.Game;
						GameStartEvent event = new GameStartEvent("Game started");
						getServer().getPluginManager().callEvent(event);
					}
				}, 40L);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onMapInitializeEvent(MapInitializeEvent event){
	
		event.getMap().getWorld().setSpawnLocation(SPAWN_LOCATION[0], SPAWN_LOCATION[1], SPAWN_LOCATION[2]);
	}
	
	
	@EventHandler(priority = EventPriority.HIGH)
    public void highLogin(PlayerLoginEvent event) {

    }
	
	private void teleportPlayers(){
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
    public void onGameStart(GameStartEvent event) {
		
		teleportPlayers();
		
    }
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		switch (current_state) {
		case Lobby:
		case PreGame:
			
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
		case PreGame:
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
		case PreGame:
			event.setCancelled(true);
			break;
		default:
			break;
		}
	}
	
}
