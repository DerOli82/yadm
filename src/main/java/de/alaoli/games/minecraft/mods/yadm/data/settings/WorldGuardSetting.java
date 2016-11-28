package de.alaoli.games.minecraft.mods.yadm.data.settings;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;

public class WorldGuardSetting implements Setting, JsonSerializable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/

	private boolean whitelistAllowed;
	private boolean interactionAllowed;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public WorldGuardSetting()
	{
		this.whitelistAllowed = false;
		this.interactionAllowed = true;
	}

	public boolean isWhitelistAllowed()
	{
		return this.whitelistAllowed;
	}
	
	public boolean isInteractionAllowed()
	{
		return this.interactionAllowed;
	}
	
	/********************************************************************************
	 * Methods - Implement Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType()
	{
		return SettingType.WORLDGUARD;
	}

	@Override
	public boolean isRequired() 
	{
		return false;
	}
	
	/********************************************************************************
	 * Methods - Implement JsonSerializable
	 ********************************************************************************/

	@Override
	public JsonValue serialize() 
	{
		JsonObject json = new JsonObject();

		json.add( "type", this.getSettingType().toString() );
		json.add( "whitelistAllowed", this.whitelistAllowed );
		json.add( "interactionAllowed", this.interactionAllowed );
		
		return json;
	}

	@Override
	public void deserialize( JsonValue json )
	{
		if( !json.isObject() ) { throw new DataException( "WorldGuardSetting isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();
		
		//Optional
		if( obj.get( "whitelistAllowed" ) != null )
		{
			if( !obj.get( "whitelistAllowed" ).isBoolean() ) { throw new DataException( "WorldGuardSetting 'whitelistAllowed' isn't an boolean." ); }
			
			this.whitelistAllowed = obj.get( "whitelistAllowed" ).asBoolean();
		}
		//Optional
		if( obj.get( "interactionAllowed" ) != null )
		{
			if( !obj.get( "interactionAllowed" ).isBoolean() ) { throw new DataException( "WorldGuardSetting 'whitelistAllowed' isn't an boolean." ); }
			
			this.interactionAllowed = obj.get( "interactionAllowed" ).asBoolean();
		}		
	}		
}
