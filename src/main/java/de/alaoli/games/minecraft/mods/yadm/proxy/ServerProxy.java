package de.alaoli.games.minecraft.mods.yadm.proxy;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.network.MessageDispatcher;
import de.alaoli.games.minecraft.mods.yadm.network.RegisterDimensionMessage;
import de.alaoli.games.minecraft.mods.yadm.network.UnregisterDimensionMessage;

public class ServerProxy extends CommonProxy
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	@Override
	public void registerDimension( Dimension dimension ) 
	{
		super.registerDimension(dimension);
		
		MessageDispatcher.network.sendToAll( new RegisterDimensionMessage( dimension ) );
	}
	
	@Override
	public void unregisterDimension( Dimension dimension )
	{
		super.unregisterDimension( dimension );
		
		MessageDispatcher.network.sendToAll( new UnregisterDimensionMessage( dimension.getId() ) );
	}
}
