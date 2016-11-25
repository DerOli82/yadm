package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.data.Player;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class TPToCommand extends Command
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public TPToCommand( Command parent ) 
	{
		super( parent );
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/
	
	@Override
	public String getCommandName() 
	{
		return "tpto";
	}

	@Override
	public String getCommandUsage( ICommandSender sender ) 
	{
		return super.getCommandUsage( sender ) + " <dimensionOwner>";
	}
	
	@Override
	public Permission requiredPermission()
	{
		return Permission.PLAYER;
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
		Player player = args.parsePlayer();
		
		if( !player.ownsDimension() ) { throw new CommandException( player.getManageableName() + "doesn't owns any dimension." );}
		
		
		
		/*
		Dimension dimension = args.parseDimension( true );
		Coordinate coordinate = null;
		EntityPlayer player = null;
		
		try
		{
			coordinate = args.parseCoordinate();
			player = args.getEntityPlayer( args.parsePlayer() );
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
			TeleportUtil.teleport( new TeleportSettings( dimension, player, coordinate ));
		}
		catch( TeleportException e )
		{
			throw new CommandException( e.getMessage(), e );
		}*/
	}
}
