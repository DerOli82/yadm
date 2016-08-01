package de.alaoli.games.minecraft.mods.yadm.proxy;

import java.util.Set;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.network.DimensionSyncHandler;
import de.alaoli.games.minecraft.mods.yadm.network.DimensionSyncMessage;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerProxy extends CommonProxy
{
	/********************************************************************************
	 * Methods - FML
	 ********************************************************************************/
	
	@Override
	public void preInit( FMLPreInitializationEvent event ) 
	{
		super.preInit( event );
		
		network.registerMessage( DimensionSyncHandler.class, DimensionSyncMessage.class, 0, Side.SERVER );
	}	
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	@Override
	public void syncDimension( Dimension dimension )
	{
		network.sendToAll( new DimensionSyncMessage( dimension ));
	}
	
	@Override
	public void syncDimension( Set<Dimension> dimensions, EntityPlayerMP player )
	{
		network.sendTo(new DimensionSyncMessage( dimensions ), player);
	}
}
