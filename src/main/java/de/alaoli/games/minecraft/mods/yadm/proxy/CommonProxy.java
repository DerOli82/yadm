package de.alaoli.games.minecraft.mods.yadm.proxy;

import java.io.File;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.command.ManageCommand;
import de.alaoli.games.minecraft.mods.yadm.event.DimensionEvent;
import de.alaoli.games.minecraft.mods.yadm.manager.PatternManager;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.network.DimensionSyncHandler;
import de.alaoli.games.minecraft.mods.yadm.network.DimensionSyncMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class CommonProxy 
{	
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	protected PatternManager patternManager;
	
	protected YADimensionManager dimensionManager;

	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public void preInit( FMLPreInitializationEvent event ) 
	{
		Config.init( new Configuration( event.getSuggestedConfigurationFile() ) );
		
		StringBuilder path = new StringBuilder();
		
		path.append( event.getModConfigurationDirectory().toString() );
		path.append( File.separator );
		path.append( YADM.MODID );
		path.append( File.separator );
		
		this.patternManager = new PatternManager();
		this.patternManager.setSavePath( path.toString() );
		this.patternManager.load();
		
		YADM.network = NetworkRegistry.INSTANCE.newSimpleChannel( YADM.MODID );
		YADM.network.registerMessage( DimensionSyncHandler.class, DimensionSyncMessage.class, 0, Side.CLIENT );
		YADM.network.registerMessage( DimensionSyncHandler.class, DimensionSyncMessage.class, 0, Side.SERVER );
	}
	
	public void init( FMLInitializationEvent event )
	{
		MinecraftForge.EVENT_BUS.register( new DimensionEvent() );
	}
	
	public void serverStarting( FMLServerStartingEvent event )
	{
		StringBuilder path = new StringBuilder();
		
		path.append( event.getServer().getEntityWorld().getSaveHandler().getWorldDirectory().getAbsolutePath() );
		path.append( File.separator );
		path.append( "data" );
		path.append( File.separator );
		path.append( YADM.MODID );
		path.append( File.separator );

		this.dimensionManager = new YADimensionManager();
		this.dimensionManager.setSavePath( path.toString() );
		this.dimensionManager.load();
		this.dimensionManager.registerAll();
		
		event.registerServerCommand( new ManageCommand() );
	}

	public void serverStopping( FMLServerStoppingEvent event ) 
	{
		this.dimensionManager.unregisterAll();
		this.dimensionManager.save();
	}
	
	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/
	
	public PatternManager getPatternManager()
	{
		return this.patternManager;
	}
	
	public YADimensionManager getDimensionManager()
	{
		return this.dimensionManager;
	}
}
