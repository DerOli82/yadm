package de.alaoli.games.minecraft.mods.lib.common.data;

import java.util.StringJoiner;

public class Coordinate 
{
	public final int x;
	
	public final int y;
	
	public final int z;
	
	public Coordinate( int x, int y, int z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String toString() 
	{
		StringJoiner join = new StringJoiner( ", " )
			.add( String.valueOf( this.x ) )
			.add( String.valueOf( this.y ) )
			.add( String.valueOf( this.z ) );
		return join.toString();
	}
}
