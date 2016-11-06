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

	private String group;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public Template() {}
	
	public Template( String group, String name )
	{
		this.group = group;
		this.name = name;
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
	public void setManageableGroupName( String name ) 
	{
		this.group = name;
	}
	
	@Override
	public String getManageableGroupName() 
	{
		return this.group;
	}
	
	@Override
	public void setManageableName( String name ) 
	{
		this.name = name;
	}
	
	@Override
	public String getManageableName() 
	{
		return this.name;
	}
	
	/********************************************************************************
	 * Methods - Implement JsonSerializable
	 ********************************************************************************/

	@Override
	public JsonValue serialize() throws DataException
	{
		JsonObject json = new JsonObject();
		
		json.add( "name", this.name );
		json.add( "settings", super.serialize().asArray() );
		
		return json;
	}

	@Override
	public void deserialize( JsonValue json ) throws DataException
	{
		try
		{
			if( !json.isObject() ) { throw new DataException( "Dimension isn't a JsonObject." ); }
			
			JsonObject obj = json.asObject();
			
			if( obj.get( "name" ) == null ) { throw new DataException( "Dimension 'name' is missing." ); }
			if( !obj.get( "name" ).isString() ) { throw new DataException( "Dimension 'name' isn't a string." ); }
			
			this.name = obj.get( "name" ).asString();
		
			super.deserialize( json );
			
		}
		catch( DataException e )
		{
			throw new DataException( "Deserialization Exception in: " + this.group + ":" + this.name, e );
		}			
	}
}
