package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.util.TeleportUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class DeleteSubCommand implements SubCommand
{
	public static final DeleteSubCommand instance = new DeleteSubCommand();
	
	private DeleteSubCommand() {}
	
	@Override
	public String getCommandName()
	{
		return "delete";
	}
	
	@Override
	public int getRequiredPermissionLevel() 
	{
		return 4;
	}
	
	@Override
	public String getCommandUsage( ICommandSender sender )
	{
		return "Usage: /" + DefaultCommand.COMMAND + " delete <dimensionName>";
	}

	@Override
	public void processCommand( ICommandSender sender, Queue<String> args )
	{
		if( args.isEmpty() )
		{
			sender.addChatMessage( new ChatComponentText( this.getCommandUsage( sender ) ) );
			return;
		}
		String name = args.remove();
		
		if( !YADM.proxy.getDimensionManager().exists( name ) )
		{
			sender.addChatMessage( new ChatComponentText( "Dimension '" + name + "' doesn't exists." ) );
			return;
		}
		Dimension dimension = (Dimension) YADM.proxy.getDimensionManager().get( name );
		World world = DimensionManager.getWorld( dimension.getId() );
		List<EntityPlayerMP> players = new ArrayList<EntityPlayerMP>((List<EntityPlayerMP>)world.playerEntities);
		
		//Teleport all players out
		for( EntityPlayerMP player : players )
		{
			TeleportUtil.emergencyTeleport( player );
		}
		YADM.proxy.getDimensionManager().delete( name );
		sender.addChatMessage( new ChatComponentText( "Dimension '" + name + "' removed." ) );
	}

}
