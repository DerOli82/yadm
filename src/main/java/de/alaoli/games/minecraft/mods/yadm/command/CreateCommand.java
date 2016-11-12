package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.util.TeleportUtil;
import net.minecraft.command.ICommandSender;
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
	public int getRequiredPermissionLevel() 
	{
		return 2;
	}
	
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
	public void processCommand( CommandParser command ) 
	{
		//Usage
		if( command.size() < 2 )
		{
			this.sendUsage( command.getSender() );
			return;
		}
		
		try
		{
			Dimension dimension = command.parseAndCreateDimension();

			YADM.proxy.registerDimension( dimension );
			
			if( ( Config.Dimension.teleportOnCreate ) && 
				( command.senderIsEntityPlayer() ) )
			{
				if( !TeleportUtil.teleport( command.getSenderAsEntityPlayer(), dimension ) )
				{
					command.getSender().addChatMessage( new ChatComponentText( "Can't teleport to dimension" ) );
				}
			}			
		}
		catch( CommandParserException e )
		{
			throw e;
		}
		catch( RuntimeException e )
		{
			e.printStackTrace();
			command.getSender().addChatMessage( new ChatComponentText( e.getMessage() ) );
		}
	}

}
