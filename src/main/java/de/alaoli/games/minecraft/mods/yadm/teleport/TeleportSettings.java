package de.alaoli.games.minecraft.mods.yadm.teleport;

import java.util.Set;

import de.alaoli.games.minecraft.mods.yadm.data.Coordinate;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SpawnSetting;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;

public class TeleportSettings
{
	final public int OFFSETY = 4;

	final public Dimension dimension;
	final public WorldServer target;
	final public EntityPlayer player;
	
	public Coordinate coordinate;
	
	protected Set<TeleportModifier> modifiers;
	
	public TeleportSettings( Dimension dimension, EntityPlayer player )
	{
		this( dimension, player, null );
	}
	
	public TeleportSettings( Dimension dimension, EntityPlayer player, Coordinate coordinate )
	{
		this.dimension = dimension;
		this.player = player;
		this.target = YADimensionManager.INSTANCE.getWorldServerForDimension( dimension );
		this.modifiers = TeleportModifierFactory.factory( dimension );
		
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
		for( TeleportModifier modifier : this.modifiers )
		{
			modifier.applyModifier( this );
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
