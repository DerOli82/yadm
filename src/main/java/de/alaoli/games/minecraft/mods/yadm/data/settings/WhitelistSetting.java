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
import de.alaoli.games.minecraft.mods.yadm.manager.PlayerManager;
import de.alaoli.games.minecraft.mods.yadm.manager.player.FindPlayer;

public class WhitelistSetting implements Setting, JsonSerializable
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	protected static final FindPlayer players = PlayerManager.INSTANCE;
	
	private Map<UUID, Player> users;
	
	private boolean editable;
	
	/********************************************************************************
	 * Methods 
	 ********************************************************************************/
	
	public WhitelistSetting()
	{
		this.users = new HashMap<UUID, Player>();
		this.editable = true;
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
	
	public boolean isEditable()
	{
		return this.editable;
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
			array.add( entry.getValue().getId().toString() );
		}
		json.add( "type", this.getSettingType().toString() );
		json.add( "users", array );
		json.add( "editable", this.editable );
		
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
			if( !value.isString() ) { throw new DataException( "Whitelist 'user' isn't a string." ); }
			
			this.add( players.findPlayer( UUID.fromString( value.asString() ) ) );
		}
		if( obj.get( "editable" ) == null ) { throw new DataException( "WhitelistSetting 'editable' is missing." ); }
		if( !obj.get( "editable" ).isBoolean() ) { throw new DataException( "WhitelistSetting 'editable' isn't an boolean." ); }
		
		this.editable = obj.get( "editable" ).asBoolean();
	}	
}
