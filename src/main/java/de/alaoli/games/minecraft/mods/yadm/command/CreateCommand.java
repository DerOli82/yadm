package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.teleport.TeleportException;
import de.alaoli.games.minecraft.mods.yadm.teleport.TeleportSettings;
import de.alaoli.games.minecraft.mods.yadm.util.TeleportUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class CreateCommand extends Command
{
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

			YADM.proxy.registerDimension( dimension );
			
			if( ( Config.Dimension.teleportOnCreate ) && 
				( args.senderIsEntityPlayer ) )
			{
				TeleportUtil.teleport( new TeleportSettings( dimension, (EntityPlayer)args.sender ) );
			}			
		}
		catch( CommandException e )
		{
			throw e;
		}
		catch( TeleportException e )
		{
			throw new CommandException( e.getMessage(), e );
		}
		catch( RuntimeException e )
		{
			throw new CommandException( e.getMessage(), e );
		}
	}

}
