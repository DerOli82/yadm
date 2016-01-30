package de.alaoli.games.minecraft.mods.yadm.manager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.data.DataObject;
import de.alaoli.games.minecraft.mods.yadm.data.DimensionPattern;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;

public class PatternManager extends AbstractManager
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private Map<Integer, String> worldProviders;

	private Map<String, WorldType> worldTypes;

	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public PatternManager()
	{
		super();
		this.initProvider();
		this.initTypes();
	}

	private void initProvider()
	{
		this.worldProviders = new HashMap<Integer, String>();
		
		try 
		{
			Hashtable<Integer, Class<? extends WorldProvider>> providers;
			Field providersField = DimensionManager.class.getDeclaredField( "providers" );
			
			providersField.setAccessible( true );
			
			providers = (Hashtable<Integer, Class<? extends WorldProvider>>) providersField.get( null );
			
			for( Entry<Integer, Class<? extends WorldProvider>> provider : providers.entrySet() )
			{
				this.worldProviders.put( provider.getKey(), provider.getValue().getName() );
			}
		}
		catch ( NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e )
		{
			Log.error( e.getMessage() );
		}
		
	}
	
	private void initTypes()
	{
		WorldType type;
		String name;
		
		this.worldTypes = new HashMap<String, WorldType>();
		
		for( int i = 0; i < WorldType.worldTypes.length; i++ )
		{
			type = WorldType.worldTypes[ i ];
			
			if( type != null )
			{
				name = type.getWorldTypeName();
			
				//Minecraft doesn't allow to create DEFAULT_1_1 ( see net.minecraft.world.WorldType )
				if ( !name.equals( WorldType.DEFAULT_1_1.getWorldTypeName() ) )
				{
					this.worldTypes.put( name, type );
				}
			}
		}
	}
	
	/**
	 * Create Overworld, Nether and The End default patterns
	 */
	private void CreateDefaultPatterns()
	{
		this.add( new DimensionPattern( 
			DimensionPattern.PROVIDER_OVERWORLD,
			DimensionPattern.PROVIDER_OVERWORLD,
			DimensionPattern.TYPE_DEFAULT,
			null
		));
		this.add( new DimensionPattern( 
			DimensionPattern.PROVIDER_NETHER,
			DimensionPattern.PROVIDER_NETHER,
			DimensionPattern.TYPE_DEFAULT,
			null
		));
		this.add( new DimensionPattern( 
			DimensionPattern.PROVIDER_END,
			DimensionPattern.PROVIDER_END,
			DimensionPattern.TYPE_DEFAULT,
			null
		));
		this.dirty = true;
		this.save();
	}
	
	public boolean existsWorldProvider( int provider )
	{
		return this.worldProviders.containsKey( provider );
	}
	
	public boolean existsWorldType( String type )
	{
		return this.worldTypes.containsKey( type );
	}

	/********************************************************************************
	 * Methods - Implements AbstractManager
	 ********************************************************************************/
	
	@Override
	public DataObject create( String name )
	{
		DimensionPattern pattern = new DimensionPattern( name );
		
		this.add( pattern );
		this.dirty = true;
		
		return pattern;
	}

	@Override
	public void load()
	{
		JsonReader reader;
		DimensionPattern pattern;
		
		Gson gson	= (new GsonBuilder()).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		File folder	= new File( this.getSavePath() );
		
		//Create folder and default pattern
		if( !folder.exists() )
		{
			folder.mkdir();
			this.CreateDefaultPatterns();
		}
		File[] files = folder.listFiles();
		
		for( File file : files ) 
		{
			if( ( file.isFile() ) && 
				( file.getName().endsWith(".json") ) )
			{
				try 
				{
					reader	= new JsonReader( new FileReader( file ) );
					pattern	= gson.fromJson( reader, DimensionPattern.class );

					this.add( pattern );
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
				gson.toJson( entry.getValue(), DimensionPattern.class, writer );
				
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
