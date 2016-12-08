package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.YADMException;

public class CommandException extends YADMException 
{
	public CommandException( String msg )
	{
		super( msg );
	}
	
	public CommandException( String msg, Exception e )
	{
		super( msg, e );
	}	
}
