package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;
import com.google.gson.annotations.Expose;

import de.alaoli.games.minecraft.mods.yadm.data.Template;

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

	@Override
	public GeneratorOptionsSetting createInstance( Type type ) 
	{
		return new GeneratorOptionsSetting();
	}		
}
