package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Queue;

import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.DimensionTemplate;
import de.alaoli.games.minecraft.mods.yadm.manager.TemplateManager;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.util.TeleportUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class CreateSubCommand implements SubCommand
{
	public static final CreateSubCommand instance = new CreateSubCommand();
	
	private CreateSubCommand() {}
	
	@Override
	public String getCommandName()
	{
		return "create";
	}
	
	@Override
	public int getRequiredPermissionLevel() 
	{
		return 4;
	}
	
	@Override
	public String getCommandUsage( ICommandSender sender )
	{
		return "/" + DefaultCommand.COMMAND + " create <dimensionName> <templateName>";
	}
	
	@Override
	public void processCommand( ICommandSender sender, Queue<String> argQueue ) 
	{
		//Usage
		if( argQueue.size() < 2 )
		{
			sender.addChatMessage( new ChatComponentText( "Usage: " + this.getCommandUsage( sender ) ) );
			return;
		}
		String name = argQueue.remove();
		String templateName = argQueue.remove();
		
		if( YADimensionManager.instance.exists( name ) )
		{
			sender.addChatMessage( new ChatComponentText( "Dimension '" + name + "' already exists." ) );
			return;
		}
		if( !TemplateManager.instance.exists( templateName ) ) 
		{
			sender.addChatMessage( new ChatComponentText( "Template '" + templateName + "' doesn't exists." ) );
			return;
		}
		DimensionTemplate template = (DimensionTemplate) TemplateManager.instance.get( templateName );
		
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
