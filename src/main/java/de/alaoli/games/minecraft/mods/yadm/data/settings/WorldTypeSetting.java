package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.lang.reflect.Type;

import com.google.gson.annotations.Expose;

public class WorldTypeSetting implements Setting
{
	/********************************************************************************
	 * Constants
	 ********************************************************************************/
	
	public static final String DEFAULT = "default";
	
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	@Expose
	private String name;

	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public WorldTypeSetting() {}
	
	public WorldTypeSetting( String name )
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	/********************************************************************************
	 * Methods - Implements Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType()
	{
		return SettingType.WORLDTYPE;
	}

	@Override
	public boolean isRequired() 
	{
		return false;
	}

	@Override
	public WorldTypeSetting createInstance( Type type ) 
	{
		return new WorldTypeSetting();
	}		
}
