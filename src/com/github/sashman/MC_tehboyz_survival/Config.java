package com.github.sashman.MC_tehboyz_survival;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Pattern;

public abstract class Config {

	public static final String[] CONFIG_KEYS = new String[]{	"MAX_PLAYERS", 
																"WORLD_SIZE", 
																"BOUNDS_CHANGE_TIME"};
	
	/* Reads from config file. 
	 * If file does not exist then creates and writes default values to file
	 * Checks for presence of each of the config keys. If any are missing from the file
	 * then they are written to the file with default values and alerted to console.
	 */
	
	public static void readFile(MC_tehboyz_survival survival){
		File f = new File(survival.CONFIG_LOCATION);
		String pattern = "^[a-zA-Z0-9_]+\\s+[a-zA-Z0-9]+$"; //Matches key<whitespace>val
		HashSet<String> keys = new HashSet<String>();
		for(String key: CONFIG_KEYS){
			keys.add(key);
		}
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(f));
			String s = br.readLine();
			survival.log.info("Reading config file.");
			while(s != null){
				if(Pattern.matches(pattern, s)){
					String[] keyval = s.split(" ");
					//TODO need to do verification of values
					if(keys.contains(keyval[0])){
						keys.remove(keyval[0]);
						
						if(keyval[0].equals("MAX_PLAYERS"))
							survival.MAX_PLAYERS = new Integer(keyval[1]);
						if(keyval[0].equals("WORLD_SIZE"))
							survival.WORLD_SIZE = new Integer(keyval[1]);
						if(keyval[0].equals("BOUNDS_CHANGE_TIME"))
							survival.BOUNDS_CHANGE_TIME = new Integer(keyval[1]);
					}else{
						survival.log.info("Key: '" + keyval[0] + "' not recognised.");
					}
				}
				s = br.readLine();
			}
			
		} catch (FileNotFoundException e) {
			survival.log.info("Config file not found at :'" + f.getAbsolutePath() + "'. Creating file and using default values.");
			try {
				f.createNewFile();
			} catch (IOException e1) {
				survival.log.info("Unable to create config file at: " + f.getAbsolutePath());
			}
		} catch (IOException e) {
			survival.log.info("Unable to read from config file. Using default values.");
		}
		
		if(!keys.isEmpty()){
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new FileWriter(f));
				survival.log.info("The following keys were not in the config file:");
				for(String k: keys){
					survival.log.info(k);
					if(k.equals("MAX_PLAYERS"))
						bw.write(k + " " + survival.MAX_PLAYERS + "\n");
					if(k.equals("WORLD_SIZE"))
						bw.write(k + " " + survival.WORLD_SIZE + "\n");
					if(k.equals("BOUNDS_CHANGE_TIME"))	
						bw.write(k + " " + survival.BOUNDS_CHANGE_TIME + "\n");
				}
				bw.flush();
			} catch (IOException e) {
				survival.log.info("Unable to write to config file at: " + f.getAbsolutePath());
			}
		}
		survival.log.info("Reading from config file complete");
		
	}

}
