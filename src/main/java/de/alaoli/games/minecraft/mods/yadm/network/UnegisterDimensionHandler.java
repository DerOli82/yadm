package de.alaoli.games.minecraft.mods.yadm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;

public class UnegisterDimensionHandler implements IMessageHandler<UnregisterDimensionMessage, IMessage>
{
	@Override
	public IMessage onMessage( UnregisterDimensionMessage message, MessageContext context ) 
	{
		Log.info( "Receive UnregisterDimensionMessage." );
		
		int dimensionId = message.getDimensionId();
		
		if( YADimensionManager.INSTANCE.exists( dimensionId ) )
		{
			Dimension dimension = YADimensionManager.INSTANCE.get( dimensionId );	
			YADimensionManager.INSTANCE.unregister( dimension );
			YADimensionManager.INSTANCE.remove( dimension );
		}
		return null;
	}
}
