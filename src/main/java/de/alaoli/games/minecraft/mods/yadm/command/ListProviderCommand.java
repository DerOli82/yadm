package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Queue;
import java.util.Map.Entry;

import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.command.ICommandSender;
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
	public void processCommand( ICommandSender sender, Queue<String> args ) 
	{
		StringBuilder msg;
		sender.addChatMessage( new ChatComponentText( "Choosable providers:" ) );
		
		for( Entry<Integer, Class<? extends WorldProvider>> entry : WorldBuilder.instance.getWorldProviders().entrySet() )
		{
			switch( entry.getKey() )
			{
				case -1:
					//Nether alias
					sender.addChatMessage( new ChatComponentText( " - 'nether'" ) );
					break;
					
				case 0:
					//Overworld alias
					sender.addChatMessage( new ChatComponentText( " - 'overworld'" ) );
					break;
					
				case 1:
					//End alias
					sender.addChatMessage( new ChatComponentText( " - 'end'" ) );
					break;
				
				default:
					//Don't list YADM provider
					if( !entry.getValue().getName().contains( "YADM" ) )
					{
						msg = new StringBuilder()
							.append( " - '" )
							.append( entry.getValue().getName() )
							.append( "'" );
						sender.addChatMessage( new ChatComponentText( msg.toString() ) );
					}
					break;
			}
		}
	}
	
}