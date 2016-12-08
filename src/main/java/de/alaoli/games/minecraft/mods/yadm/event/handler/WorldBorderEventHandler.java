package de.alaoli.games.minecraft.mods.yadm.event.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.alaoli.games.minecraft.mods.yadm.YADMException;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldBorderSetting;
import de.alaoli.games.minecraft.mods.yadm.event.WorldBorderEvent;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;

public class WorldBorderEventHandler 
{
	protected static final YADimensionManager dimensions = YADimensionManager.INSTANCE;
	
	@SubscribeEvent
	public void onEnteringChunk( EnteringChunk event )
	{
		//World Border, check only on Player and YADM Dimension
		if( ( !event.entity.worldObj.isRemote ) &&
			( event.entity instanceof EntityPlayer ) && 
			( dimensions.existsDimension( event.entity.dimension ) ) )
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			Dimension dimension = dimensions.findDimension( player.dimension );
			
			if( dimension.hasSetting( SettingType.WORLDBORDER ) )
			{
				WorldBorderSetting border = (WorldBorderSetting)dimension.get( SettingType.WORLDBORDER );
				
				try
				{
					border.performWorldBorderEvent( new WorldBorderEvent( event, dimension ) );
				}
				catch( YADMException e )
				{
					player.addChatComponentMessage( new ChatComponentText( e.getMessage() ) );
					e.printStackTrace();
				}
			}
		}
	}
}
