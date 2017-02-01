package de.alaoli.games.minecraft.mods.lib.common.data;

import java.util.StringJoiner;

public class DimensionChunkCoordinate extends ChunkCoordinate
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/

	public final int dimId;

	/********************************************************************************
	 * Method
	 ********************************************************************************/

	public DimensionChunkCoordinate( int dimId, int x, int z )
	{
		super( x, z );
		
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
