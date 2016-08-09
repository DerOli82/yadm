package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Queue;

import de.alaoli.games.minecraft.mods.yadm.manager.TemplateManager;
import net.minecraft.command.ICommandSender;

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
	public void processCommand( ICommandSender sender, Queue<String> args ) 
	{
		TemplateManager.instance.reload();
	}

}
