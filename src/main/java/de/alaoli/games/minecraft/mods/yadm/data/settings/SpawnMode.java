package de.alaoli.games.minecraft.mods.yadm.data.settings;

import de.alaoli.games.minecraft.mods.lib.common.data.DataException;

public enum SpawnMode 
{
	EXACT( "exact"),
	TOPDOWN( "topdown" );
	
	private String name;
	
	private SpawnMode( String name )
	{
		this.name = name;
	}
		
	@Override
	public String toString() 
	{
		return this.name;
	}

	public static boolean hasMode( String name )
	{
		return get( name ) != null;
	}
	
	public static SpawnMode get( String name )
	{
		for( SpawnMode mode : SpawnMode.values() )
		{
			if( mode.toString().equals( name ) )
			{
				return mode;
			}
		}
		throw new DataException( "Unknown SpawnMode." );
	}	
}
