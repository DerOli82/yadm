package de.alaoli.games.minecraft.mods.yadm.command;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.DimensionDummy;
import de.alaoli.games.minecraft.mods.yadm.data.Player;
import de.alaoli.games.minecraft.mods.yadm.data.Template;
import de.alaoli.games.minecraft.mods.yadm.manager.PlayerManager;
import de.alaoli.games.minecraft.mods.yadm.manager.TemplateManager;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.DimensionException;
import de.alaoli.games.minecraft.mods.yadm.manager.player.FindPlayer;
import de.alaoli.games.minecraft.mods.yadm.manager.player.PlayerException;
import de.alaoli.games.minecraft.mods.yadm.manager.template.FindTemplate;
import de.alaoli.games.minecraft.mods.yadm.manager.template.TemplateException;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;

public class Arguments 
{
	protected static final FindPlayer players = PlayerManager.INSTANCE;
	protected static final FindTemplate templates = TemplateManager.INSTANCE; 
	protected static final YADimensionManager dimensions = YADimensionManager.INSTANCE;
	
	private Queue<String> args;
	
	public final ICommandSender sender; 
	public final boolean senderIsEntityPlayer; 
	public final boolean senderIsOP;
	
	public Arguments( ICommandSender sender, String[] args )
	{
		this.sender = sender;
		this.senderIsEntityPlayer = sender instanceof EntityPlayer;
		this.senderIsOP = sender.canCommandSenderUseCommand( 2, "yadm" );
		
		this.args = new LinkedList<String>( Arrays.asList( args ) );
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
		String next = this.args.remove();
		
		//Replace @p with player name
		if( next.contains( "@p" ) )
		{
			EntityPlayer player = PlayerSelector.matchOnePlayer( this.sender, "@p" );
			next = next.replaceAll( "@p", player.getDisplayName() );
		}
		return next;
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

	public Player getSenderAsPlayer() throws PlayerException
	{
		return players.findPlayer( this.sender );
	}
	
	/**********************************************************************
	 * Method - Parser
	 **********************************************************************/
	
	public String[] parseGroupAndName() throws CommandException
	{
		if( this.isEmpty() )
		{
			throw new CommandException( "Missing [<group>]:<name> argument." );
		}
		String name = this.next();
		
		return this.parseGroupAndName( name );
	}
	
	public String[] parseGroupAndName( String name ) throws CommandException
	{
		String[] groupAndName;

		if( name.contains( ":" ) )
		{
			groupAndName =  name.split( ":" );
		}
		else
		{
			groupAndName = new String[2];
			
			groupAndName[0] = "default";
			groupAndName[1] = name;
		}
		//Values set?
		if( ( groupAndName[0] == null  ) || 
			( groupAndName[1] == null ) )
		{
			throw new CommandException( "Invalid [<group>]:<name> argument." );
		}
		//Int not allowed
		if( ( isInt( groupAndName[0] ) ) || 
			( isInt( groupAndName[1] ) ) )
		{
			throw new CommandException( "Invalid [<group>]:<name> argument. Numbers aren't allowed as group or name" );
		}
		return groupAndName;
	}
	
	public Template parseTemplate() throws CommandException, TemplateException
	{	
		String[] groupAndName = this.parseGroupAndName();

		return templates.findTemplate( groupAndName[0], groupAndName[1] );
	}
	
	public Dimension parseDimension() throws CommandException
	{
		return this.parseDimension( false );
	}
	
	public Dimension parseDimension( boolean includeVanillaDimensions ) throws CommandException, DimensionException
	{
		Dimension dimension;
		String value = this.next();
		
		if( isInt( value ) )
		{
			int dimensionId = Integer.valueOf( value );
			
			if( dimensions.existsDimension(dimensionId) )
			{
				dimension = dimensions.findDimension( dimensionId );
			}
			else
			{
				if( ( includeVanillaDimensions ) && 
					( DimensionManager.isDimensionRegistered( dimensionId ) ) )
				{
					dimension = new DimensionDummy( dimensionId );
				}
				else
				{
					throw new CommandException( "Dimension '" + dimensionId + "' doesn't exists." );
				}
			}
		}
		else
		{
			String[] groupAndName = this.parseGroupAndName( value );
			
			dimension = dimensions.findDimension( groupAndName[0], groupAndName[1] );
		}
		if( dimension == null ) { throw new CommandException( "Couldn't find Dimension '" + value + "'." ); }
		
		return dimension;		
	}
	
	public Dimension parseAndCreateDimension() throws CommandException, TemplateException, DimensionException, PlayerException
	{
		Player owner = null;
		Template template = this.parseTemplate();
		String[] groupAndName = this.parseGroupAndName();

		if( dimensions.existsDimension( groupAndName[0], groupAndName[1] ) )
		{
			throw new CommandException( "Dimension '" + groupAndName[0] + ":" + groupAndName[1] + "' already exists." );
		}
		Dimension dimension = dimensions.createDimension( groupAndName[0], groupAndName[1], template );
		
		if( dimension == null )
		{
			throw new CommandException( "Couldn't create Dimension '" + groupAndName[0] + ":" + groupAndName[1] + "'." );
		}
		
		try
		{
			 owner = this.parsePlayer();
			 dimension.setOwner( owner );
		}
		catch( CommandException|PlayerException e )
		{
			//Ignore because optional
		}
		return dimension;
	}
	
	public Coordinate parseCoordinate() throws CommandException
	{
		if( this.size() < 3 )
		{
			throw new CommandException( "Missing coordinate argument." );
		}
		String x = this.next();
		String y = this.next();
		String z = this.next();
		
		if( ( !isInt( x ) ) || ( !isInt( y ) ) || ( !isInt( z ) ) )
		{
			throw new CommandException( "Invalid coordinate argument." );
		}
		return new Coordinate( Integer.valueOf( x ),Integer.valueOf( y ),Integer.valueOf( z ) );
	}
	
	public Player parsePlayer() throws CommandException, PlayerException
	{
		if( this.isEmpty() ) { throw new CommandException( "Missing <player> argument." ); }
		
		return players.findPlayer( this.next() );
	}
}
