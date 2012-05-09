package com.github.sashman.MC_tehboyz_survival;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class MC_tehboyz_survival extends JavaPlugin{
	Logger log;

	public void onEnable(){ 
		log = this.getLogger();
		log.info("Your plugin has enabled");
	}
	 
	public void onDisable(){ 
		log.info("Your plugin has disabled");
	}
	
	
}
