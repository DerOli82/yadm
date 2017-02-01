package de.alaoli.games.minecraft.mods.yadm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.DimensionException;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.FindDimension;

public class UpdateDimensionHandler implements IMessageHandler<UpdateDimensionMessage, IMessage>
{
	protected static final FindDimension dimensions = YADimensionManager.INSTANCE;
	
	@Override
	public IMessage onMessage( UpdateDimensionMessage message, MessageContext context ) 
	{
		Log.info( "Receive UpdateDimensionMessage." );
		
		Dimension dimension = message.getDimension();

		try
		{
			dimensions.findDimension( dimension.getId() ).updateSetting( dimension.getSetting( SettingType.SPAWN) );
		}
		catch( DimensionException e )
		{
			Log.error( e.getMessage() );
		}
		
		return null;
	}
}
