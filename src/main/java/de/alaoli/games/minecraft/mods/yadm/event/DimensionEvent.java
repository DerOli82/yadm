package de.alaoli.games.minecraft.mods.yadm.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraftforge.event.world.WorldEvent;

public class DimensionEvent 
{
	public YADimensionManager manager;
	
	@SubscribeEvent
	public void onWorldLoaded( WorldEvent.Load event )
	{
		//Initialize YADimensionManager
		this.manager = YADimensionManager.getInstance( event.world );
		
		this.manager.registerDimensions();
	}
	
	@SubscribeEvent
	public void onWorldUnloaded( WorldEvent.Unload event )
	{
		this.manager.unregisterDimensions();
	}
	
	@SubscribeEvent
	public void onWorldSave( WorldEvent.Save event )
	{

	}		
}
