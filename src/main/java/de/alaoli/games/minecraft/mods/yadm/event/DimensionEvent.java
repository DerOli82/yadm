package de.alaoli.games.minecraft.mods.yadm.event;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.WorldEvent;

public class DimensionEvent 
{
	@SubscribeEvent
	public void onWorldSave( WorldEvent.Save event )
	{
		YADM.proxy.getDimensionManager().save();
	}
	
	@SubscribeEvent
	@SideOnly( Side.CLIENT )
	public void onUnloadWorld( WorldEvent.Unload event )
	{
        if( ( event.world.provider.dimensionId == 0 ) && 
    		( event.world.isRemote ) )
        {
			Log.info( "Unload remote world dimensions..." );
			
            YADM.proxy.getDimensionManager().unregisterAll();
        }
	}
}
