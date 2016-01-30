package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.util.TeleportUtil;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class ManageCommand implements ICommand
{
	/********************************************************************************
	 * Constants
	 ********************************************************************************/
	
	public static final String COMMAND = "yadm";
	
	public static final String SUBCOMMAND_HELP		= "help";
	public static final String SUBCOMMAND_PROVIDERS	= "providers";
	public static final String SUBCOMMAND_TYPES		= "types";
	public static final String SUBCOMMAND_TP		= "tp";
	public static final String SUBCOMMAND_CREATE	= "create";
	public static final String SUBCOMMAND_REMOVE	= "remove";
	
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	List<String> aliases;
	
	/********************************************************************************
	 * Methods - Constructor
	 ********************************************************************************/
	
	public ManageCommand()
	{
		this.aliases = new ArrayList<String>();
		this.aliases.add( COMMAND );
	}
	
	/********************************************************************************
	 * Interface - ICommand
	 ********************************************************************************/
	
	@Override
	public int compareTo( Object obj )
	{
		return 0;
	}

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
		result.append("manage");
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
			this.processCommandHelp( sender, argQueue );
			return;
		}
		String cmd = argQueue.remove().toLowerCase();
		
		switch( cmd )
		{
			case SUBCOMMAND_HELP:
				this.processCommandHelp( sender, argQueue );
			break;

			case SUBCOMMAND_TP:
				this.processCommandTP( sender, argQueue );
				break;
				
			case SUBCOMMAND_CREATE:
				this.processCommandCreate( sender, argQueue );
				break;
				
			default:
				this.processCommandHelp( sender, argQueue );
				break;
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
	   
	   list.add( SUBCOMMAND_HELP );
	   list.add( SUBCOMMAND_PROVIDERS );
	   list.add( SUBCOMMAND_TYPES );
	   list.add( SUBCOMMAND_TP );
	   list.add( SUBCOMMAND_CREATE );
	   list.add( SUBCOMMAND_REMOVE );
		
	   return list;
	}

	@Override
	public boolean isUsernameIndex(String[] list, int p_82358_2_) {
		// TODO Auto-generated method stub
		return false;
	}

	/********************************************************************************
	 * Methods - Process Commands
	 ********************************************************************************/
	
	/**
	 * Command Usage
	 * 
	 * @param ICommandSender
	 * @param String[]
	 */
	private void processCommandHelp( ICommandSender sender, Queue<String> argQueue )
	{
		sender.addChatMessage( new ChatComponentText( this.getCommandUsage( sender ) ) );
	}

	/**
	 * Teleport Command
	 * 
	 * @param ICommandSender
	 * @param String[]
	 */	
	private void processCommandTP( ICommandSender sender, Queue<String> argQueue )
	{
		//Usage
		if( argQueue.isEmpty() )
		{
			sender.addChatMessage( new ChatComponentText( "Usage: /" + COMMAND + " tp <dimensionName> [player] [x y z]" ) );
			return;
		}
		int dimId, x, y, z;
		Coordinate coordinate;
		EntityPlayerMP player;
		
		YADimensionManager dm = YADM.proxy.getDimensionManager();
		String dimensionName = argQueue.remove();
		/*
		//Does Dimension exists
		if( !dm.existsDimension( dimensionName ) )
		{
			sender.addChatMessage( new ChatComponentText( "World name doesn't exists. Note world names are case-sensitive" ) );
			return;
		}
		dimId = dm.getDimensionByName( dimensionName ).getId();
		
		//Optional Player
		if( argQueue.isEmpty() )
		{
			player = (EntityPlayerMP) sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName());
		}
		else
		{
			player = (EntityPlayerMP) sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName());
		}
		
		//Optional Coordinate
		if( argQueue.isEmpty() )
		{
			x = (int) player.posX;
			y = (int) player.posY;
			z = (int) player.posZ;
		}
		else
		{
			x = (int) player.posX;
			y = (int) player.posY;
			z = (int) player.posZ;
		}
		coordinate = new Coordinate( dimId, x, y, z );
		
		TeleportUtil.teleport( player, coordinate );
			
		//Optional teleport coordinate
		/*
		if( argQueue.size() < 3 )
		{
			sender.addChatMessage( new ChatComponentText( "keine coordis" ) );
		}
		else
		{
			
		}
		try
		{
			
			x = Integer.parseInt( argQueue.remove() );
			y = Integer.parseInt( argQueue.remove() );
			z = Integer.parseInt( argQueue.remove() );
			
			
		}
		catch( NumberFormatException e )
		{
			sender.addChatMessage( new ChatComponentText( "Can't parse coordinate." ) );
		}
		*/
	}
	
	/**
	 * Command Usage
	 * 
	 * @param ICommandSender
	 * @param String[]
	 */
	private void processCommandCreate( ICommandSender sender, Queue<String> argQueue )
	{
		//Usage
		if( argQueue.size() < 2 )
		{
			sender.addChatMessage( new ChatComponentText( "Usage: /" + COMMAND + " create <dimensionName> <dimensionPattern>" ) );
			return;
		}
		String name = argQueue.remove();
		String pattern = argQueue.remove();
		
		try
		{
			Dimension dimension = (Dimension) YADM.proxy.getDimensionManager().create( name );	
			dimension.setPatternName( pattern );
			
			YADM.proxy.getDimensionManager().register( dimension );
			YADM.proxy.getDimensionManager().init( dimension );
			
			if( Config.Dimensions.teleportOnCreate )
			{
				EntityPlayerMP player = (EntityPlayerMP) sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName());
				Coordinate coordinate = new Coordinate( 
					dimension.getId(),
					(int) player.posX,
					(int) player.posY,
					(int) player.posZ
				);
				TeleportUtil.teleport( player, coordinate );
			}			
		}
		catch( RuntimeException e )
		{
			sender.addChatMessage( new ChatComponentText( e.getMessage() ) );
		}
	}	
	

}
