package de.alaoli.games.minecraft.mods.yadm.proxy;

import java.io.File;
import java.io.IOException;
import java.util.StringJoiner;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.command.YADMCommandGroup;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.event.handler.DimensionEvent;
import de.alaoli.games.minecraft.mods.yadm.event.handler.DimensionFMLEvent;
import de.alaoli.games.minecraft.mods.yadm.event.handler.WorldGuardEvent;
import de.alaoli.games.minecraft.mods.yadm.json.JsonFileAdapter;
import de.alaoli.games.minecraft.mods.yadm.manager.PlayerManager;
import de.alaoli.games.minecraft.mods.yadm.manager.TemplateManager;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.network.MessageDispatcher;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class CommonProxy 
{	
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	protected static final JsonFileAdapter templateFiles = TemplateManager.INSTANCE;
	protected static final JsonFileAdapter playerFiles = PlayerManager.INSTANCE;	
	protected static final YADimensionManager dimensions = YADimensionManager.INSTANCE;
	
	/********************************************************************************
	 * Methods - FML
	 ********************************************************************************/
	
	/**
	 * Load templates from JSON files and register network message dispatcher
	 * 
	 * @param FMLPreInitializationEvent event
	 */
	public void preInit( FMLPreInitializationEvent event ) 
	{
		Config.init( new Configuration( event.getSuggestedConfigurationFile() ) );
		
		StringJoiner path = new StringJoiner( File.separator )
			.add( event.getModConfigurationDirectory().toString() )
			.add( YADM.MODID + "-templates" );
		templateFiles.setSavePath( path.toString() );
		
		try 
		{
			templateFiles.load();
			
		} catch ( DataException | IOException e )
		{
			e.printStackTrace();
		}
		MessageDispatcher.register();
	}
	
	/**
	 * Register events
	 * 
	 * @param FMLInitializationEvent event
	 */
	public void init( FMLInitializationEvent event )
	{
		FMLCommonHandler.instance().bus().register( new DimensionFMLEvent() );
		MinecraftForge.EVENT_BUS.register( new DimensionEvent() );
		MinecraftForge.EVENT_BUS.register( new WorldGuardEvent() );
	}
	
	/**
	 * Load players, dimensions and register server commands
	 * 
	 * @param FMLServerStartingEvent event
	 * @throws DataException
	 * @throws IOException
	 */
	public void serverStarting( FMLServerStartingEvent event ) throws DataException, IOException
	{
		playerFiles.load();
		dimensions.load();
		
		dimensions.registerDimensions();
		
		event.registerServerCommand( new YADMCommandGroup() );
	}

	/**
	 * Save players, dimensions and cleanup
	 * @param event
	 * @throws DataException
	 * @throws IOException
	 */
	public void serverStopped( FMLServerStoppedEvent event ) throws DataException, IOException 
	{
		playerFiles.save();
		dimensions.save();
		
		dimensions.unregisterDimensions();
		
		playerFiles.cleanup();
		dimensions.cleanup();
	}
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public void registerDimension( Dimension dimension )
	{
		dimensions.registerDimension( dimension );
	}
	
	public void unregisterDimension( Dimension dimension ) {}
}
