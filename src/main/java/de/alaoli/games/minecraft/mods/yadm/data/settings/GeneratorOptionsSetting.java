package de.alaoli.games.minecraft.mods.yadm.data.settings;

import com.google.gson.annotations.Expose;

public class GeneratorOptionsSetting implements Setting
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/

	@Expose
	private String value;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public String getValue()
	{
		return this.value;
	}
	
	/********************************************************************************
	 * Methods - Implement Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType()
	{
		return SettingType.GENERATOROPTIONS;
	}

	@Override
	public boolean isRequired() 
	{
		return false;
	}
}
