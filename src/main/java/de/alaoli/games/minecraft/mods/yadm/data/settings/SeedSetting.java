package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.util.List;
import java.util.Random;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import cpw.mods.fml.relauncher.ReflectionHelper;
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
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public SeedSetting() 
	{
		this.value = (new Random()).nextLong();
	}
	
	public SeedSetting( Long seed )
	{
		this.value = seed;
	}
	
	public Long getValue()
	{
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
		return true;
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
	public void deserialize( JsonValue json )
	{
		this.value = json.asObject().get( "value" ).asLong();
	}
	
	/********************************************************************************
	 * Methods - Implement Packageable
	 ********************************************************************************/
	
	@Override
	public void writeToNBT( NBTTagCompound tagCompound ) 
	{	
		tagCompound.setLong( "value", this.value );
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
				this.value, 
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
