package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;
import de.alaoli.games.minecraft.mods.yadm.json.SettingJsonAdapter;

public abstract class SettingGroup implements Setting, JsonSerializable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private Map<SettingType, Setting> settings = new HashMap<SettingType, Setting>();
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public void add( Setting setting )
	{
		if( !this.settings.containsKey( setting.getSettingType() ) )
		{
			this.settings.put( setting.getSettingType(), setting );
		}
	}
	
	public void add( Map<SettingType, Setting> settings )
	{
		for( Entry<SettingType, Setting> setting : settings.entrySet() )
		{
			this.add( setting.getValue() );
		}
	}
	
	public void remove( Setting setting )
	{
		if( this.settings.containsKey( setting.getSettingType() ) )
		{
			this.settings.remove( setting.getSettingType() );
		}
	}
	
	public Setting get( SettingType type )
	{
		return this.settings.get( type ); 
	}

	public Setting get( String name )
	{
		return this.settings.get( name ); 
	}
	
	public Map<SettingType, Setting> getAll()
	{
		return this.settings;
	}
	
	public boolean hasSetting( SettingType type )
	{
		return this.settings.containsKey( type );
	}
	
	/********************************************************************************
	 * Methods - Implement JsonSerializable
	 ********************************************************************************/

	@Override
	public JsonElement serialize( JsonSerializationContext context ) 
	{
		JsonObject result = new JsonObject();
		JsonArray arrayJson = new JsonArray();
		
		for( Setting setting : this.settings.values() )
		{
			arrayJson.add( context.serialize( setting, SettingJsonAdapter.class ).getAsJsonObject() );
		}
		result.add( "settings", arrayJson );
		
		return result;
	}

	@Override
	public void deserialize( JsonElement json, JsonDeserializationContext context )
	{
		JsonObject groupJson = json.getAsJsonObject();
		
		if( !groupJson.has( "settings" ) ) { 
			throw new JsonParseException( "Settings required." ); 
		}
		if( !groupJson.get( "settings" ).isJsonArray() ) {
			throw new JsonParseException( "Settings must be an array." );
		}
		JsonArray arrayJson = groupJson.get( "settings" ).getAsJsonArray();
		
		for( JsonElement settingJson : arrayJson )
		{
			this.add( (Setting)context.deserialize( settingJson, SettingJsonAdapter.class ) );
		}
	}	
}
