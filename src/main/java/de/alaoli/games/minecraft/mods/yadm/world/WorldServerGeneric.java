package de.alaoli.games.minecraft.mods.yadm.world;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.EntitySpawnSetting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SpawnSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveHandler;

public class WorldServerGeneric extends WorldServer
{
	/**********************************************************************
	 * Attributes
	 **********************************************************************/
		
	public final Dimension dimension;

	/**********************************************************************
	 * Methods
	 **********************************************************************/
	
	public WorldServerGeneric( MinecraftServer mcServer, ISaveHandler saveHandler, String worldName, Dimension dimension, WorldSettings settings, Profiler profile )
	{
		super( mcServer, saveHandler, worldName, dimension.getId(), settings, profile );

		this.dimension = dimension;
	}

	/**********************************************************************
	 * Methods - Override World
	 **********************************************************************/
	
	@Override
	public ChunkCoordinates getSpawnPoint() 
	{
		if( dimension.hasSetting( SettingType.SPAWN ) )
		{
			SpawnSetting setting = (SpawnSetting)dimension.get( SettingType.SPAWN );
			
			return new ChunkCoordinates( 
				setting.getCoordinate().x,
				setting.getCoordinate().y,
				setting.getCoordinate().z
			);
		}
		else
		{
			return super.getSpawnPoint();
		}
	}

	@Override
	public boolean spawnEntityInWorld( Entity entity )
	{
		if( entity instanceof EntityPlayer )  { return super.spawnEntityInWorld( entity ); }
		if( !this.dimension.hasSetting( SettingType.ENTITYSPAWN ) ) { return super.spawnEntityInWorld( entity ); }

    	EntitySpawnSetting setting = (EntitySpawnSetting)this.dimension.get( SettingType.ENTITYSPAWN );
    		
		if( setting.isLimitReached( entity ) )
		{
			return false;
		}
		else
		{
			return super.spawnEntityInWorld( entity );
		}		
	}

	@Override
	public void onEntityAdded( Entity entity ) 
	{
		if( !( entity instanceof EntityPlayer ) &&
			( this.dimension.hasSetting( SettingType.ENTITYSPAWN ) ) )
		{
			EntitySpawnSetting setting = (EntitySpawnSetting)this.dimension.get( SettingType.ENTITYSPAWN );
			
			setting.increase( entity );
		}
		super.onEntityAdded( entity );
	}
	
	@Override
	public void onEntityRemoved( Entity entity )
	{
		if( !( entity instanceof EntityPlayer ) &&
			( this.dimension.hasSetting( SettingType.ENTITYSPAWN ) ) )
		{
			EntitySpawnSetting setting = (EntitySpawnSetting)this.dimension.get( SettingType.ENTITYSPAWN );
			
			setting.decrease( entity );
		}
		super.onEntityRemoved( entity );
	}
	
	
}
