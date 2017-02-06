package de.alaoli.games.minecraft.mods.yadm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.alaoli.games.minecraft.mods.yadm.config.ConfigLogSection;

public class Log 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private static final Logger LOGGER = LogManager.getLogger( YADM.MODID );
	
	/********************************************************************************
	 * Methods

	/**
	 * Logs a message object with the DEBUG level if enabled
	 * 
	 * @param String
	 */
	public static void debug( String msg )
	{
		if( ConfigLogSection.debugging )
		{
			LOGGER.info( msg );
		}
	}

	/**
	 * Logs a message object with the INFO level.
	 * 
	 * @param String
	 */
	public static void info( String msg )
	{
		LOGGER.info( msg );
	}
	
	/**
	 * Logs a message object with the WARN level.
	 * 
	 * @param String
	 */
	public static void warn( String msg )
	{
		LOGGER.warn( msg );
	}
	
	/**
	 * Logs a message object with the ERROR level.
	 * 
	 * @param String
	 */
	public static void error( String msg )
	{
		LOGGER.error( msg );
	}	
}
