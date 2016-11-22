package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public abstract class CommandGroup extends Command 
{
	public CommandGroup( Command parent ) 
	{
		super(parent);
	}

	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private Map<String, Command> commands = new HashMap<String, Command>();
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public void add( Command command )
	{
		if( !this.commands.containsKey( command.getCommandName() ) )
		{
			this.commands.put( command.getCommandName(), command );
		}
	}
	
	public void remove( Command command )
	{
		if( this.commands.containsKey( command.getCommandName() ) )
		{
			this.commands.remove(command.getCommandName() );
		}
	}
	
	public Command get( String command )
	{
		return this.commands.get( command );
	}


	public String getCommandUsageList( ICommandSender sender ) 
	{
		String usage = super.getCommandUsage( sender ) + " ";
		Iterator<String> iterator = this.commands.keySet().iterator();
		
		while( iterator.hasNext() )
		{
			usage += iterator.next();
			
			if( iterator.hasNext() )
			{
				usage += " | ";
			}
		}
		return usage;
	}
	
	@Override
	public void sendUsage( ICommandSender sender )
	{
		sender.addChatMessage( new ChatComponentText( this.getCommandUsageList( sender ) ) );
	}
	
	/********************************************************************************
	 * Interface - ICommand
	 ********************************************************************************/
	
	@Override
	public List addTabCompletionOptions( ICommandSender sender, String[] args )
	{
		List<String> list = new ArrayList<String>();
		
		if( this.commands.containsKey( args[ 0 ] ) )
		{
			Command command = this.commands.get( args[ 0 ] );
			
			if( command instanceof CommandGroup )
			{
				return ((CommandGroup)command).addTabCompletionOptions( sender, ArrayUtils.remove( args, 0 ) );
			}
			return list;
		}
		
		for( String command : this.commands.keySet() )
		{
			if( command.regionMatches( 0, args[ 0 ], 0, args[ 0 ].length() ) )
			{
				list.add( command );
			}
		}
		return list;
	}
	
	@Override
	public void processCommand( Arguments args )
	{
		if( args.isEmpty() )
		{
			this.sendUsage( args.sender );
			return;
		}
		String arg = args.next(); 
		
		if( this.commands.containsKey( arg ) )
		{
			this.commands.get( arg ).processCommand( args );
		}
		else
		{
			this.sendUsage( args.sender );
		}
	}
}
