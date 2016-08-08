package de.alaoli.games.minecraft.mods.yadm.manager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.DataObject;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.DimensionTemplate;
import de.alaoli.games.minecraft.mods.yadm.data.DimensionSettings;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

public class YADimensionManager extends AbstractManager 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private static int nextId = Config.Dimension.beginsWithId;
	
	private Set<Dimension> deletedDimensions;
	
	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/

	public Dimension getById( int id )
	{
		Dimension dimension;
		
		for( Entry<String, DataObject> entry : this.getAll() )
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
		
		for( Entry<String, DataObject> entry : this.getAll() )
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
			dimension.getSettings().setProviderId( providerId );
			dimension.setRegistered( true );
			
			StringBuilder msg = new StringBuilder()
				.append( "Register Dimension '" )
				.append( dimension.getName() )
				.append( "' with ID '" )
				.append( dimension.getId() )
				.append( "' and Provider '" )
				.append( dimension.getSettings().getProviderName() )
				.append( "' with ID '" )
				.append( dimension.getSettings().getProviderId() )
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
	
	public Dimension create( String name, DimensionTemplate pattern ) 
	{
		Dimension dimension = new Dimension( this.nextDimensionId(), name, new DimensionSettings( pattern ) );
		
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
		
		
		if( Config.Dimension.backupDimensionOnRemove )
		{
			this.backup( dimension );
		}
		if( this.deletedDimensions == null ) {
			this.deletedDimensions = new HashSet<Dimension>();
		}
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
		this.deletedDimensions.add( dimension );
		this.remove( dimension );
		
		dimension.markDeleted();
		this.markDirty();
	}
	
	public void unregister( Dimension dimension )
	{
		StringBuilder msg;
		
		if( DimensionManager.isDimensionRegistered( dimension.getId() ) )
		{
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
					.append( dimension.getSettings().getProviderName() )
					.append( "' with ID '" )
					.append( dimension.getSettings().getProviderId() )
					.append( "'." );
				Log.info( msg.toString() );
				
				//DimensionManager.unregisterProviderType( dimension.getSettings().getProviderId() );
			}
			catch( Exception e )
			{
				msg = new StringBuilder()
					.append( "Couldn't unregister Provider '" )
					.append( dimension.getSettings().getProviderName() )
					.append( "' with ID '" )
					.append( dimension.getSettings().getProviderId() )
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
		
		for( Entry<String, DataObject> entry : this.getAll() )
		{
			dimension = (Dimension) entry.getValue();
		
			if( dimension.isRegistered() )
			{
				this.unregister( dimension );
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
		File file;
		StringBuilder path;
		
		this.unregisterAll();
		
		for( Dimension dimension : this.deletedDimensions )
		{
			//Delete dimension folder
			path = new StringBuilder()
				.append( this.getSavePath() )
				.append( File.separator )
				.append( "DIM" )
				.append( dimension.getId() )
				.append( File.separator );
			file = new File( path.toString() );
				
			if( ( file.exists() ) && ( file.isDirectory() ) )
			{
				try 
				{
					FileUtils.deleteDirectory(file);
				}
				catch ( IOException e ) 
				{
					e.printStackTrace();
				}
			}	
		}
		this.clear();	
	}
	
	/********************************************************************************
	 * Methods - Implements/Override AbstractManager
	 ********************************************************************************/

	@Override
	public void load() 
	{
		JsonReader reader;
		Dimension dimension;
		
		StringBuilder path = new StringBuilder()
			.append( this.getSavePath() )
			.append( File.separator )
			.append( "data" )
			.append( File.separator )
			.append( YADM.MODID )
			.append( File.separator );
		File folder	= new File( path.toString() );
		Gson gson = (new GsonBuilder()).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		
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
					reader		= new JsonReader( new FileReader( file ) );
					dimension	= gson.fromJson( reader, Dimension.class );

					this.add( dimension );
					this.register( dimension );
					this.init( dimension );
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
		File file;
		JsonWriter writer;
		StringBuilder pathJson, pathDimension;
		
		Gson gson = (new GsonBuilder()).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

		try 
		{
			for( Entry<String, DataObject> entry : this.getAll() )
			{
				pathJson = new StringBuilder()
					.append( this.getSavePath() )
					.append( File.separator )
					.append( "data" )
					.append( File.separator )
					.append( YADM.MODID )
					.append( File.separator)
					.append( entry.getValue().getName() )
					.append( ".json" );
				writer = new JsonWriter( new FileWriter( pathJson.toString() ) );
				gson.toJson( entry.getValue(), Dimension.class, writer );
			
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
	protected void clear()
	{
		super.clear();
		
		this.deletedDimensions.clear();
		this.deletedDimensions = null;
	}
}
