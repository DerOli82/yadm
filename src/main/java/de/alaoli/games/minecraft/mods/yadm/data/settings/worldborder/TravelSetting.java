package de.alaoli.games.minecraft.mods.yadm.data.settings.worldborder;

import java.util.HashSet;
import java.util.Set;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.DimensionDummy;
import de.alaoli.games.minecraft.mods.yadm.data.settings.BorderSide;
import de.alaoli.games.minecraft.mods.yadm.data.settings.Setting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.event.PerformWorldBorderEvent;
import de.alaoli.games.minecraft.mods.yadm.event.WorldBorderEvent;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;
import de.alaoli.games.minecraft.mods.yadm.manager.PlayerManager;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportPlayer;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportSettings;
import net.minecraft.entity.player.EntityPlayer;

public class TravelSetting implements Setting, PerformWorldBorderEvent, JsonSerializable 
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/	
	
	protected static final YADimensionManager dimensions = YADimensionManager.INSTANCE;
	protected static final TeleportPlayer players = PlayerManager.INSTANCE; 
	
	private int targetId;
	private BorderSide targetSide;
	private boolean editable = true;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public TravelSetting() {}
	
	public TravelSetting( int targetId ) 
	{ 
		this.targetId = targetId;
	}
	
	public TravelSetting( int targetId, BorderSide targetSide ) 
	{ 
		this.targetId = targetId;
		this.targetSide = targetSide;
	}
	
	/********************************************************************************
	 * Methods - Implement JsonSerializable
	 ********************************************************************************/

	@Override
	public SettingType getSettingType() 
	{
		return SettingType.WORLDBORDER_TRAVEL;
	}

	@Override
	public boolean isRequired() 
	{
		return false;
	}
	
	public int getTargetId() 
	{
		return this.targetId;
	}

	public BorderSide getTargetSide() 
	{
		return this.targetSide;
	}
	
	public boolean isEditable()
	{
		return this.editable;
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
		json.add( "targetId", this.targetId );
		
		if( this.targetSide != null )
		{
			json.add( "targetSide", this.targetSide.toString() );
		}
		json.add( "editable", this.editable );
		
		return json;
	}

	@Override
	public void deserialize(JsonValue json) throws DataException 
	{
		if( !json.isObject() ) { throw new DataException( "TravelSetting isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();
		
		if( obj.get( "targetId" ) == null ) { throw new DataException( "TravelSetting 'targetId' is missing." ); }
		if( !obj.get( "targetId" ).isNumber() ) { throw new DataException( "TravelSetting 'targetId' isn't a number." ); }
		
		this.targetId = obj.get( "targetId" ).asInt(); 
		
		if( ( obj.get( "targetSide" ) != null ) &&
			( obj.get( "targetSide").isString() ) )
		{
			String side = obj.get( "targetSide").asString();
			
			if( this.allowedBorderSides().contains( side ) ) { throw new DataException( "TravelSetting 'targetSide' not allowed for 'side'." ); }
			
			this.targetSide = BorderSide.get( side );
		}
		if( obj.get( "editable" ) == null ) { throw new DataException( "TravelSetting 'editable' is missing." ); }
		if( !obj.get( "editable" ).isBoolean() ) { throw new DataException( "TravelSetting 'editable' isn't an boolean." ); }
		
		this.editable = obj.get( "editable" ).asBoolean(); 
	}

	/********************************************************************************
	 * Methods - Implement WorldBorderAction
	 ********************************************************************************/
	
	
	@Override
	public int priority()
	{
		return 100;
	}
	
	@Override
	public Set<BorderSide> allowedBorderSides()
	{
		Set<BorderSide> allowed = new HashSet<BorderSide>();
		allowed.add( BorderSide.NORTH );
		allowed.add( BorderSide.EAST );
		allowed.add( BorderSide.SOUTH );
		allowed.add( BorderSide.WEST );

		return allowed;
	}
	
	@Override
	public void performWorldBorderEvent( WorldBorderEvent event ) 
	{
		Dimension dimension;
		EntityPlayer player = (EntityPlayer)event.chunkEvent.entity; 
		
		if( dimensions.existsDimension( this.targetId ) )
		{
			dimension = dimensions.findDimension( this.targetId );
		}
		else
		{
			dimension = new DimensionDummy( this.targetId );
		}
		
		//Cancel other events
		event.setCanceled( true );
		
		if( this.targetSide != null )
		{
			Coordinate coordinate = event.setting.toCoordinate( targetSide, (int)player.posY );
			players.teleport( new TeleportSettings( dimension, player, coordinate ));
		}
		else
		{
			players.teleport( new TeleportSettings( dimension, (EntityPlayer)event.chunkEvent.entity ) );
		}
	}

}
