package de.alaoli.games.minecraft.mods.yadm.event;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.Setting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SpawnSetting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.WhitelistSetting;
import de.alaoli.games.minecraft.mods.yadm.manager.player.TeleportModifier;
import de.alaoli.games.minecraft.mods.yadm.world.ManageWorlds;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.WorldServer;

public class TeleportEvent extends Event 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	protected static final ManageWorlds worlds = WorldBuilder.INSTANCE;
	
	public static final int OFFSETY = 4;

	public final Dimension dimension;
	public final WorldServer target;
	public final EntityPlayerMP player;
	
	public Coordinate coordinate;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public TeleportEvent( Dimension dimension, EntityPlayerMP player )
	{
		this( dimension, player, null );
	}
	
	public TeleportEvent( Dimension dimension, EntityPlayerMP player, Coordinate coordinate )
	{
		this.dimension = dimension;
		this.player = player;
		this.target = worlds.getWorldServerForDimension( dimension );
		
		if( coordinate == null )
		{
			if( this.dimension.hasSetting( SettingType.SPAWN ) )
			{
				SpawnSetting spawn = (SpawnSetting)this.dimension.get( SettingType.SPAWN );
				coordinate = spawn.getCoordinate();
			}
			else
			{
				coordinate = new Coordinate(
					this.target.getSpawnPoint().posX,
					this.target.getSpawnPoint().posY + OFFSETY,
					this.target.getSpawnPoint().posZ
				);
			}
		}
		this.coordinate = coordinate;
	}

	public boolean canPlayerTeleport()
	{
		ServerConfigurationManager scm = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager();
		
		//Operator and owner always allowed
		if( ( this.dimension.isOwner( this.player ) ) || ( scm.canSendCommands( this.player.getGameProfile() )) ) { return true; }
		
		if( this.dimension.hasSetting( SettingType.WHITELIST ) )
		{
			WhitelistSetting setting = (WhitelistSetting) this.dimension.get( SettingType.WHITELIST );
			
			return setting.exists( this.player.getUniqueID() );
		}
		else
		{
			return true;
		}
	}
	
	public void prepare()
	{
		//Apply modifiers
		for( Setting setting : dimension.getAll().values() )
		{
			if( setting instanceof TeleportModifier )
			{
				((TeleportModifier)setting).applyTeleportModifier( this );
			}
		}
		
		//Prepare player
		if( this.player.isRiding() )
		{
			this.player.mountEntity( null );
		}
		if( this.player.isSneaking() )
		{
			this.player.setSneaking( false );
		}		
	}
	
	/********************************************************************************
	 * Methods - Implement Event
	 ********************************************************************************/
	
	@Override
	public boolean isCancelable() 
	{
		return true;
	}
	
	
}
