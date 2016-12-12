package de.alaoli.games.minecraft.mods.yadm.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.UUID;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.WriterConfig;

import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.data.Player;
import de.alaoli.games.minecraft.mods.yadm.event.TeleportEvent;
import de.alaoli.games.minecraft.mods.yadm.json.JsonFileAdapter;
import de.alaoli.games.minecraft.mods.yadm.manager.player.DimensionTeleport;
import de.alaoli.games.minecraft.mods.yadm.manager.player.FindPlayer;
import de.alaoli.games.minecraft.mods.yadm.manager.player.ManagePlayers;
import de.alaoli.games.minecraft.mods.yadm.manager.player.PlayerException;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportException;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraftforge.common.DimensionManager;

public class PlayerManager extends ManageableGroup implements ManagePlayers, FindPlayer, TeleportPlayer, JsonFileAdapter
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	public static final PlayerManager INSTANCE = new PlayerManager();
	
	private Map<UUID, Player> mapppingId;
	private boolean dirty;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	private PlayerManager() 
	{
		super( "players" );
		
		this.mapppingId = new HashMap<UUID, Player>();
		this.setDirty( false );
	}

	public Manageable get( UUID id )
	{
		//Mapping
		if( this.mapppingId.containsKey( id ) ) { return this.mapppingId.get( id ); }
		
		//Searching
		for( Entry<String, Manageable> entry : this.getAll() )
		{
			if( ( entry.getValue() instanceof Player ) &&
				( ((Player)entry.getValue()).getId().equals( id ) ) )
			{
				this.mapppingId.put( ((Player)entry.getValue()).getId(), (Player)entry.getValue() );
				
				return entry.getValue();
			}
		}
		return null;
	}
	
	/********************************************************************************
	 * Methods - Implement ManageableGroup
	 ********************************************************************************/	

	@Override
	public Manageable create() 
	{
		return new Player();
	}

	/********************************************************************************
	 * Methods - Implement ManagePlayers
	 ********************************************************************************/	
	
	@Override
	public boolean existsPlayer( UUID id )
	{
		return this.get( id ) != null;
	}
	
	@Override
	public boolean existsPlayer( String name )
	{
		return this.exists( name );
	}
	
	@Override
	public boolean existsPlayer( EntityPlayer entityPlayer )
	{
		return this.existsPlayer( entityPlayer.getUniqueID() );
	}
	
	@Override
	public void addPlayer( Player player )
	{
		this.add( player );
		this.setDirty( true );
	}
	
	@Override
	public void addPlayer( EntityPlayer entityPlayer )
	{
		this.addPlayer( new Player( entityPlayer ) );
	}
	
	@Override
	public void removePlayer( UUID id )
	{
		Manageable player = this.get( id );
		
		if( player instanceof Player )
		{
			this.removePlayer( (Player)player );
		}
	}
	
	@Override
	public void removePlayer( String name )
	{
		Manageable player = this.get( name );
		
		if( player instanceof Player )
		{
			this.removePlayer( (Player)player );
		}		
	}
	
	@Override
	public void removePlayer( Player player )
	{
		this.remove( player );
		this.setDirty( true );
	}
	
	@Override
	public void removePlayer( EntityPlayer entityPlayer )
	{
		this.removePlayer( entityPlayer.getUniqueID() );
	}
	
	/********************************************************************************
	 * Methods - Implement FindPlayer
	 ********************************************************************************/	
	
	@Override
	public Player findPlayer( UUID id ) throws PlayerException
	{
		Manageable player = this.get( id );
		
		if( player instanceof Player )
		{
			return (Player)player;
		}
		else
		{
			throw new PlayerException( "Can't find player with id '" + id.toString() + "'" );
		}
	}
	
	@Override
	public Player findPlayer( String name ) throws PlayerException
	{
		//Case sensitive
		if( this.exists( name ) ) { return (Player)this.get( name ); }
		
		//Not case sensitive
		for( Entry<String, Manageable> entry : this.getAll() )
		{
			if( ( entry.getValue() instanceof Player ) &&
				( ((Player)entry.getValue()).getManageableName().toLowerCase().equals( name.toLowerCase() ) ) )
			{
				return (Player)entry.getValue();
			}
		}
		throw new PlayerException( "Can't find player with name '" + name + "'" );
	}
	
	@Override
	public Player findPlayer( ICommandSender sender ) throws PlayerException
	{
		return this.findPlayer( sender.getCommandSenderName() );
	}
	
	@Override
	public Player findPlayer( EntityPlayer entityPlayer ) throws PlayerException
	{
		return this.findPlayer( entityPlayer.getUniqueID() );
	}

	/********************************************************************************
	 * Methods - Implement TeleportPlayer
	 ********************************************************************************/
	
	@Override
	public void teleport( TeleportEvent event )
	{
		if( event.isCanceled() ) { return; }
		
		event.prepare();
		
		if( event.dimension == null ) { throw new TeleportException( "Teleport 'dimension' is missing." ); }
		if( event.target == null ) { throw new TeleportException( "Teleport 'target' is missing." ); }
		if( event.target.isRemote ) { return; }
		if( event.player == null ) { throw new TeleportException( "Teleport 'player' is missing." ); }
		if( !(event.player instanceof EntityPlayerMP ) ) { return; }
		if( event.coordinate == null ) { throw new TeleportException( "Teleport 'coordinate' is missing." ); }

		ServerConfigurationManager scm = MinecraftServer.getServer().getConfigurationManager();
		
		//Dimension transfer
		if( event.player.dimension != event.dimension.getId() ) 
		{
			scm.transferPlayerToDimension(
					event.player, 
					event.dimension.getId(), 
				new DimensionTeleport( event.target, event.coordinate ) 
			);
		}
		event.player.playerNetServerHandler.setPlayerLocation(
			event.coordinate.x,
			event.coordinate.y,
			event.coordinate.z,
			event.player.rotationYaw,
			event.player.rotationPitch
		);
	}
	
	/********************************************************************************
	 * Methods - Implement JsonFileAdapter
	 ********************************************************************************/
	
	@Override
	public void setSavePath( String savePath ) {}

	@Override
	public String getSavePath() 
	{
		StringJoiner path = new StringJoiner( File.separator )
			.add( DimensionManager.getCurrentSaveRootDirectory().toString() )
			.add( "data" )
			.add( YADM.MODID );
		return path.toString();
	}

	@Override
	public void setDirty( boolean flag )
	{
		this.dirty = flag;
	}

	@Override
	public boolean isDirty() 
	{
		return this.dirty;
	}

	@Override
	public void save() throws IOException, DataException
	{
		if( !this.isDirty() ) { return; }
		
		Manageable data;
		String fileName = this.getSavePath() + File.separator + this.getManageableGroupName() + ".json";
		
		try 
		{
			OutputStreamWriter writer = new OutputStreamWriter( new FileOutputStream( fileName ), "UTF-8" );
			
			this.serialize().writeTo( writer, WriterConfig.PRETTY_PRINT );
			
			writer.close();
		} 
		catch ( IOException | DataException e )
		{
			e.printStackTrace();
		}	
	}

	@Override
	public void load() throws IOException, DataException
	{
		File folder	= new File( this.getSavePath() );
		
		if( !folder.exists() ) { folder.mkdir(); }
		
		Manageable data;
		File file = new File( this.getSavePath() + File.separator + this.getManageableGroupName() + ".json" );
		
		if( ( file.exists() ) && 
			( file.isFile() ) )
		{
			InputStreamReader reader = new InputStreamReader( new FileInputStream( file ), "UTF-8" );
			
			this.deserialize( Json.parse( reader ) );
			
			reader.close();
		}
	}		
	
	@Override
	public void cleanup()
	{
		this.mapppingId.clear();
		this.clear();
	}		
}
