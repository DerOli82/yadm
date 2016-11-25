package de.alaoli.games.minecraft.mods.yadm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.ManageDimensions;

public class RegisterDimensionHandler implements IMessageHandler<RegisterDimensionMessage, IMessage>
{
	protected static final ManageDimensions dimensions = YADimensionManager.INSTANCE;
	
	@Override
	public IMessage onMessage( RegisterDimensionMessage message, MessageContext context ) 
	{
		Log.info( "Receive RegisterDimensionMessage." );
		
		Dimension dimension = message.getDimension();
		
		if( !dimensions.existsDimension( dimension.getId() ) )
		{	 
			dimensions.addDimension( dimension );
		}			
		dimensions.registerDimension( dimension );

		return null;
	}
}
