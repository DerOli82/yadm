package de.alaoli.games.minecraft.mods.yadm.manager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cpw.mods.fml.common.FMLCommonHandler;
import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.data.DataObject;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.DimensionTemplate;
import de.alaoli.games.minecraft.mods.yadm.data.DimensionSettings;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

public class YADimensionManager extends AbstractManager 
{
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
	 * Methods
	 ********************************************************************************/
	
	public int nextDimensionId()
	{
		int nextId = Config.Dimensions.beginsWithId;
		
		//Check for next ID
		while( DimensionManager.isDimensionRegistered( nextId ) )
		{
			nextId++;
		}
		return nextId;
	}
	
	public void init( Dimension dimension )
	{
		DimensionManager.initDimension( dimension.getId() );
	}
	
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
	
	public void registerAll()
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
	
	public Dimension create( String name, DimensionTemplate pattern ) 
	{
		Dimension dimension = new Dimension( this.nextDimensionId(), name, new DimensionSettings( pattern ) );
		
		this.add( dimension );
		this.dirty = true;

		return dimension;
	}
	
	/********************************************************************************
	 * Methods - Implements AbstractManager
	 ********************************************************************************/
	
	@Override
	public void load() 
	{
		JsonReader reader;
		Dimension dimension;
		
		Gson gson	= (new GsonBuilder()).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		File folder	= new File( this.getSavePath() );
		
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
		StringBuilder file;
		JsonWriter writer;
		
		Gson gson = (new GsonBuilder()).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

		try 
		{
			for( Entry<String, DataObject> entry : this.getAll() )
			{
				file = new StringBuilder();
				
				file.append( this.getSavePath() );
				file.append( File.separator);
				file.append( entry.getKey() );
				file.append( ".json" );
				
				writer = new JsonWriter( new FileWriter( file.toString() ) );
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
}
