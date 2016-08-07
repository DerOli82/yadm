package de.alaoli.games.minecraft.mods.yadm;

import net.minecraftforge.common.config.Configuration;

public class Config 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/

	public static class Dimensions
	{
		/**
		 * Use as first dimension id.
		 */
		public static int beginsWithId = 1000;
		
		/**
		 * Teleport to new dimension after create command.
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
	public static void init( Configuration config )
	{	
		config.load();
        
     	Config.Dimensions.beginsWithId = config.getInt(
			"beginsWithId",
			"dimensions", 
			Config.Dimensions.beginsWithId, 
			Integer.MIN_VALUE,
			Integer.MAX_VALUE,
			"Use as first dimension id."
		);
     	
     	Config.Dimensions.teleportOnCreate = config.getBoolean( 
 			"teleportOnCreate", 
 			"dimensions", 
 			Config.Dimensions.teleportOnCreate, 
 			"Teleport to new dimension after create command."
		);
     	     	
    	if( config.hasChanged() == true )
    	{
    		config.save();
    	}			
	}
}
