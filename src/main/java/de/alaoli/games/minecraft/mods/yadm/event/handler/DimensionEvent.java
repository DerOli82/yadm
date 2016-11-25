package de.alaoli.games.minecraft.mods.yadm.event.handler;

import java.io.IOException;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldBorderSetting;
import de.alaoli.games.minecraft.mods.yadm.event.WorldBorderEvent;
import de.alaoli.games.minecraft.mods.yadm.json.JsonFileAdapter;
import de.alaoli.games.minecraft.mods.yadm.manager.PlayerManager;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.world.ManageWorlds;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.event.world.WorldEvent;

public class DimensionEvent 
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
	public void onUnloadWorld( WorldEvent.Unload event )
	{
		//Cleanup deleted dimensions
		if( !event.world.isRemote )
		{
			worlds.deleteWorld(  event.world );
		}
	}

	@SubscribeEvent
	public void onEnteringChunk( EnteringChunk event )
	{
		//World Border, check only on Player and YADM Dimension
		if( ( event.entity instanceof EntityPlayer ) && 
			( dimensions.existsDimension( event.entity.dimension ) ) )
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			Dimension dimension = dimensions.findDimension( player.dimension );
			
			if( dimension.hasSetting( SettingType.WORLDBORDER ) )
			{
				WorldBorderSetting border = (WorldBorderSetting)dimension.get( SettingType.WORLDBORDER );
				
				border.performAction( new WorldBorderEvent( event, dimension ) );
			}
		}
	}
}
