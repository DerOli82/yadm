package de.alaoli.games.minecraft.mods.yadm.data.settings.worldborder;

import java.util.HashSet;
import java.util.Set;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.data.settings.BorderSide;
import de.alaoli.games.minecraft.mods.yadm.data.settings.Setting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.event.PerformWorldBorderEvent;
import de.alaoli.games.minecraft.mods.yadm.event.WorldBorderEvent;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class MessageSetting implements Setting, PerformWorldBorderEvent, JsonSerializable 
{
	private String message;
	
	/********************************************************************************
	 * Methods - Implement JsonSerializable
	 ********************************************************************************/	

	@Override
	public SettingType getSettingType() 
	{
		return SettingType.WORLDBORDER_MESSAGE;
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
		String setting = this.getSettingType().toString();
		JsonObject json = new JsonObject();
		
		json.add( "type", setting.substring( setting.indexOf( ":" ) + 1, setting.length() ) );
		json.add( "message", this.message );
		
		return json;
	}

	@Override
	public void deserialize(JsonValue json) throws DataException 
	{
		if( !json.isObject() ) { throw new DataException( "MessageSetting isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();
		
		if( obj.get( "message" ) == null ) { throw new DataException( "MessageSetting 'message' is missing." ); }
		if( !obj.get( "message" ).isString() ) { throw new DataException( "MessageSetting 'message' isn't a string." ); }
		
		this.message = obj.get( "message" ).asString(); 
	}

	/********************************************************************************
	 * Methods - Implement WorldBorderAction
	 ********************************************************************************/
	
	@Override
	public int priority()
	{
		return 0;
	}
	
	@Override
	public Set<BorderSide> allowedBorderSides()
	{
		Set<BorderSide> allowed = new HashSet<BorderSide>();
		
		for( BorderSide side : BorderSide.values() )
		{
			allowed.add( side );
		}
		return allowed;
	}
	
	@Override
	public void performWorldBorderEvent( WorldBorderEvent event ) 
	{
		EntityPlayer player = (EntityPlayer)event.chunkEvent.entity;
		
		player.addChatComponentMessage( new ChatComponentText( this.message ) );
	}

}
