package de.alaoli.games.minecraft.mods.yadm.command.list;

import java.util.Map.Entry;

import de.alaoli.games.minecraft.mods.yadm.command.Command;
import de.alaoli.games.minecraft.mods.yadm.command.Arguments;
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
	public String getCommandName() 
	{
		return "provider";
	}

	@Override
	public void processCommand( Arguments command ) 
	{
		StringBuilder msg;
		command.sender.addChatMessage( new ChatComponentText( "Choosable providers:" ) );
		
		for( Entry<Integer, Class<? extends WorldProvider>> entry : WorldBuilder.instance.getWorldProviders().entrySet() )
		{
			switch( entry.getKey() )
			{
				case -1:
					//Nether alias
					command.sender.addChatMessage( new ChatComponentText( " - 'nether'" ) );
					break;
					
				case 0:
					//Overworld alias
					command.sender.addChatMessage( new ChatComponentText( " - 'overworld'" ) );
					break;
					
				case 1:
					//End alias
					command.sender.addChatMessage( new ChatComponentText( " - 'end'" ) );
					break;
				
				default:
					//Don't list YADM provider
					if( !entry.getValue().getName().contains( "YADM" ) )
					{
						msg = new StringBuilder()
							.append( " - '" )
							.append( entry.getValue().getName() )
							.append( "'" );
						command.sender.addChatMessage( new ChatComponentText( msg.toString() ) );
					}
					break;
			}
		}
	}
	
}