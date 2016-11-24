package de.alaoli.games.minecraft.mods.yadm.manager.player;

public class TeleportException extends RuntimeException 
{
	public TeleportException( String message )
	{
		super( message );
	}
	
	public TeleportException( String message, Exception e )
	{
		super(message, e );
	}
}
