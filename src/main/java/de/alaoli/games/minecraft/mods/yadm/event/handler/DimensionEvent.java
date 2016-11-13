package de.alaoli.games.minecraft.mods.yadm.event.handler;

import java.io.IOException;

import javax.vecmath.Vector2d;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldBorderSetting;
import de.alaoli.games.minecraft.mods.yadm.event.KnockbackEvent;
import de.alaoli.games.minecraft.mods.yadm.event.WorldBorderEvent;
import de.alaoli.games.minecraft.mods.yadm.manager.PlayerManager;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.event.world.WorldEvent;

public class DimensionEvent 
{
	@SubscribeEvent
	public void onKnockback( KnockbackEvent event )
	{
		Log.info( "knockback event");
		
		EntityPlayer player = (EntityPlayer)event.entity;
		
		Vector2d centerPlayer = new Vector2d( 
			event.borderEvent.chunkEvent.newChunkX - event.borderEvent.setting.getPointCenter().x,
			event.borderEvent.chunkEvent.newChunkZ - event.borderEvent.setting.getPointCenter().z
		);
		centerPlayer.scale( -1 / centerPlayer.length());
		
		player.motionX = centerPlayer.x * 1;
		player.motionY = 0.4D;
		player.motionZ = centerPlayer.y * 1;
		
	}
	
	@SubscribeEvent
	public void onWorldSave( WorldEvent.Save event )
	{
		try 
		{
			PlayerManager.INSTANCE.save();
			YADimensionManager.INSTANCE.save();
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
			YADimensionManager.INSTANCE.delete( event.world );
		}
	}

	@SubscribeEvent
	public void onEnteringChunk( EnteringChunk event )
	{
		//World Border, check only on Player and YADM Dimension
		if( ( event.entity instanceof EntityPlayer ) && 
			( YADimensionManager.INSTANCE.exists( event.entity.dimension ) ) )
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			Dimension dimension = YADimensionManager.INSTANCE.get( player.dimension );
			
			if( dimension.hasSetting( SettingType.WORLDBORDER ) )
			{
				WorldBorderSetting border = (WorldBorderSetting)dimension.get( SettingType.WORLDBORDER );
				
				border.performAction( new WorldBorderEvent( event, dimension ) );
			}
		}
	}
}
