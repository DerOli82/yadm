package de.alaoli.games.minecraft.mods.yadm.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.Template;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldProviderSetting;
import de.alaoli.games.minecraft.mods.yadm.json.DimensionJsonAdapter;
import de.alaoli.games.minecraft.mods.yadm.json.SettingJsonAdapter;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class YADimensionManager extends AbstractManager 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	public static final YADimensionManager instance = new YADimensionManager();
	
	private static int nextId = Config.Dimension.beginsWithId;
	
	private Map<Integer, Dimension> deletedDimensions;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	private YADimensionManager() 
	{
		this.deletedDimensions = new HashMap<Integer, Dimension>();
	}
	
	
	public boolean exists( int id )
	{
		if( this.get( id ) == null ) { return false; }
		
		return true;
	}
	
	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/

	public Dimension get( int id )
	{
		Dimension dimension;
		
		for( Entry<String, Manageable> entry : this.getAll() )
		{
			dimension = (Dimension) entry.getValue();
			
			if( dimension.getId() == id )
			{
				return dimension;
			}
		}
		return null;
	}
	
	/********************************************************************************
	 * Methods - Manage Dimensions
	 ********************************************************************************/
	
	public int nextDimensionId()
	{
		//Check for next ID
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
		Dimension dimension;
		
		for( Entry<String, Manageable> entry : this.getAll() )
		{
			dimension = (Dimension) entry.getValue();
		
			if( !dimension.isRegistered() )
			{
				this.register( dimension );
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
				.append( dimension.getName() )
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
		DimensionManager.initDimension( dimension.getId() );
	}
	
	public Dimension create( String name, Template template ) 
	{
		Dimension dimension = new Dimension( this.nextDimensionId(), name );
		
		dimension.add( template.getAll() );
		this.add( dimension );

		return dimension;
	}
	
	public void backup( Dimension dimension )
	{
		//TODO
		Log.info("Backup feature not implemented yet!");
	}
	
	public void delete( String name )
	{
		this.delete( (Dimension)this.get(name) );
	}
	
	public void delete( Dimension dimension )
	{
		//Delete dimension json file		
		StringBuilder path = new StringBuilder()
			.append( this.getSavePath() )
			.append( File.separator )
			.append( "data" )
			.append( File.separator )
			.append( YADM.MODID )
			.append( File.separator)
			.append( dimension.getName() )
			.append( ".json" );
		File file = new File( path.toString() );
	
		if( file.exists() )
		{
			file.delete();
		}
		this.deletedDimensions.put( dimension.getId(), dimension );
		this.remove( dimension );
		
		this.markDirty();
		DimensionManager.unloadWorld( dimension.getId() );
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
					.append( dimension.getName() )
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
					.append( dimension.getName() )
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
				.append( dimension.getName() )
				.append( "' with ID '" )
				.append( dimension.getId() )
				.append( "' already unregistered.");
			Log.info( msg.toString() );			
		}
		dimension.setRegistered( false );
	}

	public void unregisterAll()
	{
		Dimension dimension;
		
		for( Entry<String, Manageable> entry : this.getAll() )
		{
			dimension = (Dimension) entry.getValue();
		
			if( dimension.isRegistered() )
			{
				this.unregister( dimension );
			}
		}
	}
	
	public void cleanup( World world )
	{
		if( !this.deletedDimensions.containsKey( world.provider.dimensionId ) ) { return; }
		
		Dimension dimension = this.deletedDimensions.get( world.provider.dimensionId );
		this.deletedDimensions.remove( dimension );
		
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
				FileUtils.deleteDirectory(file);
				Log.info( "Dimenson folder 'DIM" + dimension.getId() + "' deleted!" );
			}
			catch ( IOException e ) 
			{
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Unregister all dimensions
	 * Teleport players from deleted dimensions to overworld spawn
	 * Delete deleted dimensions directories 
	 */
	public void cleanup()
	{
		this.unregisterAll();
		this.clear();	
	}
	
	/********************************************************************************
	 * Methods - Implements/Override AbstractManager
	 ********************************************************************************/
	
	@Override
	public void load() 
	{
		JsonReader reader;
		Set<Manageable> group;
		
		Gson gson = new GsonBuilder()
			.registerTypeHierarchyAdapter( SettingJsonAdapter.class, new SettingJsonAdapter() )
			.registerTypeHierarchyAdapter( DimensionJsonAdapter.class, new DimensionJsonAdapter() )
			.excludeFieldsWithoutExposeAnnotation()
			.create();		
		StringBuilder path = new StringBuilder()
			.append( this.getSavePath() )
			.append( File.separator )
			.append( "data" )
			.append( File.separator )
			.append( YADM.MODID )
			.append( File.separator );
		File folder	= new File( path.toString() );
		
		//Create folder
		if( !folder.exists() )
		{
			folder.mkdir();
		}
		File[] files = folder.listFiles();
		
		for( File file : files ) 
		{
			if( ( file.isFile() ) && 
				( file.getName().endsWith(".json") ) )
			{
				try 
				{
					reader = new JsonReader( new InputStreamReader( new FileInputStream( file.toString() ), "UTF-8" ) );
					group = gson.fromJson( reader, DimensionJsonAdapter.class );

					this.addGroup( group );
					
					for( Manageable dimension : group )
					{
						this.register( (Dimension)dimension );
						this.init( (Dimension)dimension );
					}
					reader.close();
				}
				catch ( IOException e ) 
				{
					Log.error( e.getMessage() );
				}
			}
		}		
	}

	@Override
	public void save() 
	{
		//Nothing to do
		if( !this.dirty )
		{
			return;
		}
		JsonWriter writer;
		StringBuilder file;
		
		Gson gson = (new GsonBuilder())
			.excludeFieldsWithoutExposeAnnotation()
			.registerTypeHierarchyAdapter( SettingJsonAdapter.class, new SettingJsonAdapter() )
			.registerTypeHierarchyAdapter( DimensionJsonAdapter.class, new DimensionJsonAdapter() )
			.create();

		try 
		{
			for( Entry<String, Set<Manageable>> entry : this.getAllGroups().entrySet() )
			{
				file = new StringBuilder()
					.append( this.getSavePath() )
					.append( File.separator )
					.append( "data" )
					.append( File.separator )
					.append( YADM.MODID )
					.append( File.separator)
					.append( entry.getKey() )
					.append( ".json" );				
				writer = new JsonWriter( new OutputStreamWriter( new FileOutputStream( file.toString() ), "UTF-8" ) );
				writer.setIndent( "  " );
				
				gson.toJson( entry.getValue(), DimensionJsonAdapter.class, writer );
			
				writer.close();
			}
			this.dirty = false;
		} 
		catch( IOException e ) 
		{
			Log.error( e.getMessage() );
		}			
	}
	
	@Override
	public void clear()
	{
		super.clear();
		this.deletedDimensions.clear();
	}
}
