package de.alaoli.games.minecraft.mods.yadm.command.list;

import de.alaoli.games.minecraft.mods.yadm.command.Command;
import de.alaoli.games.minecraft.mods.yadm.command.CommandException;
import de.alaoli.games.minecraft.mods.yadm.command.Permission;

import java.util.Collection;
import de.alaoli.games.minecraft.mods.yadm.command.Arguments;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ChatComponentText;

public class ListEntityCommand extends Command
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public ListEntityCommand( Command parent ) 
	{
		super( parent );
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/
	
	@Override
	public String getCommandName() 
	{
		return "entity";
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
		args.sender.addChatMessage( new ChatComponentText( "Choosable entities:" ) );
		
		for( String name : (Collection<String>)EntityList.stringToClassMapping.keySet() )
		{
			msg = new StringBuilder()
				.append( " - '" )
				.append( name )
				.append( "'" );
			args.sender.addChatMessage( new ChatComponentText( msg.toString() ) );
		}
	}
	
}