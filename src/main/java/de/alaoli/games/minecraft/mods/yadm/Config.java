package de.alaoli.games.minecraft.mods.yadm;

import net.minecraftforge.common.config.Configuration;

public class Config 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/

	public static class Provider
	{
		/**
		 * Use as first provider id.
		 */
		public static int beginsWithId = 500;		
	}
	
	public static class Dimension
	{
		/**
		 * Use as first dimension id.
		 */
		public static int beginsWithId = 1000;
	
		/**
		 * Backup feature not implemented yet!
		 */
		public static boolean backupDimensionOnDelete = true;
		
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
        
     	Config.Provider.beginsWithId = config.getInt(
			"beginsWithId",
			"provider", 
			Config.Provider.beginsWithId, 
			Integer.MIN_VALUE,
			Integer.MAX_VALUE,
			"Use as first dimension id."
		);
		
     	Config.Dimension.beginsWithId = config.getInt(
			"beginsWithId",
			"dimension", 
			Config.Dimension.beginsWithId, 
			Integer.MIN_VALUE,
			Integer.MAX_VALUE,
			"Use as first dimension id."
		);

     	Config.Dimension.backupDimensionOnDelete = config.getBoolean( 
 			"backupDimensionOnDelete", 
 			"dimension", 
 			Config.Dimension.backupDimensionOnDelete, 
 			"Backup feature not implemented yet!"
		);
     	
     	Config.Dimension.teleportOnCreate = config.getBoolean( 
 			"teleportOnCreate", 
 			"dimension", 
 			Config.Dimension.teleportOnCreate, 
 			"Teleport to new dimension after create command."
		);
     	     	
    	if( config.hasChanged() == true )
    	{
    		config.save();
    	}			
	}
}
