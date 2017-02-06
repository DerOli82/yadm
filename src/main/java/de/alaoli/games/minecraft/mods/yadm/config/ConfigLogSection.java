package de.alaoli.games.minecraft.mods.yadm.config;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.lib.common.config.Section;
import de.alaoli.games.minecraft.mods.lib.common.data.DataException;

public class ConfigLogSection implements Section
{

	/**
	 * Log debug messages with default false
	 */
	public static boolean debugging = false;

	/********************************************************************************
	 * Method - Implements Section
	 ********************************************************************************/
	
	@Override
	public String getSectionName() 
	{
		return "Log";
	}

	/********************************************************************************
	 * Method - Implements JsonSerializable
	 ********************************************************************************/
	
	@Override
	public JsonValue serialize() throws DataException { return null; }

	@Override
	public void deserialize( JsonValue json ) throws DataException 
	{
		if( !json.isObject() ) { throw new DataException( "ConfigLogSection isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();
		
		if( obj.get( "debugging" ) != null )
		{
			if( !obj.get( "debugging" ).isBoolean() ) { throw new DataException( "ConfigLogSection 'debugging' isn't an boolean." ); }
			
			debugging = obj.get( "debugging" ).asBoolean();
		}		
	}
}
