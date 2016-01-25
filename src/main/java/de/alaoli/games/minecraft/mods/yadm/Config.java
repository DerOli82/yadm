package de.alaoli.games.minecraft.mods.yadm;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import de.alaoli.games.minecraft.mods.yadm.data.DimensionPattern;
import de.alaoli.games.minecraft.mods.yadm.json.DimensionPatternAdapter;
import de.alaoli.games.minecraft.mods.yadm.manager.DimensionPatternManager;
import net.minecraftforge.common.config.Configuration;

public class Config 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/

	public static class Dimensions
	{
		/**
		 * Use as first dimension id.
		 */
		public static int beginsWithId = 1000;
		
		/**
		 * Teleport to new dimension after create command.
		 */
		public static boolean teleportOnCreate = true;
	}
	
	private static String jsonPath;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	/**
	 * Configuration initialization 
	 * 
	 * @param Configuration
	 */	
	public static void initCfg( Configuration config )
	{	
		config.load();
        
     	Config.Dimensions.beginsWithId = config.getInt(
			"beginsWithId",
			"dimensions", 
			Config.Dimensions.beginsWithId, 
			Integer.MIN_VALUE,
			Integer.MAX_VALUE,
			"Use as first dimension id."
		);
     	
     	Config.Dimensions.teleportOnCreate = config.getBoolean( 
 			"teleportOnCreate", 
 			"dimensions", 
 			Config.Dimensions.teleportOnCreate, 
 			"Teleport to new dimension after create command."
		);
     	     	
    	if( config.hasChanged() == true )
    	{
    		config.save();
    	}			
	}
	
	public static void initJson( String configPath )
	{
		DimensionPatternManager manager = DimensionPatternManager.getInstance();
		StringBuilder path = new StringBuilder();
		
		path.append( configPath );
		path.append( File.separator );
		path.append( YADM.MODID );
		path.append( File.separator );
		
		Config.jsonPath = path.toString();
		
		File folder = new File( Config.jsonPath );
		
		//Create folder and default pattern
		if( !folder.exists() )
		{
			folder.mkdir();

			manager.addPattern( new DimensionPattern( 
				DimensionPattern.PROVIDER_OVERWORLD,
				DimensionPattern.PROVIDER_OVERWORLD,
				DimensionPattern.TYPE_DEFAULT,
				null
			));
			manager.addPattern( new DimensionPattern( 
				DimensionPattern.PROVIDER_NETHER,
				DimensionPattern.PROVIDER_NETHER,
				DimensionPattern.TYPE_DEFAULT,
				null
			));
			manager.addPattern( new DimensionPattern( 
				DimensionPattern.PROVIDER_END,
				DimensionPattern.PROVIDER_END,
				DimensionPattern.TYPE_DEFAULT,
				null
			));
			manager.markDirty();
			Config.saveJson();
		}
		Config.loadJson();
	}	
	
	public static void loadJson()
	{
		JsonReader reader;
		DimensionPattern pattern;
		DimensionPatternManager manager = DimensionPatternManager.getInstance();
		Gson gson = new GsonBuilder().registerTypeAdapter( DimensionPattern.class, new DimensionPatternAdapter() ).create();		
		File folder = new File( Config.jsonPath );
		File[] files = folder.listFiles();
		
		for( File file : files )
		{
			if( ( file.isFile() ) && 
				( file.getName().endsWith( ".json" ) ) )
			{
				try 
				{
					reader = new JsonReader( new FileReader( file ) );
					pattern = gson.fromJson(reader, DimensionPattern.class );
					
					manager.addPattern( pattern );
					reader.close();
				}
				catch ( IOException e ) 
				{
					/**
					 * @TODO Log
					 */
					e.printStackTrace();
				}
			}
		}		
	}
	
	public static void saveJson()
	{
		DimensionPatternManager manager = DimensionPatternManager.getInstance();
		
		if( !manager.isDirty() )
		{
			return;
		}
		StringBuilder file;
		JsonWriter writer;
		Gson gson = new GsonBuilder().registerTypeAdapter( DimensionPattern.class, new DimensionPatternAdapter() ).create();
		
		for( Entry<String, DimensionPattern> entry : manager.getPatterns() )
		{
			try 
			{
				file = new StringBuilder();
				
				file.append( Config.jsonPath );
				file.append( File.separator);
				file.append( entry.getKey() );
				file.append( ".json" );
				
				writer = new JsonWriter( new FileWriter( file.toString() ) );
				gson.toJson( entry.getValue(), DimensionPattern.class, writer );
				
				writer.close();
			} 
			catch (IOException e) 
			{
				/**
				 * @TODO Log
				 */
				e.printStackTrace();
			}			
		}		
	}
}
