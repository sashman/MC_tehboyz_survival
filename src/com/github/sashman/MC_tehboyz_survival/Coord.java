package com.github.sashman.MC_tehboyz_survival;

public class Coord {
	private int x,z;
	
	public Coord(int x, int z){
		this.x = x;
		this.z = z;
	}
	
	public int getX(){
		return x;
	}
	public int getZ(){
		return z;
	}
	
	public void setX(int x){
		this.x = x;
	}
	public void setZ(int z){
		this.z = z;
	}
}
