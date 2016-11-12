package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.command.manage.WhitelistCommand;
import de.alaoli.games.minecraft.mods.yadm.command.manage.OwnerCommand;

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
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/
	
	@Override
	public String getCommandName()
	{
		return "manage";
	}
}
