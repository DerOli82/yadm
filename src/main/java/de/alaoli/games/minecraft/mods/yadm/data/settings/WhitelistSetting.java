package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.data.Player;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;

public class WhitelistSetting implements Setting, JsonSerializable
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	private Map<UUID, Player> users;
	
	/********************************************************************************
	 * Methods 
	 ********************************************************************************/
	
	public WhitelistSetting()
	{
		this.users = new HashMap<UUID, Player>();
	}
	
	public void add( Player player )
	{
		this.users.put( player.getId(), player );
	}
	
	public void remove( Player player )
	{
		this.users.remove( player.getId() );
	}
	
	public boolean exists( Player player )
	{
		return this.users.containsKey( player.getId() );
	}
	
	public Set<Entry<UUID,Player>> getUsers()
	{
		return this.users.entrySet();
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
		
		for( Entry<UUID, Player> entry : this.users.entrySet() )
		{
			array.add( entry.getValue().serialize() );
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
		
		Player player;
		JsonArray users = obj.get( "users" ).asArray();
		
		for( JsonValue value : users )
		{
			player = new Player();
			
			player.deserialize( value );
			
			this.add( player );
		}
	}	
}