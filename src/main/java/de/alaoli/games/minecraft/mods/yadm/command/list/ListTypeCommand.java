package de.alaoli.games.minecraft.mods.yadm.command.list;

import de.alaoli.games.minecraft.mods.yadm.command.Command;
import de.alaoli.games.minecraft.mods.yadm.command.CommandException;
import de.alaoli.games.minecraft.mods.yadm.command.Permission;
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
		
		for( String type : WorldBuilder.instance.getWorldTypes().keySet() )
		{
			msg = new StringBuilder()
				.append( " - '" )
				.append( type )
				.append( "'" );
			args.sender.addChatMessage( new ChatComponentText( msg.toString() ) );
		}
	}
	
}