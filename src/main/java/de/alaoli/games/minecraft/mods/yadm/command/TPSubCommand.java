package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Queue;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.util.TeleportUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class TPSubCommand implements SubCommand
{
	public static final TPSubCommand instance = new TPSubCommand();
	
	private TPSubCommand() {}
	
	@Override
	public String getCommandName()
	{
		return "tp";
	}
	
	@Override
	public int getRequiredPermissionLevel() 
	{
		return 4;
	}
	
	@Override
	public String getCommandUsage( ICommandSender sender )
	{
		return "/" +  " tp <dimensionName> [x y z] [player]";
	}

	@Override
	public void processCommand( ICommandSender sender, Queue<String> args ) 
	{
		//Usage
		if( args.isEmpty() )
		{
			sender.addChatMessage( new ChatComponentText( "Usage: " + this.getCommandUsage( sender ) ) );
			return;
		}
		String dimensionName = args.remove();

		if( !YADM.proxy.getDimensionManager().exists( dimensionName ) )
		{
			sender.addChatMessage( new ChatComponentText( "Dimension '" + dimensionName + "' doesn't exists." ) );
			return;
		}
		EntityPlayerMP player = (EntityPlayerMP) sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName());
		
		YADimensionManager dm = YADM.proxy.getDimensionManager();
		
		Dimension dimension = (Dimension) dm.get( dimensionName );
		
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
