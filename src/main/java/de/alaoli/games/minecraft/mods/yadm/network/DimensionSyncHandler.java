package de.alaoli.games.minecraft.mods.yadm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;

public class DimensionSyncHandler implements IMessageHandler<DimensionSyncMessage, IMessage>
{
	@Override
	public IMessage onMessage( DimensionSyncMessage message, MessageContext ctx ) 
	{
		for( Dimension dimension : message.getDimensions() )
		{	
			if( !YADM.proxy.getDimensionManager().exists( dimension ) )
			{
				 
				YADM.proxy.getDimensionManager().add( dimension );
				YADM.proxy.getDimensionManager().register( dimension );
				
				if( ctx.side == Side.SERVER )
				{
					
					YADM.proxy.getDimensionManager().init( dimension );
				}
			}			
		}
		return null;
	}
}
