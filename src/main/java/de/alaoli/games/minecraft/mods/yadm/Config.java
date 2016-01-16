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

	public static class Dimensions
	{
		/**
		 * 
		 */
		public static int beginsWithId = 1000;
		
		/**
		 * Teleport to new dimension after create command
		 */
		public static boolean teleportOnCreate = true;
	}
	
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
    
     	Config.Dimensions.beginsWithId = configFile.getInt(
			"beginsWithId",
			"dimensions", 
			Config.Dimensions.beginsWithId, 
			Integer.MIN_VALUE,
			Integer.MAX_VALUE,
			""
		);
     	
     	Config.Dimensions.teleportOnCreate = configFile.getBoolean( 
 			"teleportOnCreate", 
 			"dimensions", 
 			Config.Dimensions.teleportOnCreate, 
 			"Teleport to new dimension after create command."
		);
     	
     	
    	if( configFile.hasChanged() == true )
    	{
    		configFile.save();
    	}
	}

}
