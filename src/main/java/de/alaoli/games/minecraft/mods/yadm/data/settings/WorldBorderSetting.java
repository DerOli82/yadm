package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import de.alaoli.games.minecraft.mods.yadm.data.ChunkCoordinate;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.event.WorldBorderAction;
import de.alaoli.games.minecraft.mods.yadm.event.WorldBorderEvent;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;

public class WorldBorderSetting implements Setting, JsonSerializable, WorldBorderAction
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
	
	private int radius;

	private Map<BorderSide, Set<WorldBorderAction>> actions;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public WorldBorderSetting()
	{
		this.actions = new HashMap<BorderSide, Set<WorldBorderAction>>();
		
		for( BorderSide side : BorderSide.values() )
		{
			this.actions.put(side, new HashSet<WorldBorderAction>() );
		}
	}
	
	public ChunkCoordinate getPointCenter() 
	{
		return this.pointCenter;
	}

	public ChunkCoordinate getPointA() 
	{
		return this.pointA;
	}

	public ChunkCoordinate getPointB() 
	{
		return this.pointB;
	}

	public ChunkCoordinate getPointC() 
	{
		return this.pointC;
	}

	public ChunkCoordinate getPointD() 
	{
		return this.pointD;
	}

	public int getRadius() 
	{
		return this.radius;
	}

	public boolean isInsideBorder( ChunkCoordinate toCheck )
	{
		return (( toCheck.x >= this.pointA.x ) && 
				( toCheck.x <= this.pointB.x ) &&
				( toCheck.z <= this.pointA.z ) &&
				( toCheck.z >= this.pointC.z ));
	}
	
	/**
	 * A > x > B
	 * 
	 * @param toCheck
	 * @return
	 */
	public boolean isNorthBorder( ChunkCoordinate toCheck )
	{
		return (( toCheck.x >= this.pointA.x ) && 
				( toCheck.x <= this.pointB.x ) &&
				( toCheck.z == this.pointA.z ));
	}
	
	/**
	 * B > z > D
	 * 
	 * @param toCheck
	 * @return
	 */
	public boolean isEastBorder( ChunkCoordinate toCheck )
	{
		return (( toCheck.z <= this.pointB.z ) &&
				( toCheck.z >= this.pointD.z ) &&
				( toCheck.x == this.pointB.x ));
	}
	
	/**
	 * C > x > D
	 * 
	 * @param toCheck
	 * @return
	 */
	public boolean isSouthBorder( ChunkCoordinate toCheck )
	{
		return (( toCheck.x >= this.pointC.x ) && 
				( toCheck.x <= this.pointD.x ) &&
				( toCheck.z == this.pointC.z ));
	}
	
	/**
	 * A > z > C
	 * 
	 * @param toCheck
	 * @return
	 */
	public boolean isWestBorder( ChunkCoordinate toCheck )
	{
		return (( toCheck.z <= this.pointA.z ) &&
				( toCheck.z >= this.pointC.z ) &&
				( toCheck.x == this.pointA.x ));
	}
	
	/*
	 * A -> B
	 * |    |
	 * C <- D
	 */
	public BorderSide intersectsBorder( ChunkCoordinate toCheck )
	{
		if( !this.isInsideBorder( toCheck ) )
		{
			return BorderSide.OUTSIDE;
		}
		else
		{
			if( this.isNorthBorder( toCheck ) )
			{
				return BorderSide.NORTH;
			}
			else if( this.isEastBorder( toCheck ) )
			{
				return BorderSide.EAST;
			}
			else if( this.isSouthBorder( toCheck ) )
			{
				return BorderSide.SOUTH;
			}
			else if( this.isWestBorder( toCheck ) )
			{
				return BorderSide.WEST;
			}
			else
			{
				return BorderSide.INSIDE;
			}
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

		JsonObject obj;
		BorderSide side;
		Set<WorldBorderAction> actions;
		
		JsonArray array = new JsonArray();
		
		for( Entry<BorderSide, Set<WorldBorderAction>> entry : this.actions.entrySet() )
		{
			side = entry.getKey();
			actions = entry.getValue();
			
			for( WorldBorderAction action : actions )
			{
				if( action instanceof JsonSerializable )
				{
					obj = (JsonObject)((JsonSerializable)action).serialize();
					obj.add( "side", side.toString() );
					array.add( obj );
				}
			}
		}
		json.add( "actions", array );
		
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
		
		if( obj.get( "actions" ) == null ) { throw new DataException( "WorldBorderSetting 'actions' is missing." ); }
		if( !obj.get( "actions" ).isArray() ) { throw new DataException( "WorldBorderSetting 'actions' isn't an array." ); }
	
		Setting setting;
		BorderSide side;
		
		for( JsonValue value : obj.get( "actions" ).asArray() )
		{
			if( !value.isObject() ) { throw new DataException( "WorldBorderSetting 'action' isn't a JsonObject." ); }
			
			JsonObject action = value.asObject();
			
			if( action.get( "type" ) == null ) { throw new DataException( "WorldBorderSetting 'action' value 'type' is missing." ); }
			if( !action.get( "type" ).isString() ) { throw new DataException( "WorldBorderSetting 'action' value 'type' isn't a string." ); }
			
			if( action.get( "side" ) == null ) { throw new DataException( "WorldBorderSetting 'action' value 'side' is missing." ); }
			if( !action.get( "side" ).isString() ) { throw new DataException( "WorldBorderSetting 'action' value 'side' isn't a string." ); }
			
			setting = SettingFactory.createNewInstance( this.getSettingType().toString() + ":" + action.get( "type" ).asString() );
			side = BorderSide.get( action.get( "side" ).asString() );
			
			if( !(setting instanceof WorldBorderAction) ) { throw new DataException( "WorldBorderSetting unknown 'action'." ); }
			if ( this.actions.get( side ).contains( setting ) ) { throw new DataException( "WorldBorderSetting duplicate 'action'." ); }
			
			Set<BorderSide> allowedSides = ((WorldBorderAction)setting).allowedBorderSides();
			
			if( !allowedSides.contains( side ) ) { throw new DataException( "WorldBorderSetting 'action' not allowed for 'side'." ); }
			
			((JsonSerializable)setting).deserialize( value );
			
			this.actions.get( side ).add( (WorldBorderAction)setting );
		}
	}


	/********************************************************************************
	 * Methods - Implement WorldBorderAction
	 ********************************************************************************/
	
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
	public void performAction( WorldBorderEvent event ) 
	{
		for( WorldBorderAction action : this.actions.get( event.side ) )
		{
			if( event.isCanceled() ) { return; }
			
			action.performAction( event );
		}
		
		//All sides action
		if( ( event.side == BorderSide.NORTH ) ||
			( event.side == BorderSide.EAST ) ||
			( event.side == BorderSide.SOUTH ) ||
			( event.side == BorderSide.WEST ) )
		{
			for( WorldBorderAction action : this.actions.get( BorderSide.ALL ) )
			{
				if( event.isCanceled() ) { return; }
				
				action.performAction( event );
			}	
		}
	}
	
}
