package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Queue;
import java.util.Map.Entry;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.Manageable;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ListDimensionCommand extends Command
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public ListDimensionCommand( Command parent ) 
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
		return "dimension";
	}

	@Override
	public void processCommand( ICommandSender sender, Queue<String> args ) 
	{
		StringBuilder msg;
		Dimension dimension;
		
		sender.addChatMessage( new ChatComponentText( "Registered Dimensions:" ) );
		
		for( Entry<String, Manageable> entry : YADimensionManager.instance.getAll() )
		{
			dimension = (Dimension)entry.getValue();
			msg = new StringBuilder()
				.append( " - '" )
				.append( dimension.getName() )
				.append( "' with ID '" )
				.append( dimension.getId() )
				.append( "'" );
			sender.addChatMessage( new ChatComponentText( msg.toString() ) );
		}
	}
	
}