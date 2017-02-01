package de.alaoli.games.minecraft.mods.yadm.network;

import java.util.EnumMap;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import de.alaoli.games.minecraft.mods.yadm.YADM;

public class MessageDispatcher 
{
	private static byte id = 0;
	
	public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel( YADM.MODID );;
	public static final EnumMap<Side,FMLEmbeddedChannel> channels = NetworkRegistry.INSTANCE.newChannel( YADM.MODID + "Channel", new SyncDimensionChannelHandler() );
	
	public static final void register()
	{
		network.registerMessage( 
			RegisterDimensionHandler.class, 
			RegisterDimensionMessage.class, 
			id++, Side.CLIENT 
		);
		network.registerMessage( 
			UnegisterDimensionHandler.class, 
			UnregisterDimensionMessage.class, 
			id++, Side.CLIENT 
		);
		network.registerMessage(
			UpdateDimensionHandler.class,
			UpdateDimensionMessage.class,
			id++, Side.CLIENT
		);
	}
}
