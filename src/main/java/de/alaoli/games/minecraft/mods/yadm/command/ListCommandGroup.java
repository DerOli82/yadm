package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.command.list.ListDimensionCommand;
import de.alaoli.games.minecraft.mods.yadm.command.list.ListProviderCommand;
import de.alaoli.games.minecraft.mods.yadm.command.list.ListTypeCommand;

public class ListCommandGroup extends CommandGroup
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public ListCommandGroup( Command parent )
	{
		super( parent );
		
		this.add( new ListDimensionCommand( this ) );
		this.add( new ListProviderCommand( this ) );
		this.add( new ListTypeCommand( this ) );
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
		return "list";
	}
	
	@Override
	public void processCommand( CommandParser command )
	{
		if( command.isEmpty() )
		{
			command.add( "dimension" );
		}
		super.processCommand( command );
	}	
}
