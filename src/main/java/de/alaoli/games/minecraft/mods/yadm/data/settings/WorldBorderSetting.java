package de.alaoli.games.minecraft.mods.yadm.data.settings;

import javax.vecmath.Vector2d;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.yadm.data.ChunkCoordinate;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;

public class WorldBorderSetting implements Setting, JsonSerializable 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private ChunkCoordinate pointA;
	private ChunkCoordinate pointB;
	private ChunkCoordinate pointC;
	private ChunkCoordinate pointD;
	
	private int warningZone;
	private String warningMessage;

	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	/**
	 * Validate if points form a rectangle
	 * 
	 * A -> B
	 * |    |
	 * C <- D
	 * 
	 * @return
	 */
	public boolean validatePoints()
	{
		Vector2d ab = this.pointB.vector( this.pointA );
		Vector2d bd = this.pointD.vector( this.pointB );
		Vector2d dc = this.pointC.vector( this.pointD );
		Vector2d ca = this.pointA.vector( this.pointC );
		
		if( ( ab.dot( bd ) == 0.0 ) && 
			( bd.dot( dc ) == 0.0 ) && 
			( dc.dot( ca ) == 0.0 ) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/********************************************************************************
	 * Methods - Implement Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType()
	{
		return SettingType.WORLDBORDER;
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
		
		/**
		 * @TODO
		 */
		
		
		return json;
	}

	@Override
	public void deserialize( JsonValue json )
	{
		/**
		 * @TODO
		 */
	}
	
}
