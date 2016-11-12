package de.alaoli.games.minecraft.mods.yadm.command.list;

import de.alaoli.games.minecraft.mods.yadm.command.Command;
import de.alaoli.games.minecraft.mods.yadm.command.Arguments;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
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
	public String getCommandName() 
	{
		return "type";
	}

	@Override
	public void processCommand( Arguments command )
	{
		StringBuilder msg;
		command.sender.addChatMessage( new ChatComponentText( "Choosable types:" ) );
		
		for( String type : WorldBuilder.instance.getWorldTypes().keySet() )
		{
			msg = new StringBuilder()
				.append( " - '" )
				.append( type )
				.append( "'" );
			command.sender.addChatMessage( new ChatComponentText( msg.toString() ) );
		}
	}
	
}