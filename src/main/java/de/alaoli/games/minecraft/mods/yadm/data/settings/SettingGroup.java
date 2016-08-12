package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.Expose;

public abstract class SettingGroup implements Setting 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private Map<SettingType, Setting> settings = new HashMap<SettingType, Setting>();
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public void add( Setting setting )
	{
		if( !this.settings.containsKey( setting.getSettingType() ) )
		{
			this.settings.put( setting.getSettingType(), setting );
		}
	}
	
	public void remove( Setting setting )
	{
		if( this.settings.containsKey( setting.getSettingType() ) )
		{
			this.settings.remove( setting.getSettingType() );
		}
	}
	
	public Setting get( String setting )
	{
		return this.settings.get( setting ); 
	}
	
	public Map<SettingType, Setting> getAll()
	{
		return this.settings;
	}
}
