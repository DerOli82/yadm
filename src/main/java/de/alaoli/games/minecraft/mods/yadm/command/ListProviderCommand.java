package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Map.Entry;

import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldProvider;

public class ListProviderCommand extends Command
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public ListProviderCommand( Command parent ) 
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
		return "provider";
	}

	@Override
	public void processCommand( CommandParser command ) 
	{
		StringBuilder msg;
		command.getSender().addChatMessage( new ChatComponentText( "Choosable providers:" ) );
		
		for( Entry<Integer, Class<? extends WorldProvider>> entry : WorldBuilder.instance.getWorldProviders().entrySet() )
		{
			switch( entry.getKey() )
			{
				case -1:
					//Nether alias
					command.getSender().addChatMessage( new ChatComponentText( " - 'nether'" ) );
					break;
					
				case 0:
					//Overworld alias
					command.getSender().addChatMessage( new ChatComponentText( " - 'overworld'" ) );
					break;
					
				case 1:
					//End alias
					command.getSender().addChatMessage( new ChatComponentText( " - 'end'" ) );
					break;
				
				default:
					//Don't list YADM provider
					if( !entry.getValue().getName().contains( "YADM" ) )
					{
						msg = new StringBuilder()
							.append( " - '" )
							.append( entry.getValue().getName() )
							.append( "'" );
						command.getSender().addChatMessage( new ChatComponentText( msg.toString() ) );
					}
					break;
			}
		}
	}
	
}