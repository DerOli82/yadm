package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Queue;

import net.minecraft.command.ICommandSender;

public interface SubCommand 
{
	public String getCommandName();
	
	public String getCommandUsage( ICommandSender sender );
	
	public int getRequiredPermissionLevel();
	
	public void processCommand( ICommandSender sender, Queue<String> args );
}
