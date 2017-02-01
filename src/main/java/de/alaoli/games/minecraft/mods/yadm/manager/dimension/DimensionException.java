package de.alaoli.games.minecraft.mods.yadm.manager.dimension;

import de.alaoli.games.minecraft.mods.lib.common.ModException;

public class DimensionException extends ModException 
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
