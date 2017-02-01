package de.alaoli.games.minecraft.mods.yadm.config;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.lib.common.config.Section;
import de.alaoli.games.minecraft.mods.lib.common.data.DataException;

public class ConfigDimensionSection implements Section
{
	/**
	 * Use as first dimension id with default 1000
	 */
	public static int beginsWithId = 1000;
	
	/**
	 * Teleport to new dimension after create command with default true
	 */
	public static boolean teleportOnCreate = true;

	/********************************************************************************
	 * Method - Implements Section
	 ********************************************************************************/
	
	@Override
	public String getSectionName() 
	{
		return "Dimension";
	}

	/********************************************************************************
	 * Method - Implements JsonSerializable
	 ********************************************************************************/
	
	@Override
	public JsonValue serialize() throws DataException { return null; }

	@Override
	public void deserialize( JsonValue json ) throws DataException 
	{
		if( !json.isObject() ) { throw new DataException( "ConfigDimensionSection isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();
		
		if( obj.get( "beginsWithId" ) != null )
		{
			if( !obj.get( "beginsWithId" ).isNumber() ) { throw new DataException( "ConfigDimensionSection 'beginsWithId' isn't a number." ); }
			
			beginsWithId = obj.get( "beginsWithId" ).asInt();
		}
		if( obj.get( "teleportOnCreate" ) != null )
		{
			if( !obj.get( "teleportOnCreate" ).isBoolean() ) { throw new DataException( "ConfigDimensionSection 'teleportOnCreate' isn't an boolean." ); }
			
			teleportOnCreate = obj.get( "teleportOnCreate" ).asBoolean();
		}		
	}
}
