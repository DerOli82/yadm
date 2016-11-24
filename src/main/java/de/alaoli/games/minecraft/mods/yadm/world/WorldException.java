package de.alaoli.games.minecraft.mods.yadm.world;

public class WorldException extends RuntimeException 
{
	public WorldException( String message )
	{
		super( message ); 
	}
	
	public WorldException( String message, Exception e )
	{
		super( message, e ); 
	}	
}
