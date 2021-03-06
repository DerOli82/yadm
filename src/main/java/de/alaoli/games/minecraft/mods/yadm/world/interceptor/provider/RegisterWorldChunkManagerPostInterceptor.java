package de.alaoli.games.minecraft.mods.yadm.world.interceptor.provider;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.world.interceptor.WorldProviderInterceptor;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.minecraft.world.WorldProvider;

public class RegisterWorldChunkManagerPostInterceptor extends WorldProviderInterceptor
{
	@RuntimeType
	public static void intercept( @This WorldProvider thiz )
	{
		Dimension dimension = getDimension( (DimensionFieldAccessor)thiz, thiz.dimensionId );

		thiz.dimensionId = dimension.getId();
	}
}
