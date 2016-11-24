package de.alaoli.games.minecraft.mods.yadm.manager.dimension;

public class DimensionException extends RuntimeException 
{
	public DimensionException( String message )
	{
		super( message );
	}
	
	public DimensionException( String message, Exception e )
	{
		super( message, e );
	}	
}
