package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.lib.common.ModException;

public class CommandException extends ModException 
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
