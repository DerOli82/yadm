package de.alaoli.games.minecraft.mods.yadm;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import de.alaoli.games.minecraft.mods.yadm.proxy.CommonProxy;


@Mod( modid = YADM.MODID, version = YADM.VERSION, name = YADM.NAME )
public class YADM
{
	/********************************************************************************
	 * Constant
	 ********************************************************************************/

	public static final String MODID	= "yadm";
	public static final String NAME		= "YADM - Yes another Dimension Manager";
	public static final String VERSION	= "0.3.1";
						
	/********************************************************************************
	 * Forge
	 ********************************************************************************/

	@SidedProxy(
		clientSide = "de.alaoli.games.minecraft.mods.yadm.proxy.ClientProxy", 
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
    public void serverStarting( FMLServerStartingEvent event )
    {
    	YADM.proxy.serverStarting( event );
    }
    
    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event)
    {
    	YADM.proxy.serverStopping( event );
    }    
    
}
