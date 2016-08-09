package de.alaoli.games.minecraft.mods.yadm.command;

import net.minecraft.command.ICommandSender;

public class YADMCommandGroup extends CommandGroup 
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public YADMCommandGroup()
	{
		super( null );
		
		this.add( new ListCommandGroup( this ) );
		this.add( new ReloadCommand( this ) );
		this.add( new CreateCommand( this ) );
		this.add( new DeleteCommand( this ) );
		this.add( new TPCommand( this ) );
		this.add( new InfoCommand( this ) );
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/
	
	@Override
	public int getRequiredPermissionLevel() 
	{
		return -1;
	}
	
	@Override
	public String getCommandName() 
	{
		return "yadm";
	}

	@Override
	public String getCommandUsage( ICommandSender sender ) 
	{
		return "Usage: /" + super.getCommandUsage( sender );
	}
}
