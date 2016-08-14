package de.alaoli.games.minecraft.mods.yadm.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map.Entry;
import java.util.Set;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.data.Template;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldProviderSetting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldTypeSetting;
import de.alaoli.games.minecraft.mods.yadm.json.SettingJsonAdapter;
import de.alaoli.games.minecraft.mods.yadm.json.TemplateJsonAdapter;

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
		Template template = new Template( "overworld", "vanilla" );
		template.add( new WorldProviderSetting( WorldProviderSetting.OVERWORLD ) );
		template.add( new WorldTypeSetting( WorldTypeSetting.DEFAULT ) );
		this.add( template );
		
		template = new Template( "nether", "vanilla" );
		template.add( new WorldProviderSetting( WorldProviderSetting.NETHER ) );
		template.add( new WorldTypeSetting( WorldTypeSetting.DEFAULT ) );
		this.add( template );
		
		template = new Template( "end", "vanilla" );
		template.add( new WorldProviderSetting( WorldProviderSetting.END ) );
		template.add( new WorldTypeSetting( WorldTypeSetting.DEFAULT ) );
		this.add( template );

		this.dirty = true;
		this.save();
	}
	
	/********************************************************************************
	 * Methods - Implements AbstractManager
	 ********************************************************************************/

	@Override
	public void load()
	{
		JsonReader reader;
		Set<Manageable> group;
		
		Gson gson = new GsonBuilder()
			.registerTypeHierarchyAdapter( SettingJsonAdapter.class, new SettingJsonAdapter() )
			.registerTypeHierarchyAdapter( TemplateJsonAdapter.class, new TemplateJsonAdapter() )
			.excludeFieldsWithoutExposeAnnotation()
			.create();
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
					reader = new JsonReader( new InputStreamReader( new FileInputStream( file.toString() ), "UTF-8" ) );
					
					group = gson.fromJson( reader, TemplateJsonAdapter.class );
					
					this.addGroup( group );
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
		if( !this.dirty ) { return; }
		
		StringBuilder file;
		JsonWriter writer;
		
		Gson gson = (new GsonBuilder())
			.excludeFieldsWithoutExposeAnnotation()
			.registerTypeHierarchyAdapter( SettingJsonAdapter.class, new SettingJsonAdapter() )
			.registerTypeHierarchyAdapter( TemplateJsonAdapter.class, new TemplateJsonAdapter() )
			.create();

		try 
		{		
			for( Entry<String, Set<Manageable>> entry : this.getAllGroups().entrySet() )
			{
				file = new StringBuilder()
					.append( this.getSavePath() )
					.append( File.separator)
					.append( entry.getKey() )
					.append( ".json" );
				writer = new JsonWriter( new OutputStreamWriter( new FileOutputStream( file.toString() ), "UTF-8" ) );
				writer.setIndent( "  " );

				gson.toJson( entry.getValue(), TemplateJsonAdapter.class, writer );

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
