package de.alaoli.games.minecraft.mods.yadm.data;

import com.google.gson.annotations.Expose;

public class Coordinate 
{
	@Expose
	public final int x;
	
	@Expose
	public final int y;
	
	@Expose
	public final int z;
	
	public Coordinate( int x, int y, int z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
