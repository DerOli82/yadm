package de.alaoli.games.minecraft.mods.yadm.manager;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.data.Template;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldProviderSetting;
import de.alaoli.games.minecraft.mods.yadm.json.JsonFileAdapter;

public class TemplateManager extends ManageableGroup implements JsonFileAdapter
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	public static final TemplateManager INSTANCE = new TemplateManager();
	
	private String savePath;
	private boolean dirty;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	private TemplateManager() 
	{	
		super( null );
		this.dirty = false;
	}
	
	/**
	 * Create Overworld, Nether and The End as default templates
	 */
	private void createDefaultTemplates()
	{
		ManageableGroup group = new TemplateGroup( "default", this.savePath );
		
		Template template = new Template( "default", "overworld" );
		template.add( new WorldProviderSetting( WorldProviderSetting.OVERWORLD ) );
		group.add( template );
		
		template = new Template( "default", "nether" );
		template.add( new WorldProviderSetting( WorldProviderSetting.NETHER ) );
		group.add( template );
		
		template = new Template( "default", "end" );
		template.add( new WorldProviderSetting( WorldProviderSetting.END ) );
		group.add( template );

		this.add( group );
		this.setDirty( true );
		
		try 
		{
			this.save();
		}
		catch ( DataException | IOException e )
		{
			e.printStackTrace();
		}
	}

	public boolean exists( String group, String name ) 
	{
		Manageable manageable = this.get( group );
		
		if( ( manageable != null ) && 
			( manageable instanceof ManageableGroup ) )
		{
			return ((ManageableGroup)manageable).exists( name );
		}
		else
		{
			return false;
		}
	}
	
	public Manageable get( String group, String name )
	{
		Manageable manageable = this.get( group );
		
		if( ( manageable != null ) && 
			( manageable instanceof ManageableGroup ) )
		{
			return ((ManageableGroup)manageable).get( name );
		}
		else
		{
			return null;
		}
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
	 * Methods - Implement JsonFileAdapter
	 ********************************************************************************/
	
	@Override
	public void setSavePath( String savePath )
	{
		this.savePath = savePath;
	}

	@Override
	public String getSavePath() 
	{
		return this.savePath;
	}

	@Override
	public void setDirty( boolean flag )
	{
		this.dirty = flag;
	}

	@Override
	public boolean isDirty() 
	{
		if( this.dirty ) { return true; }
		
		Manageable data;
		
		for( Entry<String, Manageable> entry : this.getAll() )
		{
			data = entry.getValue();
			
			if( data instanceof JsonFileAdapter )
			{
				if( ((JsonFileAdapter)data).isDirty() ) { return true; }
			}
		}
		return false;
	}

	@Override
	public void save() throws IOException, DataException
	{
		if( !this.isDirty() ) { return; }
		
		Manageable data;
		
		for( Entry<String, Manageable> entry : this.getAll() )
		{
			data = entry.getValue();
			
			if( data instanceof JsonFileAdapter )
			{
				((JsonFileAdapter)data).save();
			}		
		}
	}

	@Override
	public void load() throws IOException, DataException
	{
		//Reload
		if( this.isDirty() )
		{
			this.save();
			this.clear();
		}
		File folder	= new File( this.getSavePath() );
		
		//Create folder and default template
		if( !folder.exists() )
		{
			folder.mkdir();
			this.createDefaultTemplates();
		}
		Manageable data;
		String groupName;
		File[] files = folder.listFiles();
		
		for( File file : files ) 
		{
			if( ( file.isFile() ) && 
				( file.getName().endsWith(".json") ) )
			{
				groupName = file.getName().replace( ".json", "" );
				
				//Initialize group 
				if( this.exists( groupName ) )
				{
					data = this.get( groupName );
				}
				else
				{
					data = new TemplateGroup( groupName, this.getSavePath() ); 
					this.add( data );
				}
				
				if( data instanceof JsonFileAdapter )
				{
					((JsonFileAdapter)data).load();
				}
			}
		}
	}
}
