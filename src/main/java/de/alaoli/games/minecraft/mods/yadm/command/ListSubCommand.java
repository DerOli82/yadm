package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;

import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.DataObject;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldProvider;

public class ListSubCommand implements SubCommand
{
	public static final ListSubCommand instance = new ListSubCommand();
	
	private ListSubCommand() {}
	
	@Override
	public String getCommandName()
	{
		return "list";
	}
	
	@Override
	public int getRequiredPermissionLevel() 
	{
		return 4;
	}
	
	@Override
	public String getCommandUsage( ICommandSender sender )
	{
		return "Usage: /" + DefaultCommand.COMMAND + " dimensions|providers|types";
	}

	@Override
	public void processCommand( ICommandSender sender, Queue<String> args ) 
	{
		//Usage
		if( args.isEmpty() )
		{
			sender.addChatMessage( new ChatComponentText( this.getCommandUsage( sender ) ) );
			return;
		}
		String cmd = args.remove().toLowerCase();
		
		switch( cmd )
		{
			case "dimensions" :
				StringBuilder msg;
				Dimension dimension;
				
				sender.addChatMessage( new ChatComponentText( "Registered Dimensions:" ) );
				
				for( Entry<String, DataObject> entry : YADM.proxy.getDimensionManager().getAll() )
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
				break;
				
			case "providers" :
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
				break;
				
			case "types" :
				sender.addChatMessage( new ChatComponentText( "Choosable types:" ) );
				
				for( String type : WorldBuilder.instance.getWorldTypes().keySet() )
				{
					msg = new StringBuilder()
						.append( " - '" )
						.append( type )
						.append( "'" );
					sender.addChatMessage( new ChatComponentText( msg.toString() ) );
				}
				break;
		
			default:
				sender.addChatMessage( new ChatComponentText( this.getCommandName() ) );
				break;
		}
		
	}

}
