package de.alaoli.games.minecraft.mods.yadm.proxy;

import java.io.File;
import java.io.IOException;
import java.util.StringJoiner;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import de.alaoli.games.minecraft.mods.lib.common.Config;
import de.alaoli.games.minecraft.mods.lib.common.Initialize;
import de.alaoli.games.minecraft.mods.lib.common.data.DataException;
import de.alaoli.games.minecraft.mods.lib.common.json.JsonFileAdapter;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.command.YADMCommandGroup;
import de.alaoli.games.minecraft.mods.yadm.config.ConfigDimensionSection;
import de.alaoli.games.minecraft.mods.yadm.config.ConfigProviderSection;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.event.handler.DimensionEventHandler;
import de.alaoli.games.minecraft.mods.yadm.event.handler.TeleportEventHandler;
import de.alaoli.games.minecraft.mods.yadm.event.handler.WorldBorderEventHandler;
import de.alaoli.games.minecraft.mods.yadm.event.handler.WorldGuardEventHandler;
import de.alaoli.games.minecraft.mods.yadm.manager.PlayerManager;
import de.alaoli.games.minecraft.mods.yadm.manager.TemplateManager;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.network.MessageDispatcher;

public class CommonProxy implements Initialize
{	
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	protected static final JsonFileAdapter templateFiles = TemplateManager.INSTANCE;
	protected static final JsonFileAdapter playerFiles = PlayerManager.INSTANCE;	
	protected static final YADimensionManager dimensions = YADimensionManager.INSTANCE;
	
	/********************************************************************************
	 * Method - Implements 
	 ********************************************************************************/
	
	@Override
	public void preInit( FMLPreInitializationEvent event ) throws IOException, DataException
	{
		StringJoiner path = new StringJoiner( File.separator )
			.add( event.getModConfigurationDirectory().toString() )
			.add( YADM.MODID );
		Config config = new Config();
			
		config.setSavePath( path.toString() + ".json" );
		config.registerSection( ConfigProviderSection.class );
		config.registerSection( ConfigDimensionSection.class );
		config.load();
		config.cleanup();
		
		templateFiles.setSavePath( path.toString() + "-templates" );
		
		MessageDispatcher.register();
	}
	
	@Override
	public void init( FMLInitializationEvent event ) throws IOException, DataException
	{
		DimensionEventHandler.register();
		WorldBorderEventHandler.register();
		WorldGuardEventHandler.register();
		TeleportEventHandler.register();
		
		try 
		{
			templateFiles.load();
		}
		catch ( DataException | IOException e )
		{
			e.printStackTrace();
		}		
	}
	
	@Override
	public void postInit( FMLPostInitializationEvent event ) throws IOException, DataException {} 	
	
	/********************************************************************************
	 * Method - Forge Event Handler
	 ********************************************************************************/
	
	public void serverStarting( FMLServerStartingEvent event ) throws DataException, IOException
	{
		playerFiles.load();
		dimensions.load();
		
		dimensions.registerDimensions();
		
		event.registerServerCommand( new YADMCommandGroup() );
	}

	public void serverStopped( FMLServerStoppedEvent event ) throws DataException, IOException 
	{
		playerFiles.save();
		dimensions.save();
		
		dimensions.unregisterDimensions();
		
		playerFiles.cleanup();
		dimensions.cleanup();
	}
	
	/********************************************************************************
	 * Method
	 ********************************************************************************/
	
	public void registerDimension( Dimension dimension )
	{
		dimensions.registerDimension( dimension );
	}
	
	public void unregisterDimension( Dimension dimension ) {}
	
	public void updateDimension( Dimension dimension ) {}
}
