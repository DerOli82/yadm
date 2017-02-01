package de.alaoli.games.minecraft.mods.lib.common.data;

import java.util.StringJoiner;

public class ChunkCoordinate 
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	public final int x;
	public final int z;
	
	/********************************************************************************
	 * Method
	 ********************************************************************************/
	
	public ChunkCoordinate( int x, int z )
	{
		this.x = x;
		this.z = z;
	}
	
	@Override
	public String toString() 
	{
		StringJoiner join = new StringJoiner( ", " )
			.add( String.valueOf( this.x ) )
			.add( String.valueOf( this.z ) );
		return join.toString();
	}	
}
