package de.alaoli.games.minecraft.mods.yadm.data.settings;

import de.alaoli.games.minecraft.mods.lib.common.data.DataException;

public enum BorderSide 
{
	NORTH( "north" ),
	EAST( "east" ),
	SOUTH( "south" ),
	WEST( "west" ),
	ALL( "all" ),
	INSIDE( "inside" ),
	OUTSIDE( "outside" );
	
	private String name;
	
	private BorderSide( String name )
	{
		this.name = name;
	}
		
	@Override
	public String toString() 
	{
		return this.name;
	}

	public static BorderSide get( String name ) throws DataException
	{
		for( BorderSide side : BorderSide.values() )
		{
			if( side.toString().equals( name ) )
			{
				return side;
			}
		}
		throw new DataException( "Unknown BorderSide." );
	}	
}
