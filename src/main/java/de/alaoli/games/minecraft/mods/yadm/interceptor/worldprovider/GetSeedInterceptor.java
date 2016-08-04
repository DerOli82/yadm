package de.alaoli.games.minecraft.mods.yadm.interceptor.worldprovider;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.interceptor.WorldProviderInterceptor;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.minecraft.world.WorldProvider;

public class GetSeedInterceptor extends WorldProviderInterceptor
{
	@RuntimeType
	public static Long intercept( @This WorldProvider thiz )
	{
		Dimension dimension = getDimension( (DimensionFieldAccessor)thiz, thiz.dimensionId );

		return dimension.getSeed();
	}
}
