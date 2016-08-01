package de.alaoli.games.minecraft.mods.yadm.proxy;

import java.io.File;
import java.util.Set;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.command.ManageCommand;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.event.DimensionEvent;
import de.alaoli.games.minecraft.mods.yadm.manager.PatternManager;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public abstract class CommonProxy 
{	
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/

	public static SimpleNetworkWrapper network;
	
	protected PatternManager patternManager;
	
	protected YADimensionManager dimensionManager;

	public CommonProxy()
	{
		this.patternManager = new PatternManager();
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
		path.append( File.separator );
		
		this.patternManager.setSavePath( path.toString() );
		this.patternManager.load();
		
		network = NetworkRegistry.INSTANCE.newSimpleChannel( YADM.MODID );
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
	 * Methods
	 ********************************************************************************/
	
	public abstract void syncDimension( Dimension dimension ); 
	public abstract void syncDimension( Set<Dimension> dimensions, EntityPlayerMP player ); 
	
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
