package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Queue;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ListTypeCommand extends Command
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public ListTypeCommand( Command parent ) 
	{
		super( parent );
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/
	
	@Override
	public int getRequiredPermissionLevel() 
	{
		return 1;
	}
	
	@Override
	public String getCommandName() 
	{
		return "type";
	}

	@Override
	public void processCommand( ICommandSender sender, Queue<String> args ) 
	{
		StringBuilder msg;
		sender.addChatMessage( new ChatComponentText( "Choosable types:" ) );
		
		for( String type : WorldBuilder.instance.getWorldTypes().keySet() )
		{
			msg = new StringBuilder()
				.append( " - '" )
				.append( type )
				.append( "'" );
			sender.addChatMessage( new ChatComponentText( msg.toString() ) );
		}
	}
	
}