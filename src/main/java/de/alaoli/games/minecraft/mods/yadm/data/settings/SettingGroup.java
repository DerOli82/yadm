package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;

public abstract class SettingGroup implements Setting, JsonSerializable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	/**
	 * Required
	 */
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
	
	public void update( Setting setting )
	{
		this.settings.put( setting.getSettingType(), setting );
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
	
	/**
	 * Check if a settings is included
	 * 
	 * @return boolean
	 */	
	public boolean hasSetting( SettingType type )
	{
		return this.settings.containsKey( type );
	}
	
	/**
	 * Check if required settings are included
	 * 
	 * @return boolean
	 */
	public boolean hasRequiredSettings()
	{
		for( SettingType type : SettingType.values() )
		{
			if( ( type.required ) && 
				( !this.hasSetting( type ) ) )
			{
				return false;
			}
		}
		return true;
	}
	
	/********************************************************************************
	 * Methods - Implement JsonSerializable
	 ********************************************************************************/

	@Override
	public JsonValue serialize() 
	{
		//Only serialize if all required settings are included
		if( !this.hasRequiredSettings() )
		{
			throw new DataException( "Can't serialize, missing required SettingType." );
		}
		
		JsonArray json = new JsonArray();
		
		for( Setting setting : this.settings.values() )
		{
			if( setting instanceof JsonSerializable )
			{
				json.add( ((JsonSerializable)setting).serialize() );
			}
		}
		return json;
	}

	@Override
	public void deserialize( JsonValue json )
	{
		if( !json.isObject() ) { throw new DataException( "SettingGroup isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();
		
		if( obj.get( "settings" ) == null ) { throw new DataException( "SettingGroup 'settings' are missing." ); }
		if( !obj.get( "settings" ).isArray() ) { throw new DataException( "SettingGroup 'settings' isn't an array." ); }
				
		Setting setting;
		JsonObject settingObj;
		JsonArray settingArray = obj.get( "settings" ).asArray();
		
		for( JsonValue settingValue : settingArray )
		{
			if( !settingValue.isObject() ) { throw new DataException( "SettingGroup 'setting' isn't a JsonObject." ); }
			
			settingObj = settingValue.asObject();
			
			if( settingObj.get( "type" ) == null ) { throw new DataException( "Setting 'type' is missing." ); }
			if( !settingObj.get( "type" ).isString() ) { throw new DataException( "Setting 'type' isn't a string." ); }
				
			setting = SettingFactory.createNewInstance( settingObj.get( "type" ).asString() );
			
			if( setting == null ) { throw new DataException( "SettingType is unknown." ); }
			
			((JsonSerializable)setting).deserialize( settingValue );
			this.add( setting );
		}
		if( !this.hasRequiredSettings() ) { throw new DataException( "SettingGroup required settings are missing." ); }
	}	
}
