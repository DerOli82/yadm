package de.alaoli.games.minecraft.mods.yadm.world;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SpawnSetting;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveHandler;

public class WorldServerGeneric extends WorldServer
{
	public final Dimension dimension;
	
	public WorldServerGeneric( MinecraftServer mcServer, ISaveHandler saveHandler, String worldName, Dimension dimension, WorldSettings settings, Profiler profile )
	{
		super( mcServer, saveHandler, worldName, dimension.getId(), settings, profile );

		this.dimension = dimension;
	}

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
	
	
	
}
