package de.alaoli.games.minecraft.mods.yadm.event;

import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.DataObject;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.event.world.WorldEvent;

public class DimensionEvent 
{
    @SubscribeEvent
    @SideOnly( Side.SERVER )
    public void onClientConnected( FMLNetworkEvent.ServerConnectionFromClientEvent event )
    {
    	//Sync Dimensions
    	if( !YADM.proxy.getDimensionManager().isEmpty() )
    	{
	    	EntityPlayerMP player = ((NetHandlerPlayServer) event.handler).playerEntity;
	
	    	Set<Dimension> dimensions = new HashSet<Dimension>(); 
	    			
	    	for( Entry<String, DataObject> entry : YADM.proxy.getDimensionManager().getAll() )
	    	{
	    		dimensions.add( (Dimension)entry );
	    	}	
	    	YADM.proxy.syncDimension(dimensions, player);
    	}
    }

	@SubscribeEvent
	public void onWorldSave( WorldEvent.Save event )
	{
		YADM.proxy.getDimensionManager().save();
	}	
}
