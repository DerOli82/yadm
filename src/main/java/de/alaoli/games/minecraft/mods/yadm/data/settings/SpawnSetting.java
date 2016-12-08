package de.alaoli.games.minecraft.mods.yadm.data.settings;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportModifier;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportSettings;
import de.alaoli.games.minecraft.mods.yadm.network.Packageable;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

public class SpawnSetting implements Setting, TeleportModifier, Packageable, JsonSerializable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private Coordinate coordinate;
	
	private SpawnMode mode;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public SpawnSetting() {}
	
	public SpawnSetting( Coordinate coordinate, SpawnMode mode )
	{
		this.coordinate = coordinate;
		this.mode = mode;
	}
	
	public Coordinate getCoordinate() 
	{
		return this.coordinate;
	}

	public SpawnMode getMode() 
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
	 * Methods - Implement TeleportModifier
	 ********************************************************************************/
	
	@Override
	public void applyTeleportModifier( TeleportSettings settings )
	{
		switch( this.mode )
		{
			case EXACT :
				//Nothing to do
				break;
				
			case TOPDOWN :
			default :
				Block block;
				int y = 255;
	
				while ( y > 0 ) 
				{
					block = settings.target.getBlock(settings.coordinate.x, y, settings.coordinate.z);
	
					if( ( block != null ) && 
						( !block.isAir(settings.target, settings.coordinate.x, y, settings.coordinate.z ) ) )
					{
						settings.coordinate = new Coordinate( 
							settings.coordinate.x, 
							y + TeleportSettings.OFFSETY, 
							settings.coordinate.z 
						);
						break;
					}
					y--;
				}		
				break;
				
		}
	}	
	
	/********************************************************************************
	 * Methods - Implement Packageable
	 ********************************************************************************/
	
	@Override
	public void writeToNBT( NBTTagCompound tagCompound ) 
	{	
		tagCompound.setInteger( "x", this.coordinate.x );
		tagCompound.setInteger( "y", this.coordinate.y );
		tagCompound.setInteger( "z", this.coordinate.z );
		
		tagCompound.setString( "mode", this.mode.toString() );
	}

	@Override
	public void readFromNBT( NBTTagCompound tagCompound ) 
	{
		this.coordinate = new Coordinate(
			tagCompound.getInteger( "x" ),
			tagCompound.getInteger( "y" ),
			tagCompound.getInteger( "z" )
		);
		this.mode = SpawnMode.get( tagCompound.getString( "mode" ) );
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
		if( !json.isObject() ) { throw new DataException( "SpawnSetting isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();		
		
		if( obj.get( "x" ) == null ) { throw new DataException( "SpawnSetting 'x' coordinate is missing." ); }
		if( !obj.get( "x" ).isNumber() ) { throw new DataException( "SpawnSetting 'x' coordinate isn't a number." ); }
		if( obj.get( "y" ) == null ) { throw new DataException( "SpawnSetting 'y' coordinate is missing." ); }
		if( !obj.get( "y" ).isNumber() ) { throw new DataException( "SpawnSetting 'y' coordinate isn't a number." ); }
		if( obj.get( "z" ) == null ) { throw new DataException( "SpawnSetting 'z' coordinate is missing." ); }
		if( !obj.get( "z" ).isNumber() ) { throw new DataException( "SpawnSetting 'z' coordinate isn't a number." ); }
		
		this.coordinate = new Coordinate(
			obj.get( "x" ).asInt(),
			obj.get( "y" ).asInt(),
			obj.get( "z" ).asInt()
		);
		
		if( obj.get( "mode" ) != null )
		{
			if( !obj.get( "mode" ).isString() ) { throw new DataException( "SpawnSetting 'mode' coordinate isn't a string." ); }
			
			this.mode = SpawnMode.get( json.asObject().get( "mode" ).asString() );
			
			if( this.mode == null ) { throw new DataException( "SpawnSetting 'mode' is invalid." ); }
		}
		else
		{
			//Default
			this.mode = SpawnMode.TOPDOWN;
		}
	}
}
