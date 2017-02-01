package de.alaoli.games.minecraft.mods.yadm.config;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.lib.common.config.Section;
import de.alaoli.games.minecraft.mods.lib.common.data.DataException;

public class ConfigProviderSection implements Section
{
	/**
	 * Use as first provider id with default 500
	 */
	public static int beginsWithId = 500;	

	/********************************************************************************
	 * Method - Implements Section
	 ********************************************************************************/
	
	@Override
	public String getSectionName() 
	{
		return "WorldProvider";
	}

	/********************************************************************************
	 * Method - Implements JsonSerializable
	 ********************************************************************************/
	
	@Override
	public JsonValue serialize() throws DataException { return null; }

	@Override
	public void deserialize( JsonValue json ) throws DataException 
	{
		if( !json.isObject() ) { throw new DataException( "ConfigProviderSection isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();
		
		if( obj.get( "beginsWithId" ) != null )
		{
			if( !obj.get( "beginsWithId" ).isNumber() ) { throw new DataException( "ConfigProviderSection 'beginsWithId' isn't a number." ); }
			
			beginsWithId = obj.get( "beginsWithId" ).asInt();
		}
	}
}
