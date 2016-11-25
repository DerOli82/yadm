package de.alaoli.games.minecraft.mods.yadm.world.interceptor;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import de.alaoli.games.minecraft.mods.yadm.manager.dimension.FindDimension;
import de.alaoli.games.minecraft.mods.yadm.world.interceptor.provider.DimensionFieldAccessor;

public abstract class WorldProviderInterceptor 
{
	protected static final FindDimension dimensions = YADimensionManager.INSTANCE;
			
	protected static Dimension getDimension( DimensionFieldAccessor worldProvider, int dimensionId )
	{
		if( worldProvider.getDimensionYADM() == null )
		{
			worldProvider.setDimensionYADM( dimensions.findDimension( dimensionId ) );
		}
		return worldProvider.getDimensionYADM();
	}
}
