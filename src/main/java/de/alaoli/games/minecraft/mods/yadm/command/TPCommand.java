package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.util.TeleportUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

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
	public int getRequiredPermissionLevel() 
	{
		return 2;
	}
		
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
	public void processCommand( CommandParser command ) 
	{
		//Usage
		if( command.isEmpty() )
		{
			this.sendUsage( command.getSender() );
			return;
		}
		Dimension dimension = command.parseDimension( true );
		Coordinate coordinate = null;
		EntityPlayer player = null;
		
		try
		{
			coordinate = command.parseCoordinate();
			player = command.parsePlayer();
		}
		catch( CommandParserException e )
		{
			//Ignore optional argument Exceptions
		}
		if( player == null )
		{
			player = command.getSender().getEntityWorld().getPlayerEntityByName( command.getSender().getCommandSenderName() );
		}
		
		if( coordinate == null )
		{
			if( !TeleportUtil.teleport( player, dimension ) )
			{
				command.getSender().addChatMessage( new ChatComponentText( "Can't teleport to dimension" ) );
			}
		}
		else
		{
			if( !TeleportUtil.teleport( player, dimension, coordinate ) )
			{
				command.getSender().addChatMessage( new ChatComponentText( "Can't teleport to dimension" ) );
			}			
		}
		
	}
}
