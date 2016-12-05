package de.alaoli.games.minecraft.mods.yadm;

import java.io.IOException;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.proxy.CommonProxy;


@Mod( modid = YADM.MODID, version = YADM.VERSION, name = YADM.NAME )
public class YADM
{
	/********************************************************************************
	 * Constant
	 ********************************************************************************/

	public static final String MODID	= "yadm";
	public static final String NAME		= "YADM - Yes Another Dimension Manager";
	public static final String VERSION	= "0.6.2";
						
	/********************************************************************************
	 * Forge
	 ********************************************************************************/

	@SidedProxy(
		clientSide = "de.alaoli.games.minecraft.mods.yadm.proxy.CommonProxy", 
		serverSide = "de.alaoli.games.minecraft.mods.yadm.proxy.ServerProxy"
	)
	public static CommonProxy proxy;
		
	/********************************************************************************
	 * Methods - Forge Event Handler
	 ********************************************************************************/

    @EventHandler 
    public void preInit( FMLPreInitializationEvent event ) 
    {
    	YADM.proxy.preInit( event );
    }
    
    @EventHandler
    public void init( FMLInitializationEvent event )
    {
    	YADM.proxy.init( event );
    }
    
    @EventHandler
    public void serverStarting( FMLServerStartingEvent event ) throws IOException, DataException
    {
    	YADM.proxy.serverStarting( event );
    }
    
    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) throws IOException, DataException
    {
    	YADM.proxy.serverStopped( event );
    }    
}
