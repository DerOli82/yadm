package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.PlayerManager;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportException;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportPlayer;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportSettings;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class TPCommand extends Command
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	protected static final TeleportPlayer players = PlayerManager.INSTANCE;
	
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
	public Permission requiredPermission()
	{
		return Permission.OPERATOR;
	}
	
	@Override
	public void processCommand( Arguments args ) 
	{
		//Check permission
		if( !this.canCommandSenderUseCommand( args ) ) { throw new CommandException( "You're not allowed to perform this command."); }
						
		//Usage
		if( args.isEmpty() )
		{
			this.sendUsage( args.sender );
			return;
		}
		Dimension dimension = args.parseDimension( true );
		Coordinate coordinate = null;
		EntityPlayer player = null;
		
		try
		{
			coordinate = args.parseCoordinate();
			player = args.parseEntityPlayer();
		}
		catch( CommandException e )
		{
			//Ignore optional argument Exceptions
		}
		
		if( ( player == null ) && 
			( args.senderIsEntityPlayer ) ) 
		{
			player = (EntityPlayer)args.sender;
		}
		
		try
		{
			players.teleport( new TeleportSettings( dimension, player, coordinate ));
		}
		catch( TeleportException e )
		{
			throw new CommandException( e.getMessage(), e );
		}
	}
}
