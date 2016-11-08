package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;
import net.minecraft.entity.player.EntityPlayer;

public class WhitelistSetting implements Setting, JsonSerializable
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	private Map<UUID, String> users;
	
	/********************************************************************************
	 * Methods 
	 ********************************************************************************/
	
	public WhitelistSetting()
	{
		this.users = new HashMap<UUID, String>();
	}
	
	public void add( EntityPlayer player )
	{
		this.users.put( player.getUniqueID(), player.getDisplayName() );
	}
	
	public void remove( EntityPlayer player )
	{
		this.users.remove( player.getUniqueID() );
	}
	
	public boolean exists( EntityPlayer player )
	{
		return this.users.containsKey( player.getUniqueID() );
	}
	
	/********************************************************************************
	 * Methods - Implement Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType()
	{
		return SettingType.WHITELIST;
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
	public JsonValue serialize() throws DataException
	{
		JsonObject user;
		JsonObject json = new JsonObject();
		JsonArray array = new JsonArray();
		
		for( Entry<UUID, String> entry : this.users.entrySet() )
		{
			user = new JsonObject();
			
			user.add( "uuid", entry.getKey().toString() );
			user.add( "name", entry.getValue() );
			
			array.add( user );
		}
		json.add( "type", this.getSettingType().toString() );
		json.add( "users", array );
		
		return json;
	}

	@Override
	public void deserialize( JsonValue json ) throws DataException
	{
		this.users.clear();
		
		if( !json.isObject() ) { throw new DataException( "WhitelistSetting isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();
		
		if( obj.get( "users" ) == null ) { throw new DataException( "WhitelistSetting 'users' is missing." ); }
		if( !obj.get( "users" ).isArray() ) { throw new DataException( "WhitelistSetting 'users' isn't an array." ); }
		
		JsonObject valueObj;
		JsonArray users = obj.get( "users" ).asArray();
		
		for( JsonValue value : users )
		{
			if( !value.isObject() ) { throw new DataException( "WhitelistSetting 'users' value isn't a JsonObject." ); }
			
			valueObj = value.asObject();
			
			if( valueObj.get( "uuid" ) == null ) { throw new DataException( "WhitelistSetting 'users' value 'uuid' is missing." ); }
			if( !valueObj.get( "uuid" ).isString() ) { throw new DataException( "WhitelistSetting 'users' value 'uuid' isn't a string." ); }
			
			if( valueObj.get( "name" ) == null ) { throw new DataException( "WhitelistSetting 'users' value 'name' is missing." ); }
			if( !valueObj.get( "name" ).isString() ) { throw new DataException( "WhitelistSetting 'users' value 'name' isn't a string." ); }
			
			this.users.put( UUID.fromString( valueObj.get( "uuid" ).asString() ), valueObj.get( "name" ).asString() );
		}
	}	
}
