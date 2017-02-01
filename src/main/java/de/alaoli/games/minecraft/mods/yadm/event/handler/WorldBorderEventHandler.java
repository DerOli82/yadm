package de.alaoli.games.minecraft.mods.yadm.event.handler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldBorderSetting;
import de.alaoli.games.minecraft.mods.yadm.event.WorldBorderEvent;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;

public class WorldBorderEventHandler 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	public static final WorldBorderEventHandler INSTANCE = new WorldBorderEventHandler();
	protected static final YADimensionManager dimensions = YADimensionManager.INSTANCE;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	private WorldBorderEventHandler() {}
	
	public static void register()
	{
		MinecraftForge.EVENT_BUS.register( INSTANCE );
	}	

	/********************************************************************************
	 * Methods - Forge Events
	 ********************************************************************************/
	
	@SubscribeEvent( priority=EventPriority.HIGHEST )
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
				WorldBorderSetting border = (WorldBorderSetting)dimension.getSetting( SettingType.WORLDBORDER );
				
				border.performWorldBorderEvent( new WorldBorderEvent( event, dimension ) );
			}
		}
	}
}
