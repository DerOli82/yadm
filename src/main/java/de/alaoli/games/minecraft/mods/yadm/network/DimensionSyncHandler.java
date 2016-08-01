package de.alaoli.games.minecraft.mods.yadm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.proxy.CommonProxy;
import de.alaoli.games.minecraft.mods.yadm.world.WorldProviderGeneric;
import net.minecraftforge.common.DimensionManager;

public class DimensionSyncHandler implements IMessageHandler<DimensionSyncMessage, IMessage>
{
	@Override
	public IMessage onMessage( DimensionSyncMessage message, MessageContext ctx ) 
	{
		for( Dimension dimension : message.getDimensions() )
		{
			
			CommonProxy proxy = YADM.proxy;
			YADimensionManager dm = YADM.proxy.getDimensionManager();
					
			if( !YADM.proxy.getDimensionManager().exists( dimension ) )
			{
				YADM.proxy.getDimensionManager().add( dimension );
			}
			if( !DimensionManager.isDimensionRegistered( dimension.getId() ) )
			{
				DimensionManager.registerProviderType( dimension.getId(), WorldProviderGeneric.class, false );
	            DimensionManager.registerDimension( dimension.getId(), dimension.getId() );
			}
			
		}
		return null;
	}
}
