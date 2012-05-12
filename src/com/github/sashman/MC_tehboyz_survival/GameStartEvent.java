package com.github.sashman.MC_tehboyz_survival;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartEvent  extends Event {

	private static final HandlerList handlers = new HandlerList();
    private String message;
 
    public GameStartEvent(String m) {
        message = m;
    }
 
    public String getMessage() {
        return message;
    }
 
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
