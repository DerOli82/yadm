package de.alaoli.games.minecraft.mods.yadm.interceptor;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.WorldInfo;

public class RegisterWorldChunkManagerInterceptor 
{
	@RuntimeType
	public static void intercept( @SuperCall Callable<?> superMethod, @This WorldProvider thiz )
	{
		Dimension dimension = YADM.proxy.getDimensionManager().getById( thiz.dimensionId );
		try 
		{
			WorldInfo worldInfo;
			Class clazz	= thiz.worldObj.getWorldInfo().getClass();
			
			if( clazz == DerivedWorldInfo.class )
			{
				Field fieldWorldInfo = clazz.getDeclaredField( "theWorldInfo" );
				fieldWorldInfo.setAccessible( true );
				
				worldInfo = (WorldInfo) fieldWorldInfo.get(thiz.worldObj.getWorldInfo());
				clazz = worldInfo.getClass();				
			}
			else
			{
				worldInfo = thiz.worldObj.getWorldInfo();
			}
			Field fieldSeed				= clazz.getDeclaredField( "randomSeed" );
			Field fieldType				= clazz.getDeclaredField( "terrainType" );
			Field fieldGeneratorOptions = clazz.getDeclaredField( "generatorOptions" );
			
			fieldSeed.setAccessible( true );
			fieldType.setAccessible( true );
			fieldGeneratorOptions.setAccessible( true );
			
			fieldSeed.set( thiz.worldObj.getWorldInfo(), dimension.getSeed() );
			fieldType.set( thiz.worldObj.getWorldInfo(), dimension.getType() );
			fieldGeneratorOptions.set( thiz.worldObj.getWorldInfo(), dimension.getGeneratorOptions() );
			
			thiz.terrainType	= dimension.getType();
			thiz.field_82913_c	= dimension.getGeneratorOptions();
			
			superMethod.call();
		}
		catch ( Exception e ) 
		{
			e.printStackTrace();
		}
	}
}
