package de.alaoli.games.minecraft.mods.yadm;

import net.minecraftforge.common.config.Configuration;

public class Config 
{
	/********************************************************************************
	 * Constants
	 ********************************************************************************/

	/********************************************************************************
	 * Attributes
	 ********************************************************************************/

	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	/**
	 * Configuration initialization 
	 * 
	 * @param Configuration
	 */
	public static void init( Configuration configFile )
	{
    	configFile.load();
    
    	if( configFile.hasChanged() == true )
    	{
    		configFile.save();
    	}
	}

}
