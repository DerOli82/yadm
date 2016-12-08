package de.alaoli.games.minecraft.mods.yadm.event.handler;

import java.io.IOException;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.json.JsonFileAdapter;
import de.alaoli.games.minecraft.mods.yadm.manager.PlayerManager;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.world.ManageWorlds;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import de.alaoli.games.minecraft.mods.yadm.world.WorldServerGeneric;
import net.minecraftforge.event.world.WorldEvent;

public class DimensionEventHandler 
{
	protected static final JsonFileAdapter playerFiles = PlayerManager.INSTANCE;
	protected static final YADimensionManager dimensions = YADimensionManager.INSTANCE;
	protected static final ManageWorlds worlds = WorldBuilder.INSTANCE;
	
	@SubscribeEvent
	public void onWorldSave( WorldEvent.Save event )
	{
		try 
		{
			playerFiles.save();
			dimensions.save();
		}
		catch ( DataException | IOException e )
		{
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public void onLoadWorld( WorldEvent.Load event )
	{
		//Ugly workaround to prevent loading "vanilla" WorldServer
		if( ( !event.world.isRemote ) &&
			( dimensions.existsDimension( event.world.provider.dimensionId ) ) &&
			( !(event.world instanceof WorldServerGeneric) ) )
		{
			Dimension dimension = dimensions.findDimension( event.world.provider.dimensionId );
			worlds.getWorldServerForDimension( dimension );
		}
			
	}
	
	@SubscribeEvent
	public void onUnloadWorld( WorldEvent.Unload event )
	{
		//Cleanup deleted dimensions
		if( !event.world.isRemote )
		{
			worlds.deleteWorld(  event.world );
		}
	}
}
