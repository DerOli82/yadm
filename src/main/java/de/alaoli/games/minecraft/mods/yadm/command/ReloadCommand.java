package de.alaoli.games.minecraft.mods.yadm.command;

import java.io.IOException;

import de.alaoli.games.minecraft.mods.yadm.data.DataException;
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
		try {
			TemplateManager.INSTANCE.load();
		} catch (DataException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
