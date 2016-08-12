package de.alaoli.games.minecraft.mods.yadm.data;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;
import com.google.gson.annotations.Expose;

import de.alaoli.games.minecraft.mods.yadm.data.settings.Setting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingGroup;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.manager.Manageable;

public class Template extends SettingGroup implements Manageable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private String group; 
	
	@Expose
	private String name;

	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public Template() {}
	
	public Template( String name, String group )
	{
		this.name = name;
		this.group = group;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	/********************************************************************************
	 * Methods - Implement Manageable
	 ********************************************************************************/

	@Override
	public String getManageableGroupName() 
	{
		return this.group;
	}
	
	@Override
	public String getManageableName() 
	{
		return this.name;
	}

	/********************************************************************************
	 * Methods - Implement Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType() 
	{
		return null;
	}

	@Override
	public boolean isRequired() 
	{
		return true;
	}
}
