package de.alaoli.games.minecraft.mods.yadm.data.settings;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;

public class SpawnSetting implements Setting, JsonSerializable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private Coordinate coordinate;
	
	private TeleportMode mode;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public SpawnSetting() {}
	
	public SpawnSetting( Coordinate coordinate, TeleportMode mode )
	{
		this.coordinate = coordinate;
		this.mode = mode;
	}
	
	public Coordinate getCoordinate() 
	{
		return this.coordinate;
	}

	public TeleportMode getMode() 
	{
		return this.mode;
	}

	/********************************************************************************
	 * Methods - Implement Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType()
	{
		return SettingType.SPAWN;
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
		json.add( "x", this.coordinate.x );
		json.add( "y", this.coordinate.y );
		json.add( "z", this.coordinate.z );
		json.add( "mode", this.mode.toString() );
		
		return json;
	}

	@Override
	public void deserialize( JsonValue json )
	{
		this.coordinate = new Coordinate(
			json.asObject().get( "x" ).asInt(),
			json.asObject().get( "y" ).asInt(),
			json.asObject().get( "z" ).asInt()
		);
		this.mode = TeleportMode.get( json.asObject().get( "mode" ).asString() );
	}	
}
