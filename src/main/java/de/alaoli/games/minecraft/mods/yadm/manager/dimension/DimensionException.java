package de.alaoli.games.minecraft.mods.yadm.manager.dimension;

import de.alaoli.games.minecraft.mods.yadm.YADMException;

public class DimensionException extends YADMException 
{
	public DimensionException( String msg )
	{
		super( msg );
	}
	
	public DimensionException( String msg, Exception e )
	{
		super( msg, e );
	}	
}
