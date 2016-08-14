package de.alaoli.games.minecraft.mods.yadm.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingGroup;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;
import de.alaoli.games.minecraft.mods.yadm.manager.Manageable;

public class Template extends SettingGroup implements Manageable, JsonSerializable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private String groupName; 
	
	private String name;

	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public Template() {}
	
	public Template( String name, String groupName )
	{
		this.name = name;
		this.groupName = groupName;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	/********************************************************************************
	 * Methods - Implement Manageable
	 ********************************************************************************/

	@Override
	public String getManageableGroupName() 
	{
		return this.groupName;
	}
	
	@Override
	public String getManageableName() 
	{
		return this.name;
	}

	/********************************************************************************
	 * Methods - Implement Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType() 
	{
		return null;
	}

	@Override
	public boolean isRequired() 
	{
		return true;
	}
	
	/********************************************************************************
	 * Methods - Implement JsonSerializable
	 ********************************************************************************/

	@Override
	public JsonElement serialize( JsonSerializationContext context ) 
	{
		JsonObject result = super.serialize( context ).getAsJsonObject();
		
		result.addProperty( "name", this.name );
		
		return result;
	}

	@Override
	public void deserialize( JsonElement json, JsonDeserializationContext context )
	{
		JsonObject data = json.getAsJsonObject();
		
		if( !data.has( "name" ) ) { 
			throw new JsonParseException( "Template requires a name." ); 
		}
		this.name = data.get( "name" ).getAsString();
		super.deserialize( json, context );
	}
}
