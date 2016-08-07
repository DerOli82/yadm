package de.alaoli.games.minecraft.mods.yadm.proxy;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.network.MessageDispatcher;
import de.alaoli.games.minecraft.mods.yadm.network.RegisterDimensionMessage;

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
}
