package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.lang.reflect.Type;

import com.google.gson.annotations.Expose;

import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;

public class SpawnSetting implements Setting
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	@Expose
	private Coordinate coordinate;
	
	@Expose
	private int mode;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public Coordinate getCoordinate() 
	{
		return this.coordinate;
	}

	public int getMode() 
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

	@Override
	public SpawnSetting createInstance( Type type ) 
	{
		return new SpawnSetting();
	}		
}
