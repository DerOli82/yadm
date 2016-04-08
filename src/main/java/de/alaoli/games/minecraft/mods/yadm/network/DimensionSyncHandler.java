package de.alaoli.games.minecraft.mods.yadm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.world.WorldProviderGeneric;
import gnu.trove.list.array.TIntArrayList;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

public class DimensionSyncHandler implements IMessageHandler<DimensionSyncMessage, IMessage>
{
	@Override
	public IMessage onMessage( DimensionSyncMessage message, MessageContext ctx ) 
	{

		int dimensionId;
		TIntArrayList dimensionIds = message.getDimensionIds();
		
		for( int i = 0; i < dimensionIds.size(); i++ )
		{
			
			dimensionId = dimensionIds.get( i );
			
			System.out.println( "Sync Dimension " + String.valueOf( dimensionId ));
			
			if( !DimensionManager.isDimensionRegistered( dimensionId) )
			{
				try 
				{
					//TODO World Provider
					WorldProvider provider = YADM.proxy.getPatternManager().getProvider("");
					
					DimensionManager.registerProviderType( dimensionId, WorldProviderGeneric.class, false );
	                DimensionManager.registerDimension( dimensionId, dimensionId );
				}
				catch( ClassNotFoundException | InstantiationException | IllegalAccessException e ) 
				{
				
					e.printStackTrace();
				}
			}
		}
	
		return null;
	}
}
