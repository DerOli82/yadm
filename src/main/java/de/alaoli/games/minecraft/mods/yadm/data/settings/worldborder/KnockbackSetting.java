package de.alaoli.games.minecraft.mods.yadm.data.settings.worldborder;

import java.util.HashSet;
import java.util.Set;

import javax.vecmath.Vector2d;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.data.settings.BorderSide;
import de.alaoli.games.minecraft.mods.yadm.data.settings.Setting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.event.PerformWorldBorderEvent;
import de.alaoli.games.minecraft.mods.yadm.event.WorldBorderEvent;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class KnockbackSetting implements Setting, PerformWorldBorderEvent, JsonSerializable 
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/	

	private float multiplier = 1.0f;
	
	/********************************************************************************
	 * Methods - Implement JsonSerializable
	 ********************************************************************************/	

	@Override
	public SettingType getSettingType() 
	{
		return SettingType.WORLDBORDER_KNOCKBACK;
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
		json.add( "multiplier", this.multiplier );
		
		return json;
	}

	@Override
	public void deserialize(JsonValue json) throws DataException 
	{
		if( !json.isObject() ) { throw new DataException( "KnockbackSetting isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();
		
		if( obj.get( "multiplier" ) != null )
		{
			if( !obj.get( "multiplier" ).isNumber() ) { throw new DataException( "KnockbackSetting 'multiplier' isn't a number." ); }
			
			this.multiplier = obj.get( "multiplier" ).asFloat();
			
			if( this.multiplier <= 0 ) { throw new DataException( "KnockbackSetting 'multiplier' must be greater than 0." ); }
		}
	}

	/********************************************************************************
	 * Methods - Implement WorldBorderAction
	 ********************************************************************************/
	
	@Override
	public int priority()
	{
		return 10;
	}
	
	@Override
	public Set<BorderSide> allowedBorderSides()
	{
		Set<BorderSide> allowed = new HashSet<BorderSide>();
		allowed.add( BorderSide.NORTH );
		allowed.add( BorderSide.EAST );
		allowed.add( BorderSide.SOUTH );
		allowed.add( BorderSide.WEST );
		allowed.add( BorderSide.ALL );

		return allowed;
	}
	
	@Override
	public void performWorldBorderEvent( WorldBorderEvent event ) 
	{
		Block block;
		Vector2d centerPlayer = new Vector2d( 
			event.chunkEvent.newChunkX - event.setting.getPointCenter().x,
			event.chunkEvent.newChunkZ - event.setting.getPointCenter().z
		);
		centerPlayer.scale( -1 / centerPlayer.length());
		
		EntityPlayer player = (EntityPlayer)event.chunkEvent.entity;
		World world = player.worldObj; 
		int checkY = 255;
		int x = (int)( player.posX + (centerPlayer.x * 16 * this.multiplier ) );
		int y = (int)player.posY;
		int z = (int)( player.posZ + (centerPlayer.y * 16 * this.multiplier) );
		
		while ( checkY > 0 ) 
		{
			block = world.getBlock( x, checkY, z );

			if( ( block != null ) && 
				( !block.isAir( world, x, checkY, z ) ) )
			{
				y = checkY + 1;
				break;
			}
			checkY--;
		}
		player.setPositionAndUpdate( x, y, z );
	}
}
