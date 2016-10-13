package de.alaoli.games.minecraft.mods.yadm.util;

import java.util.List;
import java.util.Queue;

import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.DimensionManager;

public class CommandUtil 
{
	public static boolean isInt( String str )
	{
	    if( str == null ) { return false; }
	    int length = str.length();
	    if( length == 0 ){ return false; }
	    int i = 0;
	    
	    if( str.charAt(0) == '-' )
	    {
            if( length == 1 ) { return false; }
            i = 1;
	    }
	    for( ; i < length; i++ )
	    {
	        if( !Character.isDigit(str.charAt( i ) ) ) { return false; }
	    }
	    return true;
	}    
	
	public static  Dimension parseDimension( ICommandSender sender, Queue<String> args  )
	{
		if( args.isEmpty() ) { return null; }
		String name = args.remove();
		
		if( isInt( name ) )
		{
			int id = Integer.valueOf( name );
			
			if( !YADimensionManager.instance.exists( id ) )
			{
				sender.addChatMessage( new ChatComponentText( "Dimension '" + id + "' doesn't exists." ) );
				return null;
			}
			return YADimensionManager.instance.get( id );
		}
		else
		{
			if( !YADimensionManager.instance.exists( name ) )
			{
				sender.addChatMessage( new ChatComponentText( "Dimension '" + name + "' doesn't exists." ) );
				return null;
			}
			return (Dimension) YADimensionManager.instance.get( name );
		}
	}
	
	/**
	 * @TODO Do it a better way! 
	 * 
	 * @param sender
	 * @param args
	 * @return
	 */
	public static Dimension parseTeleportDimension( ICommandSender sender, Queue<String> args  )
	{
		if( args.isEmpty() ) { return null; }
		String name = args.remove();
		
		if( isInt( name ) )
		{
			int id = Integer.valueOf( name );
			
			if( !YADimensionManager.instance.exists( id ) )
			{
				if( DimensionManager.isDimensionRegistered( id ) )
				{
					Dimension dimension = new Dimension( id, null );
					dimension.setRegistered( true );
					
					return dimension; 
				}
				else
				{
					sender.addChatMessage( new ChatComponentText( "Dimension '" + id + "' doesn't exists." ) );
					return null;
				}
			}
			return YADimensionManager.instance.get( id );
		}
		else
		{
			if( !YADimensionManager.instance.exists( name ) )
			{
				sender.addChatMessage( new ChatComponentText( "Dimension '" + name + "' doesn't exists." ) );
				return null;
			}
			return (Dimension) YADimensionManager.instance.get( name );
		}
	}
	
	public static  Coordinate parseCoordinate( ICommandSender sender, Queue<String> args  )
	{
		if( args.size() < 3 ) { return null; }
		
		String x = args.remove();
		String y = args.remove();
		String z = args.remove();
		
		if( ( isInt( x ) ) && ( isInt( y ) ) && ( isInt( z ) ) )
		{
			return new Coordinate( Integer.valueOf( x ),Integer.valueOf( y ),Integer.valueOf( z ) );
		}
		return null;
		
	}
	
	public static  EntityPlayer parsePlayer( ICommandSender sender, Queue<String> args  )
	{
		if( args.isEmpty() ) { return null; }
		
		String name = args.remove();
		List<EntityPlayer> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		
		if( players == null ) { return null; }
		
		for( EntityPlayer player : players )
		{
			if( player.getDisplayName().toLowerCase().equals( name.toLowerCase() ) )
			{
				return player;
			}
		}
		return null;
	}
}
