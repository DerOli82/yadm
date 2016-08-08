package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Queue;

import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.DimensionTemplate;
import de.alaoli.games.minecraft.mods.yadm.util.TeleportUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
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
		return "";
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
		
		if( YADM.proxy.getDimensionManager().exists( name ) )
		{
			sender.addChatMessage( new ChatComponentText( "Dimension '" + name + "' already exists." ) );
			return;
		}
		if( !YADM.proxy.getTemplateManager().exists( templateName ) ) 
		{
			sender.addChatMessage( new ChatComponentText( "Template '" + templateName + "' doesn't exists." ) );
			return;
		}
		DimensionTemplate template = (DimensionTemplate) YADM.proxy.getTemplateManager().get( templateName );
		
		try
		{
			Dimension dimension = YADM.proxy.getDimensionManager().create( name, template );	
			
			YADM.proxy.registerDimension( dimension );
			
			if( Config.Dimension.teleportOnCreate )
			{
				EntityPlayerMP player = (EntityPlayerMP) sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName());

				Coordinate coordinate = new Coordinate( 
					0,
					150,
					0
				);
				if( !TeleportUtil.teleport( player, dimension, coordinate ) )
				{
					/**
					 * @TODO more infos?
					 */
					sender.addChatMessage( new ChatComponentText( "Can't teleport to dimension" ) );
				}
			}			
		}
		catch( RuntimeException e )
		{
			sender.addChatMessage( new ChatComponentText( e.getMessage() ) );
		}	
	}
}
