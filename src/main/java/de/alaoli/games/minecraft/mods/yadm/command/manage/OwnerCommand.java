package de.alaoli.games.minecraft.mods.yadm.command.manage;

import de.alaoli.games.minecraft.mods.yadm.command.Command;
import de.alaoli.games.minecraft.mods.yadm.command.CommandParser;

public class OwnerCommand extends Command
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public OwnerCommand( Command parent ) 
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
		return "owner";
	}

	@Override
	public void processCommand( CommandParser command )
	{
		String cmd = command.next();
		
		switch( cmd )
		{
			case "set" :
				break;
				
			default:
				break;
		}
	}
}