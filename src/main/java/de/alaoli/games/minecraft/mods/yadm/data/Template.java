package de.alaoli.games.minecraft.mods.yadm.data;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingGroup;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;
import de.alaoli.games.minecraft.mods.yadm.manager.Manageable;

public class Template extends SettingGroup implements Manageable, JsonSerializable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private String name;

	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public Template() {}
	
	public Template( String name )
	{
		this.name = name;
	}
	
	public String getName()
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
	 * Methods - Implement Manageable
	 ********************************************************************************/

	@Override
	public String getManageableName() 
	{
		return this.name;
	}
	
	/********************************************************************************
	 * Methods - Implement JsonSerializable
	 ********************************************************************************/

	@Override
	public JsonValue serialize() 
	{
		JsonObject json = new JsonObject();
		
		json.add( "name", this.name );
		json.add( "settings", super.serialize().asArray() );
		
		return json;
	}

	@Override
	public void deserialize( JsonValue json )
	{
		this.name = json.asObject().get( "name" ).asString();
		
		super.deserialize( json.asObject().get( "settings" ).asArray() );
	}
}
