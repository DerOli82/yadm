package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.List;
import java.util.Queue;

import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public abstract class Command
{
	public int getRequiredPermissionLevel() 
	{
		return 4;
	}
    
	public boolean IsInt( String str )
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
	
	public Dimension parseDimension( ICommandSender sender, Queue<String> args  )
	{
		if( args.isEmpty() ) { return null; }
		String name = args.remove();
		
		if( this.IsInt( name ) )
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
	
	public Coordinate parseCoordinate( ICommandSender sender, Queue<String> args  )
	{
		if( args.size() < 3 ) { return null; }
		
		String x = args.remove();
		String y = args.remove();
		String z = args.remove();
		
		if( ( this.IsInt( x ) ) && ( this.IsInt( y ) ) && ( this.IsInt( z ) ) )
		{
			return new Coordinate( Integer.valueOf( x ),Integer.valueOf( y ),Integer.valueOf( z ) );
		}
		return null;
		
	}
	
	public EntityPlayer parsePlayer( ICommandSender sender, Queue<String> args  )
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
