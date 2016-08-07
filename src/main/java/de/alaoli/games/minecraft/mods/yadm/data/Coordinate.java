package de.alaoli.games.minecraft.mods.yadm.data;

public class Coordinate 
{
	public final int dimId;
	
	public final int x;
	
	public final int y;
	
	public final int z;
	
	public Coordinate( int dimId, int x, int y, int z )
	{
		this.dimId = dimId;
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
