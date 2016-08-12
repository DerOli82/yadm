package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.lang.reflect.Type;

import com.google.gson.annotations.Expose;

public class SeedSetting implements Setting
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	@Expose
	private Long value;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
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
		return false;
	}

	@Override
	public SeedSetting createInstance( Type type ) 
	{
		return new SeedSetting();
	}	
}
