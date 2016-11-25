package de.alaoli.games.minecraft.mods.yadm.command.list;

import de.alaoli.games.minecraft.mods.yadm.command.Command;
import de.alaoli.games.minecraft.mods.yadm.command.CommandException;
import de.alaoli.games.minecraft.mods.yadm.command.Permission;

import java.util.Map.Entry;

import de.alaoli.games.minecraft.mods.yadm.command.Arguments;
import de.alaoli.games.minecraft.mods.yadm.world.ListOptions;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldType;

public class ListTypeCommand extends Command
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	protected static final ListOptions worlds = WorldBuilder.INSTANCE;
		
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
		args.sender.addChatMessage( new ChatComponentText( "Choosable types:" ) );
		
		for( Entry<String, WorldType> entry : worlds.listWorldType() )
		{
			msg = new StringBuilder()
				.append( " - '" )
				.append( entry.getKey() )
				.append( "'" );
			args.sender.addChatMessage( new ChatComponentText( msg.toString() ) );
		}
	}
	
}