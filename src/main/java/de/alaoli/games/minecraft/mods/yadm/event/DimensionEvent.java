package de.alaoli.games.minecraft.mods.yadm.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.alaoli.games.minecraft.mods.yadm.YADM;
import net.minecraftforge.event.world.WorldEvent;

public class DimensionEvent 
{
	@SubscribeEvent
	public void onWorldSave( WorldEvent.Save event )
	{
		YADM.proxy.getDimensionManager().save();
	}	
}
