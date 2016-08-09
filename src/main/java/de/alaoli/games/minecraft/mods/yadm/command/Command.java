package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public abstract class Command implements ICommand
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	private Command parent;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public Command( Command parent )
	{
		this.parent = parent;
	}
	
	public boolean hasParent()
	{
		if( this.parent == null )
		{
			return false;
		}
		return true;
	}
	
	public Command getParent()
	{
		return this.parent;
	}
	
	public int getRequiredPermissionLevel() 
	{
		return 4;
	}

	public abstract void processCommand( ICommandSender sender, Queue<String> args );
	
	/********************************************************************************
	 * Interface - ICommand
	 ********************************************************************************/
	
	@Override
	public int compareTo( Object obj ) 
	{
		return  this.getCommandName().compareTo( ((ICommand)obj).getCommandName() );
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender )
	{
		return sender.canCommandSenderUseCommand( this.getRequiredPermissionLevel(), this.getCommandName() );
	}
	
	@Override
	public boolean isUsernameIndex( String[] p_82358_1_, int p_82358_2_ )
	{
		return false;
	}
    
	@Override
	public String getCommandUsage( ICommandSender sender ) 
	{
		String usage = this.getCommandName();
		
		if( this.hasParent() )
		{
			usage = this.getParent().getCommandUsage( sender ) + " " + usage;
		}
		return usage;
	}
	
	@Override
	public List getCommandAliases() 
	{
		List<String> list = new ArrayList<String>();
		list.add( this.getCommandName() );
		return null;
	}	
	
	@Override
	public List addTabCompletionOptions( ICommandSender sender, String[] args )
	{
		List<String> list = new ArrayList<String>();
		list.add( this.getCommandName() );
		
		return list;
	}
	
	@Override
	public void processCommand( ICommandSender sender, String[] args )
	{
		this.processCommand( sender, new LinkedList<String>( Arrays.asList( args ) ) );
	}
}
