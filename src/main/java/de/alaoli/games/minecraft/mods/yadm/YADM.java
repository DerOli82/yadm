package de.alaoli.games.minecraft.mods.yadm;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import de.alaoli.games.minecraft.mods.yadm.command.ManageCommand;
import de.alaoli.games.minecraft.mods.yadm.command.TeleportCommand;
import de.alaoli.games.minecraft.mods.yadm.proxy.CommonProxy;


@Mod( modid = YADM.MODID, version = YADM.VERSION, name = YADM.NAME )
public class YADM
{
	/********************************************************************************
	 * Mod Info
	 ********************************************************************************/

	public static final String MODID	= "YADM";
	public static final String NAME		= "YADM - Yes another Dimension Manager";
	public static final String VERSION	= "0.1.0";
						
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/

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
    	Configuration configFile = new Configuration( event.getSuggestedConfigurationFile() );
    	
    	Config.init( configFile );

    }
    
    @EventHandler
    public void init( FMLInitializationEvent event )
    {
    	proxy.registerRenderers();

    	
    }
    
    @EventHandler
    public void serverInit( FMLServerStartingEvent event )
    {
		event.registerServerCommand( new ManageCommand() );
		event.registerServerCommand( new TeleportCommand() );
    }
    
	/********************************************************************************
	 * Methods - Getter / Setter
	 ********************************************************************************/    

}
