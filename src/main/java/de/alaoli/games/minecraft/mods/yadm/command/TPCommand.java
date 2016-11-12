package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.util.TeleportUtil;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class TPCommand extends Command
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public TPCommand( Command parent ) 
	{
		super( parent );
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/
	
	@Override
	public String getCommandName() 
	{
		return "tp";
	}

	@Override
	public String getCommandUsage( ICommandSender sender ) 
	{
		return super.getCommandUsage( sender ) + " <dimensionName|Id> [<x> <y> <z>] [<player]";
	}
	
	@Override
	public void processCommand( Arguments command ) 
	{
		//Usage
		if( command.isEmpty() )
		{
			this.sendUsage( command.sender );
			return;
		}
		Dimension dimension = command.parseDimension( true );
		Coordinate coordinate = null;
		EntityPlayer player = null;
		
		try
		{
			coordinate = command.parseCoordinate();
			player = command.getEntityPlayer( command.parsePlayer() );
		}
		catch( CommandException e )
		{
			//Ignore optional argument Exceptions
		}
		
		if( ( player == null ) && 
			( command.senderIsEntityPlayer ) ) 
		{
			player = (EntityPlayer)command.sender;
		}
		
		if( coordinate == null )
		{
			if( !TeleportUtil.teleport( player, dimension ) )
			{
				throw new CommandException( "Can't teleport to dimension" );
			}
		}
		else
		{
			if( !TeleportUtil.teleport( player, dimension, coordinate ) )
			{
				throw new CommandException( "Can't teleport to dimension" );
			}			
		}
		
	}
}
