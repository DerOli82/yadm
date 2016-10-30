package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.util.List;
import java.util.Random;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import cpw.mods.fml.relauncher.ReflectionHelper;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.interceptor.Injectable;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;
import de.alaoli.games.minecraft.mods.yadm.network.Packageable;
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
		}
		return this.value;
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
		json.add( "value", this.value );
		
		return json;
	}

	@Override
	public void deserialize( JsonValue json ) throws DataException
	{
		if( ( json.asObject().get( "value" ).isString() ) && 
			(json.asObject().get( "value" ).asString().contains( "*" ) ) )
		{
			this.isRandom = true;
			this.value = null;
		}
		else if( json.asObject().get( "value" ).isString() )
		{
			this.value = json.asObject().get( "value" ).asLong();
			this.isRandom = false;
		}
		else
		{
			throw new DataException( "Invalid seed value." );
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
