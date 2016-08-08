package de.alaoli.games.minecraft.mods.yadm.proxy;

import java.io.File;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.command.DefaultCommand;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.event.DimensionEvent;
import de.alaoli.games.minecraft.mods.yadm.event.DimensionFMLEvent;
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

	protected TemplateManager templateManager;
	
	protected YADimensionManager dimensionManager;

	public CommonProxy()
	{
		this.templateManager = new TemplateManager();
		this.dimensionManager = new YADimensionManager();
	}
	
	/********************************************************************************
	 * Methods - FML
	 ********************************************************************************/
	
	public void preInit( FMLPreInitializationEvent event ) 
	{
		Config.init( new Configuration( event.getSuggestedConfigurationFile() ) );
		
		StringBuilder path = new StringBuilder();
		
		path.append( event.getModConfigurationDirectory().toString() );
		path.append( File.separator );
		path.append( YADM.MODID );
		path.append( "-templates" );
		path.append( File.separator );
		
		this.templateManager.setSavePath( path.toString() );
		this.templateManager.load();
		
		MessageDispatcher.register();
	}
	
	public void init( FMLInitializationEvent event )
	{
		FMLCommonHandler.instance().bus().register( new DimensionFMLEvent() );
		MinecraftForge.EVENT_BUS.register( new DimensionEvent() );
	}
	
	public void serverStarting( FMLServerStartingEvent event )
	{
		this.dimensionManager.setSavePath( 
			event.getServer().getEntityWorld().getSaveHandler().getWorldDirectory().getAbsolutePath() 
		);
		this.dimensionManager.load();
		this.dimensionManager.register();
		
		event.registerServerCommand( DefaultCommand.instance );
	}

	public void serverStopped( FMLServerStoppedEvent event ) 
	{
		this.dimensionManager.save();
		this.dimensionManager.cleanup();
	}
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public void registerDimension( Dimension dimension )
	{
		this.dimensionManager.register( dimension );
		this.dimensionManager.init( dimension );
	}
	
	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/
	
	public TemplateManager getTemplateManager()
	{
		return this.templateManager;
	}
	
	public YADimensionManager getDimensionManager()
	{
		return this.dimensionManager;
	}
}
