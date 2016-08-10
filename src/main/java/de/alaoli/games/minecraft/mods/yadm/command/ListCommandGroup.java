package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Queue;

import net.minecraft.command.ICommandSender;

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
	public void processCommand( ICommandSender sender, Queue<String> args )
	{
		if( args.isEmpty() )
		{
			args.add( "dimension" );
		}
		super.processCommand( sender, args );
	}	
}
