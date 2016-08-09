package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class DefaultCommand extends Command implements ICommand
{
	/********************************************************************************
	 * Constants
	 ********************************************************************************/
	
	public static final String COMMAND = "yadm";
	
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	public static final DefaultCommand instance = new DefaultCommand();
	
	private static Map<String, SubCommand> subcommands = new HashMap<String, SubCommand>();
	
	private List<String> aliases;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	static
	{
		registerSubCommand( HelpSubCommand.instance );
		registerSubCommand( ListSubCommand.instance );
		registerSubCommand( ReloadSubCommand.instance );
		registerSubCommand( CreateSubCommand.instance );
		registerSubCommand( DeleteSubCommand.instance );
		registerSubCommand( InfoSubCommand.instance );
		registerSubCommand( TPSubCommand.instance );
	}

	private DefaultCommand()
	{
		this.aliases = new ArrayList<String>();
		this.aliases.add( this.getCommandName() );
	}
	
	public static void registerSubCommand( SubCommand subCommand )
	{
		if( !subcommands.containsKey( subCommand ) )
		{
			subcommands.put( subCommand.getCommandName(), subCommand );
		}
	}
	
	@Override
	public int getRequiredPermissionLevel() 
	{
		return 0;
	}
	
	/********************************************************************************
	 * Interface - ICommand
	 ********************************************************************************/


	@Override
	public String getCommandName() 
	{
		return COMMAND;
	}

	@Override
	public String getCommandUsage( ICommandSender sender ) 
	{
		StringBuilder result = new StringBuilder();
		/**
		 * @TODO
		 */
		result.append("todo");
		return result.toString();
	}

	@Override
	public List getCommandAliases() 
	{
		return this.aliases;
	}

	@Override
	public void processCommand( ICommandSender sender, String[] args )
	{
		Queue<String> argQueue = new LinkedList<String>( Arrays.asList( args ) );
		
		if( argQueue.isEmpty() )
		{
			sender.addChatMessage( new ChatComponentText( this.getCommandUsage( sender ) ) );
			return;
		}
		String cmd = argQueue.remove().toLowerCase();
		
		if( DefaultCommand.subcommands.containsKey( cmd ) )
		{
			DefaultCommand.subcommands.get( cmd ).processCommand( sender, argQueue );
		}
		else
		{
			sender.addChatMessage( new ChatComponentText( this.getCommandUsage( sender ) ) );
		}
	}

	@Override
	public boolean canCommandSenderUseCommand( ICommandSender sender ) 
	{
		return true;
	}

	@Override
	public List addTabCompletionOptions( ICommandSender sender, String[] args ) 
	{
	   List<String> list = new ArrayList<String>();
	   
	   list.add( "help" );
	   
	   for( String subcommand : DefaultCommand.subcommands.keySet() )
	   {
		   list.add( subcommand );
	   }
	   return list;
	}

	@Override
	public boolean isUsernameIndex(String[] list, int p_82358_2_) 
	{
		return false;
	}

	@Override
	public int compareTo( Object obj ) 
	{
		return  this.getCommandName().compareTo( ((ICommand)obj).getCommandName() );
	}
}
