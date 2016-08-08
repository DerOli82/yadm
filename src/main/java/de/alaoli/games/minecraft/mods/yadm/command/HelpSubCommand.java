package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Queue;

import net.minecraft.command.ICommandSender;

public class HelpSubCommand implements SubCommand
{
	public static final HelpSubCommand instance = new HelpSubCommand();
	
	private HelpSubCommand() {}
	
	@Override
	public String getCommandName()
	{
		return "help";
	}
	
	@Override
	public int getRequiredPermissionLevel() 
	{
		return 0;
	}
	
	@Override
	public String getCommandUsage( ICommandSender sender )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processCommand(ICommandSender sender, Queue<String> args) {
		// TODO Auto-generated method stub
		
	}

}
