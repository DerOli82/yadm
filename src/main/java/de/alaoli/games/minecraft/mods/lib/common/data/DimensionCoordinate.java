package de.alaoli.games.minecraft.mods.lib.common.data;

import java.util.StringJoiner;

public class DimensionCoordinate extends Coordinate
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	public final int dimId;
	
	/********************************************************************************
	 * Method
	 ********************************************************************************/
	
	public DimensionCoordinate( int dimId, int x, int y, int z )
	{
		super( x, y, z );
		
		this.dimId = dimId;
	}

	@Override
	public String toString() 
	{
		StringJoiner join = new StringJoiner( " | " )
			.add( String.valueOf( this.dimId ) )
			.add( super.toString() );
		return join.toString();
	}
}
