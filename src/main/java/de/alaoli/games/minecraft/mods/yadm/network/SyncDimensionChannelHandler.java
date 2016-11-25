package de.alaoli.games.minecraft.mods.yadm.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.relauncher.Side;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.ManageDimensions;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class SyncDimensionChannelHandler extends FMLIndexedMessageToMessageCodec<SyncDimensionsMessage>
{
	protected static final ManageDimensions dimensions = YADimensionManager.INSTANCE;
	
    public SyncDimensionChannelHandler() {
        addDiscriminator(0, SyncDimensionsMessage.class);
    }
    
	@Override
	public void encodeInto( ChannelHandlerContext ctx, SyncDimensionsMessage msg, ByteBuf target ) throws Exception 
	{
		msg.toBytes( target );
		
	}

	@Override
	public void decodeInto( ChannelHandlerContext ctx, ByteBuf source, SyncDimensionsMessage msg ) 
	{
		msg.fromBytes( source );
		
		if( FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT )
		{
			for( Dimension dimension : msg.getDimensions() )
			{	
				if( !dimensions.existsDimension( dimension.getId() ) )
				{	 
					dimensions.addDimension( dimension );
				}			
				dimensions.registerDimension( dimension );
			}
		}
	}
}
