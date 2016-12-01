package de.alaoli.games.minecraft.mods.yadm.manager.player;

import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.Setting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SpawnSetting;
import de.alaoli.games.minecraft.mods.yadm.world.ManageWorlds;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;

public class TeleportSettings
{
	protected static final ManageWorlds worlds = WorldBuilder.INSTANCE;
	
	public static final int OFFSETY = 4;

	public final Dimension dimension;
	public final WorldServer target;
	public final EntityPlayer player;
	
	public Coordinate coordinate;
	
	public TeleportSettings( Dimension dimension, EntityPlayer player )
	{
		this( dimension, player, null );
	}
	
	public TeleportSettings( Dimension dimension, EntityPlayer player, Coordinate coordinate )
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

}
