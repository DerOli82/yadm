package de.alaoli.games.minecraft.mods.yadm.world.interceptor.provider;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SpawnSetting;
import de.alaoli.games.minecraft.mods.yadm.world.interceptor.WorldProviderInterceptor;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldProvider;

public class GetSpawnPointInterceptor extends WorldProviderInterceptor
{
	@RuntimeType
	public static ChunkCoordinates intercept( @This WorldProvider thiz )
	{
		Dimension dimension = getDimension( (DimensionFieldAccessor)thiz, thiz.dimensionId );

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
			return thiz.getSpawnPoint();
		}
	}
}
