package de.alaoli.games.minecraft.mods.yadm.command.list;

import java.util.Map.Entry;

import de.alaoli.games.minecraft.mods.yadm.command.Command;
import de.alaoli.games.minecraft.mods.yadm.command.CommandException;
import de.alaoli.games.minecraft.mods.yadm.command.Permission;
import de.alaoli.games.minecraft.mods.yadm.command.Arguments;
import de.alaoli.games.minecraft.mods.yadm.world.ListOptions;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldProvider;

public class ListProviderCommand extends Command
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	protected static final ListOptions worlds = WorldBuilder.INSTANCE;
	
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
	public Permission requiredPermission()
	{
		return Permission.OPERATOR;
	}
	
	@Override
	public void processCommand( Arguments args ) 
	{
		//Check permission
		if( !this.canCommandSenderUseCommand( args ) ) { throw new CommandException( "You're not allowed to perform this command."); }
						
		StringBuilder msg;
		args.sender.addChatMessage( new ChatComponentText( "Choosable providers:" ) );
		
		for( Entry<Integer, Class<? extends WorldProvider>> entry : worlds.listWorldProvider() )
		{
			switch( entry.getKey() )
			{
				case -1:
					//Nether alias
					args.sender.addChatMessage( new ChatComponentText( " - 'nether'" ) );
					break;
					
				case 0:
					//Overworld alias
					args.sender.addChatMessage( new ChatComponentText( " - 'overworld'" ) );
					break;
					
				case 1:
					//End alias
					args.sender.addChatMessage( new ChatComponentText( " - 'end'" ) );
					break;
				
				default:
					//Don't list YADM provider
					if( !entry.getValue().getName().contains( "YADM" ) )
					{
						msg = new StringBuilder()
							.append( " - '" )
							.append( entry.getValue().getName() )
							.append( "'" );
						args.sender.addChatMessage( new ChatComponentText( msg.toString() ) );
					}
					break;
			}
		}
	}
	
}