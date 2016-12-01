package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.command.manage.WhitelistCommand;
import de.alaoli.games.minecraft.mods.yadm.command.manage.OwnerCommand;
import de.alaoli.games.minecraft.mods.yadm.command.manage.TravelCommand;

public class ManageCommandGroup extends CommandGroup
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public ManageCommandGroup( Command parent )
	{
		super( parent );
		
		this.add( new OwnerCommand( this ) );
		this.add( new WhitelistCommand( this ) );
		this.add( new TravelCommand( this ) );
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/
	

	
	@Override
	public String getCommandName()
	{
		return "manage";
	}

	@Override
	public Permission requiredPermission()
	{
		return Permission.OWNER;
	}
	
	@Override
	public void processCommand(Arguments args) 
	{
		//Check permission
		if( !this.canCommandSenderUseCommand( args ) ) { throw new CommandException( "You're not allowed to perform this command."); }
		
		super.processCommand( args );
	}
}
