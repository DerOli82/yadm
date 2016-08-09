package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Queue;

import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.util.CommandUtil;
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
	public void processCommand( ICommandSender sender, Queue<String> args ) 
	{
		//Usage
		if( args.isEmpty() )
		{
			sender.addChatMessage( new ChatComponentText( this.getCommandUsage( sender ) ) );
			return;
		}
		Dimension dimension = CommandUtil.parseTeleportDimension( sender, args );
		
		if( dimension == null ) { return; }
				
		Coordinate coordinate = CommandUtil.parseCoordinate( sender, args );
		EntityPlayer player = CommandUtil.parsePlayer( sender, args );
		
		if( player == null )
		{
			player = sender.getEntityWorld().getPlayerEntityByName( sender.getCommandSenderName() );
		}
		
		if( coordinate == null )
		{
			if( !TeleportUtil.teleport( player, dimension ) )
			{
				sender.addChatMessage( new ChatComponentText( "Can't teleport to dimension" ) );
			}
		}
		else
		{
			if( !TeleportUtil.teleport( player, dimension, coordinate ) )
			{
				sender.addChatMessage( new ChatComponentText( "Can't teleport to dimension" ) );
			}			
		}
		
	}
}
