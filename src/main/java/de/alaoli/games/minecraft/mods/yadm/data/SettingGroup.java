package de.alaoli.games.minecraft.mods.yadm.data;

import java.util.HashMap;
import java.util.Map;

public abstract class SettingGroup implements Setting 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private Map<String, Setting> settings = new HashMap<String, Setting>();
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public void add( Setting setting )
	{
		if( !this.settings.containsKey( setting.getSettingName() ) )
		{
			this.settings.put( setting.getSettingName(), setting );
		}
	}
	
	public void remove( Setting setting )
	{
		if( this.settings.containsKey( setting.getSettingName() ) )
		{
			this.settings.remove(setting.getSettingName() );
		}
	}
	
	public Setting get( String setting )
	{
		return this.settings.get( setting ); 
	}	
}
