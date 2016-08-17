package de.alaoli.games.minecraft.mods.yadm.event;

import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.handshake.NetworkDispatcher;
import cpw.mods.fml.relauncher.Side;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.Manageable;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.network.MessageDispatcher;
import de.alaoli.games.minecraft.mods.yadm.network.SyncDimensionsMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;

public class DimensionFMLEvent 
{
    @SubscribeEvent( priority = EventPriority.HIGHEST )
    public void onClientConnected( FMLNetworkEvent.ServerConnectionFromClientEvent event )
    {
    	Log.info( "Client connected..." );

    	if( !YADimensionManager.instance.isEmpty() )
    	{
	    	EntityPlayerMP player = ((NetHandlerPlayServer) event.handler).playerEntity;
	    	
	    	StringBuilder msg = new StringBuilder()
    			.append( "... sync Dimensions to Player '" )
	    		.append( player.getUniqueID() )
	    		.append( "':" );
	    	Log.info( msg.toString() );
	    
	    	Dimension dimension;
	    	Set<Dimension> dimensions = new HashSet<Dimension>(); 
	    	
	    	for( Entry<String, Manageable> entry : YADimensionManager.instance.getAll() )
	    	{
	    		dimension = (Dimension)entry.getValue();
	    		dimensions.add( dimension );
	    		
				msg = new StringBuilder()
					.append( "- Dimension '" )
					.append( dimension.getName() )
					.append( "' with ID '" )
					.append( dimension.getId() )
					.append( "'." );
				Log.info( msg.toString() );	    		
	    	}
	    	FMLEmbeddedChannel channel = MessageDispatcher.channels.get( Side.SERVER );
	    	
	    	channel.attr(FMLOutboundHandler.FML_MESSAGETARGET ).set( FMLOutboundHandler.OutboundTarget.DISPATCHER );
	    	channel.attr( FMLOutboundHandler.FML_MESSAGETARGETARGS ).set(event.manager.channel().attr( NetworkDispatcher.FML_DISPATCHER ).get() );
	    	channel.writeOutbound( new SyncDimensionsMessage( dimensions ) );	    	
    	}
    	else
    	{
    		Log.info( "... nothing to sync." );
    	}
    }
}
