package de.alaoli.games.minecraft.mods.yadm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.DimensionGroup;
import de.alaoli.games.minecraft.mods.yadm.manager.ManageableGroup;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;

public class RegisterDimensionHandler implements IMessageHandler<RegisterDimensionMessage, IMessage>
{
	@Override
	public IMessage onMessage( RegisterDimensionMessage message, MessageContext context ) 
	{
		Log.info( "Receive RegisterDimensionMessage." );
		
		Dimension dimension = message.getDimension();
		
		if( !YADimensionManager.instance.exists( dimension ) )
		{	 
			//Create new group
			if( !YADimensionManager.instance.exists( dimension.getGroup() ) )
			{
				YADimensionManager.instance.add( new DimensionGroup( dimension.getGroup() ) );
			}
			((ManageableGroup)YADimensionManager.instance.get( dimension.getGroup() )).add( dimension );
		}			
		YADimensionManager.instance.register( dimension );

		return null;
	}
}
