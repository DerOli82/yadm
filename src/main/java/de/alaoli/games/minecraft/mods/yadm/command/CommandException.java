package de.alaoli.games.minecraft.mods.yadm.command;

public class CommandException extends RuntimeException 
{
	public CommandException( String message )
	{
		super( message );
	}
	
	public CommandException( String message, Exception e )
	{
		super( message, e );
	}	
}
