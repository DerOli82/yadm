package de.alaoli.games.minecraft.mods.yadm.command;

import de.alaoli.games.minecraft.mods.yadm.manager.TemplateManager;

public class ReloadCommand extends Command 
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public ReloadCommand( Command parent ) 
	{
		super(parent);
	}

	/********************************************************************************
	 * Override - ICommand, Command
	 ********************************************************************************/
	
	@Override
	public int getRequiredPermissionLevel() 
	{
		return 2;
	}
	
	@Override
	public String getCommandName() 
	{
		return "reload";
	}

	@Override
	public void processCommand( CommandParser command ) 
	{
		TemplateManager.instance.reload();
	}

}
