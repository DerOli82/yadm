package de.alaoli.games.minecraft.mods.yadm.command.list;

import java.util.Map.Entry;

import de.alaoli.games.minecraft.mods.yadm.command.Command;
import de.alaoli.games.minecraft.mods.yadm.command.Arguments;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.Manageable;
import de.alaoli.games.minecraft.mods.yadm.manager.ManageableGroup;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
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
	public String getCommandName() 
	{
		return "dimension";
	}

	@Override
	public void processCommand( Arguments command )
	{
		StringBuilder msg;
		Dimension dimension;
		
		command.sender.addChatMessage( new ChatComponentText( "Registered Dimensions:" ) );
		
		for( Entry<String, Manageable> groupEntry : YADimensionManager.INSTANCE.getAll() )
		{	
			command.sender.addChatMessage( new ChatComponentText( " - " + groupEntry.getValue().getManageableGroupName() + ":" ) );
			
			for( Entry<String, Manageable> dimensionEntry : ((ManageableGroup)groupEntry.getValue()).getAll() )
			{
				dimension = (Dimension)dimensionEntry.getValue();
				msg = new StringBuilder()
					.append( "    - '" )
					.append( dimension.getManageableName() )
					.append( "' with ID '" )
					.append( dimension.getId() )
					.append( "'" );
				command.sender.addChatMessage( new ChatComponentText( msg.toString() ) );
			}
		}
	}
	
}