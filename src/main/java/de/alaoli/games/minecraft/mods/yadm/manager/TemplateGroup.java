package de.alaoli.games.minecraft.mods.yadm.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.WriterConfig;

import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.data.Template;
import de.alaoli.games.minecraft.mods.yadm.json.JsonFileAdapter;

public class TemplateGroup extends ManageableGroup implements JsonFileAdapter
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/

	private String savePath;
	
	private boolean dirty;

	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public TemplateGroup( String name )
	{
		super( name );
		this.dirty = false;
	}

	public TemplateGroup( String name, String savePath )
	{
		super( name );
		this.savePath = savePath;
		this.dirty = false;
	}
		
	/********************************************************************************
	 * Methods - Implement ManageableGroup
	 ********************************************************************************/

	@Override
	public Manageable create() 
	{
		Manageable manageable = new Template();
		manageable.setManageableGroupName( this.getManageableGroupName() );
		
		return manageable;
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
		return this.dirty;
	}

	@Override
	public void save() throws IOException, DataException
	{
		Manageable data;
		String fileName = this.getSavePath() + File.separator + this.getManageableGroupName() + ".json";
		
		try 
		{
			OutputStreamWriter writer = new OutputStreamWriter( new FileOutputStream( fileName ), "UTF-8" );
			
			this.serialize().writeTo( writer, WriterConfig.PRETTY_PRINT );
			
			writer.close();
		} 
		catch ( IOException | DataException e )
		{
			e.printStackTrace();
		}
	}

	@Override
	public void load() throws IOException, DataException 
	{
		Manageable data;
		String fileName = this.getSavePath() + File.separator + this.getManageableGroupName() + ".json";		
		
		try
		{
			InputStreamReader reader = new InputStreamReader( new FileInputStream( fileName ), "UTF-8" );
			
			this.deserialize( Json.parse( reader ) );
			
			reader.close();
		} 
		catch ( IOException | DataException e )
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void cleanup()
	{
		this.clear();
	}
}
