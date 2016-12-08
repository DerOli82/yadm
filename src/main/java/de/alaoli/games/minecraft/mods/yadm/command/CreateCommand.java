package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.YADMException;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.PlayerManager;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportPlayer;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportSettings;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CreateCommand extends Command
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	protected static final TeleportPlayer players = PlayerManager.INSTANCE;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public CreateCommand( Command parent ) 
	{
		super( parent );
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/
	
	@Override
	public String getCommandName() 
	{
		return "create";
	}

	@Override
	public String getCommandUsage( ICommandSender sender ) 
	{
		return super.getCommandUsage( sender ) + " [<templateGroup>:]<templateName> [<dimensionGroup>:]<dimensionName> [<owner>]";
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
		if( args.size() < 2 )
		{
			this.sendUsage( args.sender );
			return;
		}
		
		try
		{
			Dimension dimension = args.parseAndCreateDimension();

			if( ( Config.Dimension.teleportOnCreate ) && 
				( args.senderIsEntityPlayer ) )
			{
				players.teleport( new TeleportSettings( dimension, (EntityPlayer)args.sender ) );
			}			
		}
		catch( YADMException e )
		{
			throw new CommandException( e.getMessage(), e );
		}
	}

}
