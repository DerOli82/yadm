package de.alaoli.games.minecraft.mods.yadm;

import java.io.IOException;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import de.alaoli.games.minecraft.mods.lib.common.Initialize;
import de.alaoli.games.minecraft.mods.lib.common.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.proxy.CommonProxy;


@Mod( modid = YADM.MODID, version = YADM.VERSION, name = YADM.NAME )
public class YADM implements Initialize
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/

	public static final String MODID	= "yadm";
	public static final String NAME		= "YADM - Yes Another Dimension Manager";
	public static final String VERSION	= "0.9.0";

	@SidedProxy(
		clientSide = "de.alaoli.games.minecraft.mods.yadm.proxy.CommonProxy", 
		serverSide = "de.alaoli.games.minecraft.mods.yadm.proxy.ServerProxy"
	)
	public static CommonProxy proxy;
		
	/********************************************************************************
	 * Method - Implements Initialize
	 ********************************************************************************/

	@Override
    @EventHandler 
    public void preInit( FMLPreInitializationEvent event ) 
    {
    	proxy.preInit( event );
    }
    
    @Override
    @EventHandler
    public void init( FMLInitializationEvent event )
    {
    	proxy.init( event );
    }
    
	@Override
	public void postInit(FMLPostInitializationEvent event) throws IOException, DataException {}    
	
	/********************************************************************************
	 * Method - Forge Event Handler
	 ********************************************************************************/
	
    @EventHandler
    public void serverStarting( FMLServerStartingEvent event ) throws IOException, DataException
    {
    	proxy.serverStarting( event );
    }
    
    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) throws IOException, DataException
    {
    	proxy.serverStopped( event );
    }
}
