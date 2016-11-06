package de.alaoli.games.minecraft.mods.yadm.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.alaoli.games.minecraft.mods.yadm.data.ChunkCoordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.BorderSide;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldBorderSetting;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.event.world.WorldEvent;

public class DimensionEvent 
{
	@SubscribeEvent
	public void onWorldSave( WorldEvent.Save event )
	{
		YADimensionManager.INSTANCE.save();
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
				
				player.addChatComponentMessage( new ChatComponentText( "x:" + event.newChunkX + " z:" + event.newChunkZ) );
				BorderSide side = border.intersectBorder(
					new ChunkCoordinate( event.oldChunkX, event.oldChunkZ ), 
					new ChunkCoordinate( event.newChunkX, event.newChunkZ )
				);
				
				if( side != null )
				{
					switch( side )
					{
						case NORTH:
							player.addChatComponentMessage( new ChatComponentText( "You reached the north border." ) );
						
							break;
							
						case EAST:
							player.addChatComponentMessage( new ChatComponentText( "You reached the east border." ) );
							
							break;
							

						case SOUTH:
							player.addChatComponentMessage( new ChatComponentText( "You reached the south border." ) );
							
							break;

						case WEST:
							player.addChatComponentMessage( new ChatComponentText( "You reached the west border." ) );
							
							break;
							
						default:
							break;
					}					
					
				}
			}
		}
	}
}
