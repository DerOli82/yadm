package de.alaoli.games.minecraft.mods.yadm.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraftforge.event.world.WorldEvent;

public class DimensionEvent 
{
	@SubscribeEvent
	public void onWorldSave( WorldEvent.Save event )
	{
		YADimensionManager.instance.save();
	}
	
	@SubscribeEvent
	public void onUnloadWorld( WorldEvent.Unload event )
	{
		//Cleanup deleted dimensions
		if( !event.world.isRemote ) 
		{
			YADimensionManager.instance.cleanup( event.world );
		}
	}
}
