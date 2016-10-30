package de.alaoli.games.minecraft.mods.yadm.interceptor.worldprovider;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.interceptor.WorldProviderInterceptor;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.minecraft.world.WorldProvider;

public class GetDimensionNameInterceptor extends WorldProviderInterceptor
{
	@RuntimeType
	public static String intercept( @This WorldProvider thiz )
	{
		Dimension dimension = getDimension( (DimensionFieldAccessor)thiz, thiz.dimensionId );

		return dimension.getGroup() + ":" + dimension.getName();
	}
}
