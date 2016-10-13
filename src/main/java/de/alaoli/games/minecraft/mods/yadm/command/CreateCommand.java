package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Queue;

import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.Template;
import de.alaoli.games.minecraft.mods.yadm.manager.TemplateManager;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.util.CommandUtil;
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
		return super.getCommandUsage( sender ) + " <dimensionName> <templateName>";
	}
	
	@Override
	public void processCommand( ICommandSender sender, Queue<String> args ) 
	{
		//Usage
		if( args.size() < 2 )
		{
			sender.addChatMessage( new ChatComponentText( this.getCommandUsage( sender ) ) );
			return;
		}
		String name = args.remove();
		String templateName = args.remove();
		
		if( YADimensionManager.instance.exists( name ) )
		{
			sender.addChatMessage( new ChatComponentText( "Dimension '" + name + "' already exists." ) );
			return;
		}
		if( !TemplateManager.instance.exists( "default", templateName ) ) 
		{
			sender.addChatMessage( new ChatComponentText( "Template '" + templateName + "' doesn't exists." ) );
			return;
		}
		if( CommandUtil.isInt( name ) )
		{
			sender.addChatMessage( new ChatComponentText( "Numbers aren't allowed for dimension names" ) );
			return;
		}
		Template template = (Template) TemplateManager.instance.get( "default", templateName );
		
		try
		{
			Dimension dimension = YADimensionManager.instance.create( name, template );	
			
			YADM.proxy.registerDimension( dimension );
			
			if( Config.Dimension.teleportOnCreate )
			{
				EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName( sender.getCommandSenderName() );

				if( !TeleportUtil.teleport( player, dimension ) )
				{
					sender.addChatMessage( new ChatComponentText( "Can't teleport to dimension" ) );
				}
			}			
		}
		catch( RuntimeException e )
		{
			e.printStackTrace();
			sender.addChatMessage( new ChatComponentText( e.getMessage() ) );
		}	
	}

}
