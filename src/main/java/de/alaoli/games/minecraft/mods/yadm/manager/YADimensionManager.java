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

import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.DataObject;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
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
		if( ( dimension.isRegistered() ) || 
			( DimensionManager.isDimensionRegistered( dimension.getId() ) ) )
		{
			return;
		}
		
		try 
		{
			WorldProvider provider = YADM.proxy.getPatternManager().getProvider( dimension.getProviderName() );
			
			DimensionManager.registerProviderType( dimension.getId(), provider.getClass(), false );
			DimensionManager.registerDimension( dimension.getId(), dimension.getId() );
			dimension.setRegistered( true );
			
			YADM.proxy.syncDimension( dimension );
		} 
		catch ( Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void unregister( Dimension dimension )
	{
		DimensionManager.unregisterDimension( dimension.getId() );
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
	
	/********************************************************************************
	 * Methods - Implements AbstractManager
	 ********************************************************************************/
	
	@Override
	public DataObject create( String name ) 
	{
		Dimension dimension = new Dimension( this.nextDimensionId(), name );
		
		this.add( dimension );
		this.dirty = true;

		return dimension;
	}

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
