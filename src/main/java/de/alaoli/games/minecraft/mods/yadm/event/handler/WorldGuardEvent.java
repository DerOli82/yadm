package de.alaoli.games.minecraft.mods.yadm.event.handler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WhitelistSetting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldGuardSetting;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class WorldGuardEvent 
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	protected static final YADimensionManager dimensions = YADimensionManager.INSTANCE;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	protected boolean isWhitelisted( Dimension dimension, EntityPlayer player )
	{
		WorldGuardSetting worldGuard = (WorldGuardSetting)dimension.get( SettingType.WORLDGUARD );
		
		//Whitelist isn't allowed
		if( !worldGuard.isWhitelistAllowed() ) { return false; }
		
		if( dimension.hasSetting( SettingType.WHITELIST ) )
		{
			WhitelistSetting whitelist = (WhitelistSetting)dimension.get( SettingType.WHITELIST );

			return whitelist.exists( player.getUniqueID() );
		}
		else
		{
			return false;
		}
	}
	
	public boolean isInteractionAllowed( Dimension dimension, EntityPlayer player )
	{
		WorldGuardSetting worldGuard = (WorldGuardSetting)dimension.get( SettingType.WORLDGUARD );
		
		//Interaction isn't allowed
		if( !worldGuard.isInteractionAllowed() ) { return false; } 
				
		//Interaction only with empty hands
		if( player.getCurrentEquippedItem() == null )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/********************************************************************************
	 * Methods - Events
	 ********************************************************************************/
	
	@SideOnly( Side.SERVER )
	@SubscribeEvent( priority = EventPriority.HIGHEST )
	public void onPlayerInteract( PlayerInteractEvent event )
	{
		//Is YADM Dimension?
		if( !dimensions.existsDimension( event.entityPlayer.dimension ) ) { return; }
		
		ServerConfigurationManager scm = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager();
		Dimension dimension = dimensions.findDimension( event.world.provider.dimensionId );
		
		//WorldGuard active and player isn't owner or operator
		if( ( dimension.hasSetting( SettingType.WORLDGUARD ) ) && 
			( !dimension.isOwner( event.entityPlayer )) && 
			( !scm.canSendCommands( event.entityPlayer.getGameProfile() ) ) ) 
		{
			event.setCanceled( true );
			
			switch( event.action )
			{
				case RIGHT_CLICK_BLOCK :
					if( this.isWhitelisted(dimension, event.entityPlayer ) )
					{
						event.setCanceled( false );
					} 
					else
					{
						if( this.isInteractionAllowed(dimension, event.entityPlayer ) )
						{
							event.setCanceled( false );
						}
					}
					break;
					
				case LEFT_CLICK_BLOCK :
					if( this.isWhitelisted( dimension, event.entityPlayer ) )
					{
						event.setCanceled( false );
					}
					break;
					
				default:
					break;
			}
	
		}
	}
}
