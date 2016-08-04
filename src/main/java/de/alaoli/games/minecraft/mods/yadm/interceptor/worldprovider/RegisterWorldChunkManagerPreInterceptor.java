package de.alaoli.games.minecraft.mods.yadm.interceptor.worldprovider;

import cpw.mods.fml.relauncher.ReflectionHelper;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.interceptor.WorldProviderInterceptor;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.WorldInfo;

public class RegisterWorldChunkManagerPreInterceptor extends WorldProviderInterceptor
{	
	private static void reflectionWorldInfo( WorldInfo worldInfo, Dimension dimension )
	{
		ReflectionHelper.setPrivateValue( 
			WorldInfo.class, worldInfo, 
			dimension.getName(), new String[] { "field_76106_k", "levelName" } 
		);
		/*
		ReflectionHelper.setPrivateValue( 
			WorldInfo.class, worldInfo, 
			dimension.getSeed(), new String[] { "field_76100_a", "randomSeed" } 
		);*/
		ReflectionHelper.setPrivateValue( 
			WorldInfo.class, worldInfo, 
			dimension.getType(), new String[] { "field_76098_b", "terrainType" } 
		);
		ReflectionHelper.setPrivateValue( 
			WorldInfo.class, worldInfo, 
			dimension.getGeneratorOptions(), new String[] { "field_82576_c", "generatorOptions" } 
		);		
	}
	
	@RuntimeType
	public static void intercept( @This WorldProvider thiz )
	{
		Dimension dimension = getDimension( (DimensionFieldAccessor)thiz, thiz.dimensionId );

		try 
		{
			WorldInfo worldInfo = thiz.worldObj.getWorldInfo();
			Class clazz	= worldInfo.getClass();

			//Get and reflect theWorldInfo
			if( clazz == DerivedWorldInfo.class )
			{
				worldInfo = (WorldInfo) ReflectionHelper.getPrivateValue( 
					DerivedWorldInfo.class, (DerivedWorldInfo)worldInfo, 
					new String[] { "field_76115_a", "theWorldInfo" } 
				);
				reflectionWorldInfo( worldInfo, dimension );
				
				//Get DerivedWorldInfo again
				worldInfo = thiz.worldObj.getWorldInfo();
			}
			reflectionWorldInfo( worldInfo, dimension );
			
			thiz.terrainType	= dimension.getType();
			thiz.field_82913_c	= dimension.getGeneratorOptions();
		}
		catch ( Exception e ) 
		{
			e.printStackTrace();
		}
	}
}
