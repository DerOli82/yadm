package de.alaoli.games.minecraft.mods.yadm.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.Template;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SeedSetting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldProviderSetting;
import de.alaoli.games.minecraft.mods.yadm.json.JsonFileAdapter;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import de.alaoli.games.minecraft.mods.yadm.world.WorldServerGeneric;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

public class YADimensionManager extends ManageableGroup implements JsonFileAdapter 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	public static final YADimensionManager INSTANCE = new YADimensionManager();
	
	private static int nextId = Config.Dimension.beginsWithId;
	
	private boolean dirty;
	
	private Map<Integer, Dimension> dimensionIdMapping;
	private Map<Integer, Dimension> deletedDimensions;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	private YADimensionManager() 
	{
		super( null );
		
		this.dimensionIdMapping = new HashMap<Integer, Dimension>();
		this.deletedDimensions = new HashMap<Integer, Dimension>();
	}
	
	public void add( Dimension dimension )
	{
		if( !this.exists( dimension.getManageableGroupName() ) )
		{
			this.add( new DimensionGroup( dimension.getManageableGroupName(), this.getSavePath() ) );
		}
		ManageableGroup group = (ManageableGroup)this.get( dimension.getManageableGroupName() );
		
		group.add( dimension );
		this.dimensionIdMapping.put( dimension.getId(), dimension );
	}
	
	public void remove( Dimension dimension )
	{
		if( this.exists( dimension.getManageableGroupName() ) )
		{
			ManageableGroup group = (ManageableGroup)this.get( dimension.getManageableGroupName() );
			
			group.remove( dimension );
			this.dimensionIdMapping.remove( dimension.getId() );
			
			if( group.isEmpty() )
			{
				this.remove( group );
			}
		}
	}
	
	public boolean exists( int id )
	{
		return this.dimensionIdMapping.containsKey( id );
	}
	
	public boolean exists( Dimension dimension )
	{
		return this.exists( dimension.getManageableGroupName(), dimension.getManageableGroupName() );
	}
	
	public boolean exists( String group, String name )
	{
		if( this.exists( group ) )
		{
			ManageableGroup manageable = (ManageableGroup)this.get( group );
			
			if( manageable.exists( name) )
			{
				return true;
			}
		}
		return false;
	}

	public Dimension get( int id )
	{
		return this.dimensionIdMapping.get( id );
	}
	
	public Dimension get( String group, String name )
	{
		if( this.exists( group ) )
		{
			ManageableGroup manageable = (ManageableGroup) this.get( group );
			
			if( manageable.exists( name) )
			{
				return (Dimension) manageable.get( name );
			}
		}
		return null;
	}
	
	public WorldServer getWorldServerForDimension( Dimension dimension )
	{
		WorldServer result = null; 
		
		if( this.exists( dimension ) )
		{
			result = DimensionManager.getWorld( dimension.getId() );
			
			if( result == null )
			{
				this.init( dimension );
				result = DimensionManager.getWorld( dimension.getId() );
			}
		}
		else
		{
			result = MinecraftServer.getServer().worldServerForDimension( dimension.getId() );
		}
		return result;
	}
	

	/********************************************************************************
	 * Methods - Manage Dimensions
	 ********************************************************************************/
	
	/**
	 * Returns next free dimension id
	 * 
	 * @return
	 */
	public static int nextDimensionId()
	{
		while( DimensionManager.isDimensionRegistered( nextId ) )
		{
			nextId++;
		}
		return nextId;
	}
	
	/**
	 * Register all YADM dimensions
	 */
	public void register()
	{
		for( Dimension dimension : this.dimensionIdMapping.values() )
		{
			if( !dimension.isRegistered() )
			{
				this.register( dimension );
				this.init( dimension );
			}			
		}
	}
	
	/**
	 * Register dimension
	 * 
	 * @param dimension
	 */
	public void register( Dimension dimension )
	{
		//Nothing to do
		if( dimension.isRegistered() ) { return; }
			
		if( DimensionManager.isDimensionRegistered( dimension.getId() ) )
		{
			dimension.setRegistered(true);
			return;
		}
		
		try 
		{
			WorldProvider provider = WorldBuilder.instance.createProvider( dimension );
			int providerId = WorldBuilder.instance.registerProvider( provider, false );
			
			DimensionManager.registerDimension( dimension.getId(), providerId );
			
			WorldProviderSetting providerSetting = (WorldProviderSetting) dimension.get( SettingType.WORLDPROVIDER );
			providerSetting.setId( providerId );
			dimension.setRegistered( true );
			
			StringBuilder msg = new StringBuilder()
				.append( "Register Dimension '" )
				.append( dimension.getManageableGroupName() )
				.append( ":" )
				.append( dimension.getManageableName() )
				.append( "' with ID '" )
				.append( dimension.getId() )
				.append( "' and Provider '" )
				.append( providerSetting.getName() )
				.append( "' with ID '" )
				.append( providerSetting.getId() )
				.append( "'." );
			Log.info( msg.toString() );
		} 
		catch ( Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Passthrough DimensionManager
	 * 
	 * @param dimension
	 */
	public void init( Dimension dimension )
	{
		//DimensionManager.initDimension( dimension.getId() );
		
        WorldServer overworld = DimensionManager.getWorld(0);
        if (overworld == null)
        {
            throw new RuntimeException("Cannot Hotload Dim: Overworld is not Loaded!");
        }
        try
        {
            DimensionManager.getProviderType(dimension.getId());
        }
        catch (Exception e)
        {
            System.err.println("Cannot Hotload Dim: " + e.getMessage());
            return; // If a provider hasn't been registered then we can't hotload the dim
        }
        MinecraftServer mcServer = overworld.func_73046_m();
        ISaveHandler savehandler = overworld.getSaveHandler();
        WorldSettings worldSettings = new WorldSettings( overworld.getWorldInfo() );

        WorldServer world = new WorldServerGeneric( mcServer, savehandler, overworld.getWorldInfo().getWorldName(), dimension, worldSettings, mcServer.theProfiler );
        world.addWorldAccess( new WorldManager( mcServer, world ) );
        
        MinecraftForge.EVENT_BUS.post( new WorldEvent.Load( world ) );
        if (!mcServer.isSinglePlayer())
        {
            world.getWorldInfo().setGameType( mcServer.getGameType() );
        }
        mcServer.func_147139_a( mcServer.func_147135_j() );
	}
	
	public Dimension create( String group, String name, Template template ) 
	{
		Dimension dimension = new Dimension( nextDimensionId(), group, name );
		
		dimension.add( template.getAll() );
		this.add( dimension );
		this.setDirty( true );
		
		//Initialize random seed
		if( ( dimension.hasSetting( SettingType.SEED ) ) &&
			( ((SeedSetting)dimension.get( SettingType.SEED )).isRandom() ) )
		{
			((SeedSetting)dimension.get( SettingType.SEED )).getValue();
		}
		return dimension;
	}

	public void delete( Dimension dimension )
	{
		this.deletedDimensions.put( dimension.getId(), dimension );
		this.remove( dimension );
		
		DimensionManager.unloadWorld( dimension.getId() );
	}
	
	/**
	 * Delete dimension folder
	 * 
	 * @param world
	 */
	public void delete( World world )
	{
		if( !this.deletedDimensions.containsKey( world.provider.dimensionId ) ) { return; }
		
		Dimension dimension = this.deletedDimensions.get( world.provider.dimensionId );
		this.deletedDimensions.remove( world.provider.dimensionId );
		
		((WorldServer)world).flush();
		this.unregister( dimension );
		
		//Delete dimension folder
		StringBuilder path = new StringBuilder()
			.append( this.getSavePath() )
			.append( File.separator )
			.append( "DIM" )
			.append( dimension.getId() )
			.append( File.separator );
		File file = new File( path.toString() );
			
		if( ( file.exists() ) && ( file.isDirectory() ) )
		{
			try 
			{
				FileUtils.deleteDirectory( file );
				this.setDirty( true );
				
				Log.info( "Dimenson folder 'DIM" + dimension.getId() + "' deleted!" );
			}
			catch ( IOException e ) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void unregister()
	{
		Dimension dimension;
		
		for( Entry<String, Manageable> groupEntry : this.getAll() )
		{	
			for( Entry<String, Manageable> dimensionEntry : ((ManageableGroup)groupEntry.getValue()).getAll() )
			{
				dimension = (Dimension) dimensionEntry.getValue();
			
				if( dimension.isRegistered() )
				{
					this.unregister( dimension );
				}
			}
		}			
	}
	
	public void unregister( Dimension dimension )
	{
		StringBuilder msg;
		
		if( DimensionManager.isDimensionRegistered( dimension.getId() ) )
		{
			WorldProviderSetting providerSetting = (WorldProviderSetting) dimension.get( SettingType.WORLDPROVIDER );
			try
			{
				msg = new StringBuilder()
					.append( "Unregister Dimension '" )
					.append( dimension.getManageableGroupName() )
					.append( ":" )
					.append( dimension.getManageableName() )
					.append( "' with ID '" )
					.append( dimension.getId() )
					.append( "'." );
				Log.info( msg.toString() );
				
				DimensionManager.unregisterDimension( dimension.getId() );
			}
			catch( Exception e )
			{
				msg = new StringBuilder()
					.append( "Couldn't unregister Dimension '" )
					.append( dimension.getManageableGroupName() )
					.append( ":" )
					.append( dimension.getManageableName() )
					.append( "' with ID '" )
					.append( dimension.getId() )
					.append( "'.");
				Log.info( msg.toString() );
			}
			try
			{
				msg = new StringBuilder()
					.append( "Unregister Provider '" )
					.append( providerSetting.getName() )
					.append( "' with ID '" )
					.append( providerSetting.getId() )
					.append( "'." );
				Log.info( msg.toString() );
				
				DimensionManager.unregisterProviderType( providerSetting.getId() );
			}
			catch( Exception e )
			{
				msg = new StringBuilder()
					.append( "Couldn't unregister Provider '" )
					.append( providerSetting.getName() )
					.append( "' with ID '" )
					.append( providerSetting )
					.append( "'.");
				Log.info( msg.toString() );
			}			
		}
		else
		{
			msg = new StringBuilder()
				.append( "Dimension '" )
				.append( dimension.getManageableGroupName() )
				.append( ":" )
				.append( dimension.getManageableName() )
				.append( "' with ID '" )
				.append( dimension.getId() )
				.append( "' already unregistered.");
			Log.info( msg.toString() );			
		}
		dimension.setRegistered( false );
		YADM.proxy.unregisterDimension( dimension );
	}

	/**
	 * Unregister all dimensions
	 * Teleport players from deleted dimensions to overworld spawn
	 * Delete deleted dimensions directories 
	 */
	public void cleanup()
	{
		this.unregister();
		this.clear();	
	}

	/********************************************************************************
	 * Methods - Implement ManageableGroup
	 ********************************************************************************/	

	@Override
	public Manageable create() 
	{
		return null;
	}	

	/********************************************************************************
	 * Methods - Implement JsonFileAdapter
	 ********************************************************************************/
	
	@Override
	public void setSavePath( String savePath ) {}

	@Override
	public String getSavePath() 
	{
		return DimensionManager.getCurrentSaveRootDirectory() + File.separator + "data" + File.separator + YADM.MODID;
	}

	@Override
	public void setDirty( boolean flag )
	{
		this.dirty = flag;
	}

	@Override
	public boolean isDirty() 
	{
		if( this.dirty ) { return true; }
		
		Manageable data;
		
		for( Entry<String, Manageable> entry : this.getAll() )
		{
			data = entry.getValue();
			
			if( data instanceof JsonFileAdapter )
			{
				if( ((JsonFileAdapter)data).isDirty() ) { return true; }
			}
		}
		return false;
	}

	@Override
	public void save() 
	{
		if( !this.dirty ) { return; }
		
		Manageable data;
		
		for( Entry<String, Manageable> entry : this.getAll() )
		{
			data = entry.getValue();
			
			if( data instanceof JsonFileAdapter )
			{
				((JsonFileAdapter)data).save();
			}		
		}		
	}

	@Override
	public void load() 
	{
		File folder	= new File( this.getSavePath() );
		
		if( !folder.exists() ) { folder.mkdir(); }
		
		Manageable data;
		String groupName;
		File[] files = folder.listFiles();
		
		for( File file : files ) 
		{
			if( ( file.isFile() ) && 
				( file.getName().endsWith(".json") ) )
			{
				groupName = file.getName().replace( ".json", "" );
				
				//Initialize group 
				if( this.exists( groupName ) )
				{
					data = this.get( groupName );
				}
				else
				{
					data = new DimensionGroup( groupName, this.getSavePath() ); 
					this.add( data );
				}
				
				if( data instanceof JsonFileAdapter )
				{
					((JsonFileAdapter)data).load();
				}
			}
		}		
	}	
}
