package de.alaoli.games.minecraft.mods.yadm.data.settings;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import de.alaoli.games.minecraft.mods.yadm.data.ChunkCoordinate;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;

public class WorldBorderSetting implements Setting, JsonSerializable 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private ChunkCoordinate pointCenter;
	
	/*
	 * A -> B
	 * |    |
	 * C <- D
	 */
	private ChunkCoordinate pointA;
	private ChunkCoordinate pointB;
	private ChunkCoordinate pointC;
	private ChunkCoordinate pointD;
	
	int radius;
	
	private String warningMessage;

	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	/*
	 * A -> B
	 * |    |
	 * C <- D
	 */
	public BorderSide intersectBorder( ChunkCoordinate oldChunk, ChunkCoordinate newChunk )
	{
		if( this.isIntersection( this.pointA, this.pointB, oldChunk, newChunk ) )
		{
			return BorderSide.NORTH;
		}
		else if( this.isIntersection( this.pointB, this.pointD, oldChunk, newChunk ) )
		{
			return BorderSide.EAST;
		}
		else if( this.isIntersection( this.pointD, this.pointC, oldChunk, newChunk ) )
		{
			return BorderSide.SOUTH;
		}
		else if( this.isIntersection( this.pointC, this.pointA, oldChunk, newChunk ) )
		{
			return BorderSide.WEST;
		}
		else
		{
			return null;
		}
	}

	protected boolean isIntersection( ChunkCoordinate pointA, ChunkCoordinate pointB, ChunkCoordinate oldChunk, ChunkCoordinate newChunk  )
	{
		float x1 = pointA.x, 
			z1 = pointA.z, 
			x2 = pointB.x, 
			z2 = pointB.z, 
			x3 = oldChunk.x, 
			z3 = oldChunk.z, 
			x4 = newChunk.x, 
			z4 = newChunk.z;
		
		float d = ( z4 - z3 ) * ( x2 - x1 ) - ( x4 - x3 ) * ( z2 - z1 );
		
		if ( d == 0 ) { return false; }
		
		float ua = ( ( x4 - x3 ) * ( z1 - z3 ) - ( z4 - z3 ) * ( x1 - x3 ) ) / d;
		float intersectX = x1 + ( x2 - x1 ) * ua;
		float intersectZ = z1 + ( z2 - z1 ) * ua;
	
		if( ( intersectX == x4 ) && ( intersectZ == z4 ) )
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
	public JsonValue serialize() throws DataException
	{
		JsonObject json = new JsonObject();
		JsonObject center = new JsonObject();
		
		json.add( "type", this.getSettingType().toString() );
		center.add( "x", this.pointCenter.x );
		center.add( "z", this.pointCenter.z );
		json.add( "center", center );
		json.add( "radius", this.radius );
		
		return json;
	}

	@Override
	public void deserialize( JsonValue json ) throws DataException
	{
		if( !json.isObject() ) { throw new DataException( "WorldBorderSetting isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();
		
		if( obj.get( "center" ) == null ) { throw new DataException( "WorldBorderSetting 'center' coordinate is missing." ); }
		if( !obj.get( "center" ).isObject() ) { throw new DataException( "WorldBorderSetting 'center' coordinate isn't a JsonObject." ); }
		
		JsonObject center = obj.get( "center" ).asObject();
		
		if( center.get( "x" ) == null ) { throw new DataException( "WorldBorderSetting 'center' coordinate 'x' is missing." ); }
		if( !center.get( "x" ).isNumber() ) { throw new DataException( "WorldBorderSetting 'center' coordinate 'x' isn't a number." ); }
		if( center.get( "z" ) == null ) { throw new DataException( "WorldBorderSetting 'center' coordinate 'z' is missing." ); }
		if( !center.get( "z" ).isNumber() ) { throw new DataException( "WorldBorderSetting 'center' coordinate 'z' isn't a number." ); }

		if( obj.get( "radius" ) == null ) { throw new DataException( "WorldBorderSetting 'radius' is missing." ); }
		if( !obj.get( "radius" ).isNumber() ) { throw new DataException( "WorldBorderSetting 'radius' isn't a number." ); }
		
		this.pointCenter = new ChunkCoordinate(center.get( "x" ).asInt(), center.get( "z" ).asInt() );
		this.radius = obj.get( "radius" ).asInt(); 
		
		this.pointA = new ChunkCoordinate( -1*this.radius + this.pointCenter.x, this.radius + this.pointCenter.z);
		this.pointB = new ChunkCoordinate( this.radius + this.pointCenter.x, this.radius + this.pointCenter.z);
		this.pointC = new ChunkCoordinate( -1*this.radius + this.pointCenter.x, -1*this.radius + this.pointCenter.z);
		this.pointD = new ChunkCoordinate( this.radius + this.pointCenter.x, -1*this.radius + this.pointCenter.z);
	}
	
}
