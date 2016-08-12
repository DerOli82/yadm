package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.lang.reflect.Type;

import com.google.gson.annotations.Expose;

public class WorldProviderSetting implements Setting 
{
	/********************************************************************************
	 * Constants
	 ********************************************************************************/
	
	public static final String OVERWORLD = "overworld";
	public static final String NETHER = "nether";
	public static final String END = "end";
		
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private int id;
	
	@Expose
	private String name;

	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public WorldProviderSetting() {}
	
	public WorldProviderSetting( String name )
	{
		this.name = name;
	}
	
	public int getId() 
	{
		return this.id;
	}

	public String getName() 
	{
		return this.name;
	}
	
	public void setId( int Id ) 
	{
		this.id = Id;
	}

	/********************************************************************************
	 * Methods - Implements Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType()
	{
		return SettingType.WORLDPROVIDER;
	}

	@Override
	public boolean isRequired() 
	{
		return true;
	}

	@Override
	public WorldProviderSetting createInstance( Type type ) 
	{
		return new WorldProviderSetting();
	}	
}
