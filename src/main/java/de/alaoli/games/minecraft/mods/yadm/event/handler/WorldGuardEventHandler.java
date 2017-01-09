package de.alaoli.games.minecraft.mods.yadm.event.handler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.alaoli.games.minecraft.mods.yadm.comparator.BlockComparator;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WhitelistSetting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WorldGuardSetting;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;

public class WorldGuardEventHandler 
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	public static final WorldGuardEventHandler INSTANCE = new WorldGuardEventHandler();
	protected static final YADimensionManager dimensions = YADimensionManager.INSTANCE;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	private WorldGuardEventHandler() {}
	
	public static void register()
	{
		MinecraftForge.EVENT_BUS.register( INSTANCE );
	}	
	
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
	
	protected boolean isInteractionAllowed( WorldGuardSetting worldGuard, EntityPlayer player )
	{
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

	protected boolean isFoodAllowed( WorldGuardSetting worldGuard, EntityPlayer player )
	{
		//Food isn't allowed
		if( !worldGuard.isFoodAllowed() ) { return false; }
		
		if( ( player.getCurrentEquippedItem() != null ) && 
			( player.getCurrentEquippedItem().getItem() != null ) &&
			( player.getCurrentEquippedItem().getItem() instanceof ItemFood ) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	protected boolean isLeftClickAllowed( WorldGuardSetting worldGuard, PlayerInteractEvent event )
	{
		BlockComparator block = new BlockComparator( 
			event.world.getBlock( event.x, event.y, event.z ),
			event.world.getBlockMetadata( event.x, event.y, event.z )
		);
		return worldGuard.isLeftClickAllowed( block );
	}

	
	/********************************************************************************
	 * Methods - Forge Events
	 ********************************************************************************/
	
	@SubscribeEvent( priority = EventPriority.HIGHEST )
	public void onPlayerInteract( PlayerInteractEvent event )
	{
		if( !event.world.isRemote ) {return;} 
		
		//Is YADM Dimension?
		if( !dimensions.existsDimension( event.entityPlayer.dimension ) ) { return; }
		
		ServerConfigurationManager scm = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager();
		Dimension dimension = dimensions.findDimension( event.entityPlayer.dimension );
		
		//WorldGuard active and player isn't owner or operator
		if( ( dimension.hasSetting( SettingType.WORLDGUARD ) ) && 
			( !dimension.isOwner( event.entityPlayer )) && 
			( !scm.canSendCommands( event.entityPlayer.getGameProfile() ) ) ) 
		{
			WorldGuardSetting worldGuard = (WorldGuardSetting)dimension.get( SettingType.WORLDGUARD );
			event.setCanceled( true );
			
			switch( event.action )
			{
				case RIGHT_CLICK_BLOCK :
					if( this.isWhitelisted( dimension, event.entityPlayer ) )
					{
						event.setCanceled( false );
					} 
					else
					{
						if( ( this.isInteractionAllowed( worldGuard, event.entityPlayer ) ) ||
							( this.isFoodAllowed( worldGuard, event.entityPlayer ) ) )
						{
							event.setCanceled( false );
						}
					}
					break;
					
				case LEFT_CLICK_BLOCK :
					if( ( this.isWhitelisted( dimension, event.entityPlayer ) ) || 
						( this.isLeftClickAllowed( worldGuard, event ) ) ) 
						
					{
						event.setCanceled( false );
					}					
					break;
					
				default:
					break;
			}
		}
	}
	
	@SubscribeEvent
	public void onExplosion( ExplosionEvent.Detonate event )
	{
		if( event.world.isRemote ) { return; }
		
		//Is YADM Dimension?
		if( !dimensions.existsDimension( event.world.provider.dimensionId ) ) { return; }
		
		Dimension dimension = dimensions.findDimension( event.world.provider.dimensionId );
		
		//WorldGuard active 
		if( dimension.hasSetting( SettingType.WORLDGUARD ) ) 
		{		
			event.explosion.affectedBlockPositions.clear();
		}
	}
}
