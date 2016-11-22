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
	public String getCommandName()
	{
		return "list";
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
		
		if( args.isEmpty() )
		{
			args.add( "dimension" );
		}
		super.processCommand( args );
	}	
}
