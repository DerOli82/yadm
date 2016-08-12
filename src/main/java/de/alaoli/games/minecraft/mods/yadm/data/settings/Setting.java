package de.alaoli.games.minecraft.mods.yadm.data.settings;

import com.google.gson.InstanceCreator;

public interface Setting
{
	public SettingType getSettingType();
	
	public boolean isRequired();
}
