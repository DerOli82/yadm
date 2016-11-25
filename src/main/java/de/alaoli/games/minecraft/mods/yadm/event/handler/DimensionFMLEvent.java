package de.alaoli.games.minecraft.mods.yadm.event.handler;

import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.handshake.NetworkDispatcher;
import cpw.mods.fml.relauncher.Side;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.Manageable;
import de.alaoli.games.minecraft.mods.yadm.manager.ManageableGroup;
import de.alaoli.games.minecraft.mods.yadm.manager.PlayerManager;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.manager.player.ManagePlayers;
import de.alaoli.games.minecraft.mods.yadm.network.MessageDispatcher;
import de.alaoli.games.minecraft.mods.yadm.network.SyncDimensionsMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;

public class DimensionFMLEvent 
{
	protected static final ManagePlayers playerManager = PlayerManager.INSTANCE; 
	
    @SubscribeEvent
    public void onClientConnected( FMLNetworkEvent.ServerConnectionFromClientEvent event )
    {
    	EntityPlayerMP player = ((NetHandlerPlayServer) event.handler).playerEntity;
    	
    	Log.info( "Client connected..." );
    	
    	//Register unknown players
    	if( !playerManager.existsPlayer( player ) )
    	{
    		playerManager.addPlayer( player );
    	}
    	
    	//Sync dimensions
    	if( !YADimensionManager.INSTANCE.isEmpty() )
    	{
	    	StringBuilder msg = new StringBuilder()
    			.append( "... sync Dimensions to Player '" )
	    		.append( player.getUniqueID() )
	    		.append( "':" );
	    	Log.info( msg.toString() );
	    
	    	Dimension dimension;
	    	Set<Dimension> dimensions = new HashSet<Dimension>(); 

			for( Entry<String, Manageable> groupEntry : YADimensionManager.INSTANCE.getAll() )
			{	
				for( Entry<String, Manageable> dimensionEntry : ((ManageableGroup)groupEntry.getValue()).getAll() )
				{
		    		dimension = (Dimension) dimensionEntry.getValue();
		    		dimensions.add( dimension );
		    		
					msg = new StringBuilder()
						.append( "- Dimension '" )
						.append( dimension.getManageableGroupName() )
						.append( ":" )
						.append( dimension.getManageableName() )
						.append( "' with ID '" )
						.append( dimension.getId() )
						.append( "'." );
					Log.info( msg.toString() );	  
				}
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
