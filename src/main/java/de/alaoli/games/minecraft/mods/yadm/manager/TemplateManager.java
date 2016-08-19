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
import de.alaoli.games.minecraft.mods.yadm.data.Template;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldProviderSetting;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;

public class TemplateManager extends AbstractManager
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	public static final TemplateManager instance = new TemplateManager();
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	private TemplateManager() 
	{
		super( "templates" );
	}
	
	/**
	 * Create Overworld, Nether and The End as default templates
	 */
	private void createDefaultTemplates()
	{
		ManageableGroup vanilla = new TemplateGroup( "vanilla" );
		
		Template template = new Template( "overworld" );
		template.add( new WorldProviderSetting( WorldProviderSetting.OVERWORLD ) );
		vanilla.add( template );
		
		template = new Template( "nether" );
		template.add( new WorldProviderSetting( WorldProviderSetting.NETHER ) );
		vanilla.add( template );
		
		template = new Template( "end" );
		template.add( new WorldProviderSetting( WorldProviderSetting.END ) );
		vanilla.add( template );

		this.add( vanilla );
		this.dirty = true;
		this.save();
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
					.append( File.separator)
					.append( data.getManageableName() )
					.append( ".json" );
				
				try 
				{
					writer = new OutputStreamWriter( new FileOutputStream( file.toString() ), "UTF-8" );
					((JsonSerializable)data).serialize().writeTo( writer, WriterConfig.PRETTY_PRINT );
					
					writer.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
}
