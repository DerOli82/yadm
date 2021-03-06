package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.yadm.YADMException;
import de.alaoli.games.minecraft.mods.yadm.comparator.PriorityComparator;
import de.alaoli.games.minecraft.mods.yadm.data.ChunkCoordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.event.PerformWorldBorderEvent;
import de.alaoli.games.minecraft.mods.yadm.event.WorldBorderEvent;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;
import net.minecraft.util.ChatComponentText;

public class WorldBorderSetting implements Setting, JsonSerializable, PerformWorldBorderEvent
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

	private Map<BorderSide, PriorityQueue<PerformWorldBorderEvent>> actions;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public WorldBorderSetting()
	{
		this.actions = new HashMap<BorderSide, PriorityQueue<PerformWorldBorderEvent>>();
		
		for( BorderSide side : BorderSide.values() )
		{
			this.actions.put(side, new PriorityQueue<PerformWorldBorderEvent>( new PriorityComparator()) );
		}
	}
	
	public void addAction( BorderSide side, PerformWorldBorderEvent action )
	{
		if( action.allowedBorderSides().contains( side ) ) { new DataException( "BorderSide '" + side + "' not allowed for this action" ); }
		
		this.actions.get( side ).add( action );
	}
	
	public void removeAction( BorderSide side, PerformWorldBorderEvent action )
	{
		this.actions.get( side ).remove( action );
	}
	
	public PerformWorldBorderEvent getAction( BorderSide side, Class<? extends PerformWorldBorderEvent> clazz )
	{
		for( PerformWorldBorderEvent action : this.actions.get( side ) )
		{
			if( action.getClass() == clazz )
			{
				return action;
			}
		}
		return null;
	}
	
	public boolean hasAction( BorderSide side, Class<? extends PerformWorldBorderEvent> clazz )
	{
		return this.getAction( side, clazz ) != null;
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
	public boolean isSouthBorder( ChunkCoordinate toCheck )
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
	public boolean isNorthBorder( ChunkCoordinate toCheck )
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

	public Coordinate toCoordinate( BorderSide side, int y ) throws DataException
	{
		int x = 0; 
		int z = 0;
		
		switch( side )
		{
			case NORTH :
				x = 16 * this.pointCenter.x;
				z = 16 * ( this.pointCenter.z - this.radius + 1 );
				break;
				
			case EAST :
				x = 16 * ( this.pointCenter.x + this.radius - 1 );
				z = 16 * this.pointCenter.z;
				break;
				
			case SOUTH :
				x = 16 * this.pointCenter.x;
				z = 16 * ( this.pointCenter.x + this.radius - 1 );
				break;
				
			case WEST :
				x = 16 * ( this.pointCenter.x - this.radius + 1 ); 
				z = 16 * this.pointCenter.z;
				break;
				
			default :
				throw new DataException( "Invalid border side." );
					
		}
		return new Coordinate( x, y, z );
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
		PriorityQueue<PerformWorldBorderEvent> actions;
		
		JsonArray array = new JsonArray();
		
		for( Entry<BorderSide, PriorityQueue<PerformWorldBorderEvent>> entry : this.actions.entrySet() )
		{
			side = entry.getKey();
			actions = entry.getValue();
			
			for( PerformWorldBorderEvent action : actions )
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
			
			if( !(setting instanceof PerformWorldBorderEvent) ) { throw new DataException( "WorldBorderSetting unknown 'action'." ); }
			if ( this.actions.get( side ).contains( setting ) ) { throw new DataException( "WorldBorderSetting duplicate 'action'." ); }
			
			Set<BorderSide> allowedSides = ((PerformWorldBorderEvent)setting).allowedBorderSides();
			
			if( !allowedSides.contains( side ) ) { throw new DataException( "WorldBorderSetting 'action' not allowed for 'side'." ); }
			
			((JsonSerializable)setting).deserialize( value );
			
			this.actions.get( side ).add( (PerformWorldBorderEvent)setting );
		}
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
		//No events inside Border
		if( event.side == BorderSide.INSIDE ) 
		{
			event.setCanceled( true );
			return;
		}
		
		for( PerformWorldBorderEvent action : this.actions.get( event.side ) )
		{
			if( event.isCanceled() ) { return; }
			
			try
			{
				action.performWorldBorderEvent( event );
			}
			catch( YADMException e )
			{
				
				event.player.addChatComponentMessage( new ChatComponentText( e.getMessage() ) );
			}
		}
		
		//All sides action
		if( ( event.side == BorderSide.NORTH ) ||
			( event.side == BorderSide.EAST ) ||
			( event.side == BorderSide.SOUTH ) ||
			( event.side == BorderSide.WEST ) )
		{
			for( PerformWorldBorderEvent action : this.actions.get( BorderSide.ALL ) )
			{
				if( event.isCanceled() ) { return; }
				
				try
				{
					action.performWorldBorderEvent( event );
				}
				catch( YADMException e )
				{
					event.player.addChatComponentMessage( new ChatComponentText( e.getMessage() ) );
				}
			}	
		}
	}
}
