package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.Player;
import de.alaoli.games.minecraft.mods.yadm.manager.PlayerManager;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportException;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportPlayer;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportSettings;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class TPToCommand extends Command
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	protected static final TeleportPlayer players = PlayerManager.INSTANCE;
	
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
		if( !args.senderIsEntityPlayer ) { throw new CommandException( "Command sender must be an player." );}
		
		try
		{
			players.teleport( new TeleportSettings( player.getDimension(), (EntityPlayer)args.sender, null ));
		}
		catch( TeleportException e )
		{
			throw new CommandException( e.getMessage(), e );
		}
	}
}
