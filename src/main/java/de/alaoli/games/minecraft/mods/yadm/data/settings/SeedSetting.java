package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.util.List;
import java.util.Random;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import cpw.mods.fml.relauncher.ReflectionHelper;
import de.alaoli.games.minecraft.mods.lib.common.data.DataException;
import de.alaoli.games.minecraft.mods.lib.common.json.JsonSerializable;
import de.alaoli.games.minecraft.mods.lib.common.network.Packageable;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.world.Injectable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldInfo;

public class SeedSetting implements Setting, JsonSerializable, Packageable, Injectable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private Long value;
	private boolean isRandom;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public Long getValue()
	{
		//Initialize random seed
		if( ( this.value == null ) && ( this.isRandom ) )
		{
			this.value = (new Random()).nextLong();
			this.isRandom = false;
		}
		return this.value;
	}
	
	public boolean isRandom()
	{
		return this.isRandom;
	}
	
	/********************************************************************************
	 * Methods - Implement Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType()
	{
		return SettingType.SEED;
	}

	@Override
	public boolean isSettingRequired() 
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
		
		if( this.value != null )
		{
			json.add( "value", this.value );
		}
		else
		{
			json.add( "value", "*" );
		}
		return json;
	}

	@Override
	public void deserialize( JsonValue json ) throws DataException
	{
		if( !json.isObject() ) { throw new DataException( "SeedSetting isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();		
		
		if( obj.get( "value" ) == null ) { throw new DataException( "SeedSetting 'value' is missing." ); }
		
		if( ( obj.get( "value" ).isString() ) && 
			( obj.get( "value" ).asString().contains( "*" ) ) )
		{
			this.isRandom = true;
			this.value = null;
		}
		else if( obj.get( "value" ).isNumber() )
		{
			this.value = json.asObject().get( "value" ).asLong();
			this.isRandom = false;
		}
		else
		{
			throw new DataException( "SeedSetting 'value' is invalid." );
		}
	}
	
	/********************************************************************************
	 * Methods - Implement Packageable
	 ********************************************************************************/
	
	@Override
	public void writeToNBT( NBTTagCompound tagCompound ) 
	{	
		tagCompound.setLong( "value", this.getValue() );
	}

	@Override
	public void readFromNBT( NBTTagCompound tagCompound ) 
	{
		this.value = tagCompound.getLong( "value" );
	}
	
	/********************************************************************************
	 * Methods - Implements Injectable
	 ********************************************************************************/

	@Override
	public void injectInto( Object target )
	{
		if( target instanceof WorldInfo )
		{
			WorldInfo worldInfo = (WorldInfo)target;
			
			ReflectionHelper.setPrivateValue( 
				WorldInfo.class, worldInfo, 
				this.getValue(), 
				new String[] { "field_76100_a", "randomSeed" } 
			);		
			Log.debug( "  - Seed: " + this.getValue() +" into WorldInfo" );
		}
	}		
	
	@Override
	public void injectInto( List targets )
	{
		for( Object target : targets )
		{
			this.injectInto( target );
		}
	}		
}
