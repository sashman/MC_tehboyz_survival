package com.github.sashman.MC_tehboyz_survival;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

/**
 * An Asynchronous repeated task which checks for players out of the world bounds and teleports them back within bounds.
 * @author HugeCannon
 */
public class WorldBoundCheckerLobby implements Runnable{
	protected Server server;
	protected int height, width;
	protected Coord bottom_left, top_right;
	//TODO need a smaller bounds checker for when game in lobby state
	
	public WorldBoundCheckerLobby(Server server, int height, int width){
		this.server = server;
		this.height = height;
		this.width = width;
		bottom_left = new Coord((height/2)*-1, (width/2)*-1);
		top_right = new Coord((height/2), (width/2));
	}
	
	@Override
	public void run() {
		if(server == null)
			return;
		checkAndMovePlayers();
		
	}
	
	protected void checkAndMovePlayers(){
		Player[] players = server.getOnlinePlayers();
		
		if(players.length == 0) return;
		for(Player p: players){
			if(!inBounds(p)){
				//TODO punishment. Should also have a pre-out-of-bounds warning distance maybe?
				
				//needs to know correct Y location
				Location l = p.getLocation();
				if(l.getX() < bottom_left.getX()) l.setX(bottom_left.getX());
				if(l.getX() > top_right.getX()) l.setX(top_right.getX());
				if(l.getZ() < bottom_left.getZ()) l.setZ(bottom_left.getZ());
				if(l.getZ() > top_right.getZ()) l.setZ(top_right.getZ());

				l.setY(p.getWorld().getHighestBlockYAt((int)l.getX(), (int)l.getZ()));
				p.teleport(l);
			}
		}
	}
	
	
	private boolean inBounds(Player p){
		double pX = p.getLocation().getX();
		double pZ = p.getLocation().getZ();
		
		return (pX > bottom_left.getX() && pX < top_right.getX() && pZ > bottom_left.getZ() && pZ < top_right.getZ());
	}

}
