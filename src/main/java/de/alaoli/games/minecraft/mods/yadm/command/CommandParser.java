package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.Player;
import de.alaoli.games.minecraft.mods.yadm.data.Template;
import de.alaoli.games.minecraft.mods.yadm.manager.PlayerManager;
import de.alaoli.games.minecraft.mods.yadm.manager.TemplateManager;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;

public class CommandParser 
{
	final private static String DEFAULT_GROUPNAME = "default";
	
	private Queue<String> args;
	private ICommandSender sender;
	
	public CommandParser( ICommandSender sender, String[] args )
	{
		this.sender = sender;
		this.args = new LinkedList<String>( Arrays.asList( args ) );
	}

	/**********************************************************************
	 * Method - Sender 
	 **********************************************************************/
	
	public ICommandSender getSender()
	{
		return this.sender;
	}
	
	/**********************************************************************
	 * Method - Arguments 
	 **********************************************************************/
	
	public void add( String arg )
	{
		this.args.add( arg );
	}
	
	public boolean isEmpty()
	{
		return args.isEmpty();
	}
	
	public int size()
	{
		return this.args.size();
	}
	
	public String next()
	{
		return this.args.remove();
	}
	
	/********************************************************************************
	 * Common
	 ********************************************************************************/
	
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
		
	/**********************************************************************
	 * Method - Parser
	 **********************************************************************/
	
	public String[] parseGroupAndName() throws CommandParserException
	{
		if( this.isEmpty() )
		{
			throw new CommandParserException( "Missing [<group>]:<name> argument." );
		}
		String name = this.next();
		
		return this.parseGroupAndName( name );
	}
	
	public String[] parseGroupAndName( String name ) throws CommandParserException
	{
		String[] groupAndName;
		
		//Get player name
		if( name.contains( "@p" ) )
		{
			EntityPlayer player = PlayerSelector.matchOnePlayer( this.sender, "@p" );
			name = name.replaceAll( "@p", player.getDisplayName() );
		}
		
		if( name.contains( ":" ) )
		{
			groupAndName =  name.split( ":" );
		}
		else
		{
			groupAndName = new String[2];
			
			groupAndName[0] = DEFAULT_GROUPNAME;
			groupAndName[1] = name;
		}
		//Values set?
		if( ( groupAndName[0] == null  ) || 
			( groupAndName[1] == null ) )
		{
			throw new CommandParserException( "Invalid [<group>]:<name> argument." );
		}
		//Int not allowed
		if( ( isInt( groupAndName[0] ) ) || 
			( isInt( groupAndName[1] ) ) )
		{
			throw new CommandParserException( "Invalid [<group>]:<name> argument. Numbers aren't allowed as group or name" );
		}
		return groupAndName;
	}
	
	public Template parseTemplate()throws CommandParserException
	{	
		String[] groupAndName = this.parseGroupAndName();

		//Template exists?
		if( !TemplateManager.INSTANCE.exists( groupAndName[0], groupAndName[1] ) ) 
		{
			throw new CommandParserException( "Template '" + groupAndName[0] + ":" + groupAndName[1] + "' doesn't exists." );
		}
		return (Template) TemplateManager.INSTANCE.get( groupAndName[0], groupAndName[1] );
	}
	
	public Dimension parseDimension() throws CommandParserException
	{
		return this.parseDimension( false );
	}
	
	public Dimension parseDimension( boolean includeVanillaDimensions ) throws CommandParserException
	{
		Dimension dimension;
		String value = this.next();
		
		if( isInt( value ) )
		{
			int id = Integer.valueOf( value );
			
			if( YADimensionManager.INSTANCE.exists( id ) )
			{
				dimension = YADimensionManager.INSTANCE.get( id );
			}
			else
			{
				if( ( includeVanillaDimensions ) && 
					( DimensionManager.isDimensionRegistered( id ) ) )
				{
					dimension = new Dimension( id, null, null );
					dimension.setRegistered( true );
				}
				else
				{
					throw new CommandParserException( "Dimension '" + id + "' doesn't exists." );
				}
			}
		}
		else
		{
			String[] groupAndName = this.parseGroupAndName( value );
			
			if( !YADimensionManager.INSTANCE.exists( groupAndName[0], groupAndName[1] ) )
			{
				throw new CommandParserException( "Dimension '" + groupAndName[0] + ":" + groupAndName[1] + "' doesn't exists." );
			}
			dimension = YADimensionManager.INSTANCE.get( groupAndName[0], groupAndName[1] );
		}
		
		if( dimension == null )
		{
			throw new CommandParserException( "Couldn't find Dimension '" + value + "'." );
		}
		return dimension;		
	}
	
	public Dimension parseAndCreateDimension() throws CommandParserException
	{
		Player owner = null;
		Template template = this.parseTemplate();
		String[] groupAndName = this.parseGroupAndName();

		if( YADimensionManager.INSTANCE.exists( groupAndName[0], groupAndName[1] ) )
		{
			throw new CommandParserException( "Dimension '" + groupAndName[0] + ":" + groupAndName[1] + "' already exists." );
		}
		Dimension dimension = YADimensionManager.INSTANCE.create( groupAndName[0], groupAndName[1], template );
		
		if( dimension == null )
		{
			throw new CommandParserException( "Couldn't create Dimension '" + groupAndName[0] + ":" + groupAndName[1] + "'." );
		}
		
		try
		{
			 owner = this.parsePlayer();
		}
		catch( CommandParserException e )
		{
			//Ignore because optional
		}
		
		if( ( owner == null ) && 
			( this.senderIsEntityPlayer() ) )
		{
			owner = (Player) PlayerManager.INSTANCE.get( this.getSenderAsEntityPlayer().getUniqueID() );
		}
		dimension.setOwner( owner );
		
		return dimension;
	}
	
	public Coordinate parseCoordinate() throws CommandParserException
	{
		if( this.size() < 3 )
		{
			throw new CommandParserException( "Missing coordinate argument." );
		}
		String x = this.next();
		String y = this.next();
		String z = this.next();
		
		if( ( !isInt( x ) ) || ( !isInt( y ) ) || ( !isInt( z ) ) )
		{
			throw new CommandParserException( "Invalid coordinate argument." );
		}
		return new Coordinate( Integer.valueOf( x ),Integer.valueOf( y ),Integer.valueOf( z ) );
	}
	
	public Player parsePlayer() throws CommandParserException
	{
		if( this.isEmpty() )
		{
			throw new CommandParserException( "Missing <player> argument." );
		}
		String name = this.next();
		
		if( !PlayerManager.INSTANCE.exists( name ) ) { throw new CommandParserException( "Couldn't find player." ); }
		
		return (Player)PlayerManager.INSTANCE.get( name );
	}

	public boolean senderIsEntityPlayer()
	{
		return sender instanceof EntityPlayer;
	}
	
	public EntityPlayer getSenderAsEntityPlayer()
	{
		if( !this.senderIsEntityPlayer() ) throw new CommandException( "Command sender isn't a player." );
		
		return (EntityPlayer)this.sender;
	}
	
	public EntityPlayer getEntityPlayer( Player player )
	{
		EntityPlayer result = PlayerSelector.matchOnePlayer( this.sender, player.getManageableName() );
		
		if( result == null ) new CommandParserException( "Couldn't find player entity." );
		
		return result;
	}
}
