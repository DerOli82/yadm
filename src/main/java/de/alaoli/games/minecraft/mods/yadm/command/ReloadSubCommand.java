package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Queue;

import de.alaoli.games.minecraft.mods.yadm.manager.TemplateManager;
import net.minecraft.command.ICommandSender;

public class ReloadSubCommand implements SubCommand
{
	public static final ReloadSubCommand instance = new ReloadSubCommand();
	
	private ReloadSubCommand() {}
	
	@Override
	public String getCommandName()
	{
		return "reload";
	}
	
	@Override
	public int getRequiredPermissionLevel() 
	{
		return 4;
	}
	
	@Override
	public String getCommandUsage( ICommandSender sender )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processCommand( ICommandSender sender, Queue<String> args ) 
	{
		TemplateManager.instance.reload();
	}

}
