package de.alaoli.games.minecraft.mods.yadm.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map.Entry;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.WriterConfig;

import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.Template;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldProviderSetting;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

public class YADimensionManager extends AbstractManager 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	public static final YADimensionManager instance = new YADimensionManager();
	
	private static int nextId = Config.Dimension.beginsWithId;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	private YADimensionManager() 
	{
		super( "dimensions" );
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
		return this.create( name, template, "default" );
	}

	public Dimension create( String name, Template template, String group ) 
	{
		Dimension dimension = new Dimension( this.nextDimensionId(), name );
		
		dimension.add( template.getAll() );
		
		//Create new group
		if( !this.exists( group ) )
		{
			this.add( new DimensionGroup( group ) );
		}
		((ManageableGroup)this.get( group )).add( dimension );
		
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
		/*
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
		
		this.dirty = true;
		DimensionManager.unloadWorld( dimension.getId() );*/
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
		/*
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
		}	*/
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
	 * Methods - Implement ManageableGroup
	 ********************************************************************************/
	
	@Override
	public Manageable create() 
	{
		return null;
	}
	
	/********************************************************************************
	 * Methods - Implement AbstractManager
	 ********************************************************************************/
	
	@Override
	public void load() 
	{
		
		String groupName;
		InputStreamReader reader;
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
					groupName = file.getName().replace( ".json", "" );
					reader = new InputStreamReader( new FileInputStream( file.toString() ), "UTF-8" );
	
					//Initialize group 
					if( !this.exists( groupName ) )
					{
						this.add( new TemplateGroup( groupName ) );
					}
					((JsonSerializable)this.get( groupName )).deserialize( Json.parse( reader ) );
					
					reader.close();
				}
				catch ( IOException e ) 
				{
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void save() 
	{
		//Nothing to do
		if( !this.dirty ) { return; }
		
		Manageable data;
		StringBuilder file;
		OutputStreamWriter writer;
		
		for( Entry<String, Manageable> entry : this.getAll() )
		{
			data = entry.getValue();
		
			if( data instanceof JsonSerializable )
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
				
				try
				{
					writer = new OutputStreamWriter( new FileOutputStream( file.toString() ), "UTF-8" );
					((JsonSerializable)data).serialize().writeTo( writer, WriterConfig.PRETTY_PRINT );
					
					writer.close();
				}
				catch( IOException e ) 
				{
					e.printStackTrace();
				}
			}
		}
	}
}
