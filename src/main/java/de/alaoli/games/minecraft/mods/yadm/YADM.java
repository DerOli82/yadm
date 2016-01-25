package de.alaoli.games.minecraft.mods.yadm;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import de.alaoli.games.minecraft.mods.yadm.proxy.CommonProxy;


@Mod( modid = YADM.MODID, version = YADM.VERSION, name = YADM.NAME )
public class YADM
{
	/********************************************************************************
	 * Constant
	 ********************************************************************************/

	public static final String MODID	= "yadm";
	public static final String NAME		= "YADM - Yes another Dimension Manager";
	public static final String VERSION	= "0.1.0";
						
	/********************************************************************************
	 * Forge
	 ********************************************************************************/

	@SidedProxy(
		clientSide = "de.alaoli.games.minecraft.mods.yadm.proxy.ClientProxy", 
		serverSide = "de.alaoli.games.minecraft.mods.yadm.proxy.CommonProxy"
	)
	public static CommonProxy proxy;
		
	public static SimpleNetworkWrapper network;
	
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
    public void serverInit( FMLServerStartingEvent event )
    {
    	YADM.proxy.serverInit( event );
    }
}
