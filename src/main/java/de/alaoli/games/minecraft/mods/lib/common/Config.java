package de.alaoli.games.minecraft.mods.lib.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.lib.common.config.Section;
import de.alaoli.games.minecraft.mods.lib.common.data.DataException;
import de.alaoli.games.minecraft.mods.lib.common.json.JsonFileAdapter;
import de.alaoli.games.minecraft.mods.lib.common.json.JsonSerializable;

public class Config implements JsonFileAdapter, JsonSerializable
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	private String savePath;
	private Set<Section> sections = new HashSet<>();
	
	/********************************************************************************
	 * Method
	 ********************************************************************************/
	
	public void registerSection( Section section )
	{
		this.sections.add( section );
	}
	
	/********************************************************************************
	 * Method - Implements JsonFileAdapter
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
	public void setDirty( boolean flag ) {}

	@Override
	public boolean isDirty() { return false; }

	@Override
	public void save() throws IOException, DataException {}

	@Override
	public void load() throws IOException, DataException 
	{	
		File config = new File( this.savePath );
		
		if( !config.exists() ) { return; }
		if( !config.isFile() ) { return; }
		
		InputStreamReader reader = new InputStreamReader( new FileInputStream( config ), "UTF-8" );
		
		this.deserialize( Json.parse( reader ) );
		
		reader.close();
	}

	@Override
	public void cleanup() 
	{
		this.sections.clear();
	}

	/********************************************************************************
	 * Method - Implements JsonSerializable
	 ********************************************************************************/
	
	@Override
	public JsonValue serialize() throws DataException { return null; }

	@Override
	public void deserialize( JsonValue json ) throws DataException 
	{
		if( !json.isObject() ) { throw new DataException( "Config isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();
		
		for( Section section : this.sections )
		{
			//Deserialize section
			if( obj.get( section.getSectionName() ) != null )
			{
				section.deserialize( obj.get( section.getSectionName() ) );
			}
		}
		
	}	
}
