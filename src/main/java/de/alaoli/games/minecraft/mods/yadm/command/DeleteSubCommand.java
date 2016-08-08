package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Queue;

import de.alaoli.games.minecraft.mods.yadm.YADM;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

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
		/**
		 * @TODO Teleport player to spawn
		 */
		YADM.proxy.getDimensionManager().delete( name );
		sender.addChatMessage( new ChatComponentText( "Dimension '" + name + "' removed." ) );
	}

}
