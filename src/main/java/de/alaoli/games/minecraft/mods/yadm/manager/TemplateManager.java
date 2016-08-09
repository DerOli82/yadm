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

import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.data.DataObject;
import de.alaoli.games.minecraft.mods.yadm.data.DimensionTemplate;

public class TemplateManager extends AbstractManager
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	public static final TemplateManager instance = new TemplateManager();
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	private TemplateManager() {}
	
	/**
	 * Create Overworld, Nether and The End as default templates
	 */
	private void createDefaultTemplates()
	{
		this.add( new DimensionTemplate( 
			DimensionTemplate.PROVIDER_OVERWORLD,
			DimensionTemplate.PROVIDER_OVERWORLD,
			DimensionTemplate.TYPE_DEFAULT,
			null,
			null
		));
		this.add( new DimensionTemplate( 
			DimensionTemplate.PROVIDER_NETHER,
			DimensionTemplate.PROVIDER_NETHER,
			DimensionTemplate.TYPE_DEFAULT,
			null,
			null
		));
		this.add( new DimensionTemplate( 
			DimensionTemplate.PROVIDER_END,
			DimensionTemplate.PROVIDER_END,
			DimensionTemplate.TYPE_DEFAULT,
			null,
			null
		));
		this.dirty = true;
		this.save();
	}
		
	public DimensionTemplate create( String name )
	{
		DimensionTemplate template = new DimensionTemplate( name );
		
		this.add( template );
		this.dirty = true;
		
		return template;
	}
	
	/********************************************************************************
	 * Methods - Implements AbstractManager
	 ********************************************************************************/

	@Override
	public void load()
	{
		JsonReader reader;
		DimensionTemplate template;
		
		Gson gson	= (new GsonBuilder()).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		File folder	= new File( this.getSavePath() );
		
		//Create folder and default template
		if( !folder.exists() )
		{
			folder.mkdir();
			this.createDefaultTemplates();
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
					template	= gson.fromJson( reader, DimensionTemplate.class );

					this.add( template );
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
				gson.toJson( entry.getValue(), DimensionTemplate.class, writer );
				
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
