package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.util.HashSet;
import java.util.Set;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.yadm.comparator.BlockComparator;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;

public class WorldGuardSetting implements Setting, JsonSerializable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/

	private boolean whitelistAllowed = false;
	private boolean interactionAllowed = true;
	private boolean allowFood = true;
	
	private Set<BlockComparator> leftClickWhitelist = new HashSet<BlockComparator>();
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public boolean isWhitelistAllowed()
	{
		return this.whitelistAllowed;
	}
	
	public boolean isInteractionAllowed()
	{
		return this.interactionAllowed;

	}
	
	public boolean isFoodAllowed()
	{
		return this.allowFood;
	}
	
	public boolean isLeftClickAllowed( BlockComparator block )
	{
		if( this.leftClickWhitelist.isEmpty() ) { return false; }
		
		return this.leftClickWhitelist.contains( block );
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
		json.add( "allowFood", this.allowFood );
		
		//Optional
		if( !this.leftClickWhitelist.isEmpty() )
		{
			JsonArray array = new JsonArray();
			
			for( BlockComparator block : this.leftClickWhitelist )
			{
				array.add( block.toString() );
			}
			json.add( "leftClickWhitelist", array );
		}
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
		
		//Optional
		if( obj.get( "allowFood" ) != null )
		{
			if( !obj.get( "allowFood" ).isBoolean() ) { throw new DataException( "WorldGuardSetting 'allowFood' isn't an boolean." ); }
			
			this.allowFood = obj.get( "allowFood" ).asBoolean();
		}			
		
		//Optional
		if( obj.get( "leftClickWhitelist" ) != null )
		{
			if( !obj.get( "leftClickWhitelist" ).isArray() ) { throw new DataException( "WorldGuardSetting 'leftClickWhitelist' isn't an array." ); }
			
			for( JsonValue value : obj.get( "leftClickWhitelist" ).asArray() )
			{
				this.leftClickWhitelist.add( new BlockComparator( value.asString() ) );
			}
		}
	}		
}
