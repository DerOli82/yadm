package de.alaoli.games.minecraft.mods.yadm.data;

public class Coordinate 
{
	private int dimId;
	
	private int x;
	
	private int y;
	
	private int z;
	
	public Coordinate( int dimId, int x, int y, int z )
	{
		this.dimId = dimId;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getDimId() 
	{
		return this.dimId;
	}

	public int getX() 
	{
		return this.x;
	}

	public int getY() 
	{
		return this.y;
	}

	public int getZ() 
	{
		return this.z;
	}
}
