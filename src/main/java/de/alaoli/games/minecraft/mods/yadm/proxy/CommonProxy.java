package de.alaoli.games.minecraft.mods.yadm.proxy;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import de.alaoli.games.minecraft.mods.yadm.Config;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.command.ManageCommand;
import de.alaoli.games.minecraft.mods.yadm.data.DimensionPattern;
import de.alaoli.games.minecraft.mods.yadm.event.DimensionEvent;
import de.alaoli.games.minecraft.mods.yadm.json.DimensionPatternAdapter;
import de.alaoli.games.minecraft.mods.yadm.manager.DimensionPatternManager;
import de.alaoli.games.minecraft.mods.yadm.network.DimensionSyncHandler;
import de.alaoli.games.minecraft.mods.yadm.network.DimensionSyncMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class CommonProxy 
{	
	public void preInit( FMLPreInitializationEvent event ) 
	{
		Config.initCfg( new Configuration( event.getSuggestedConfigurationFile() ) );
		Config.initJson( event.getModConfigurationDirectory().toString() );
		
		YADM.network = NetworkRegistry.INSTANCE.newSimpleChannel( YADM.MODID );
		YADM.network.registerMessage( DimensionSyncHandler.class, DimensionSyncMessage.class, 0, Side.CLIENT );
		YADM.network.registerMessage( DimensionSyncHandler.class, DimensionSyncMessage.class, 0, Side.SERVER );
	}
	
	public void init( FMLInitializationEvent event )
	{
		MinecraftForge.EVENT_BUS.register( new DimensionEvent() );
	}
	
	public void serverInit( FMLServerStartingEvent event )
	{
		event.registerServerCommand( new ManageCommand() );
	}
	

	
}
