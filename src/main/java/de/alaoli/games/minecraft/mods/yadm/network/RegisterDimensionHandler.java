package de.alaoli.games.minecraft.mods.yadm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;

public class RegisterDimensionHandler implements IMessageHandler<RegisterDimensionMessage, IMessage>
{
	@Override
	public IMessage onMessage( RegisterDimensionMessage message, MessageContext context ) 
	{
		Log.info( "Receive RegisterDimensionMessage." );
		
		Dimension dimension = message.getDimension();
		
		if( !YADM.proxy.getDimensionManager().exists( dimension ) )
		{	 
			YADM.proxy.getDimensionManager().add( dimension );
		}			
		YADM.proxy.getDimensionManager().register( dimension );

		return null;
	}
}
