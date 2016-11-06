package de.alaoli.games.minecraft.mods.yadm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;

public class RegisterDimensionHandler implements IMessageHandler<RegisterDimensionMessage, IMessage>
{
	@Override
	public IMessage onMessage( RegisterDimensionMessage message, MessageContext context ) 
	{
		Log.info( "Receive RegisterDimensionMessage." );
		
		Dimension dimension = message.getDimension();
		
		if( !YADimensionManager.INSTANCE.exists( dimension ) )
		{	 
			YADimensionManager.INSTANCE.add( dimension );
		}			
		YADimensionManager.INSTANCE.register( dimension );

		return null;
	}
}
