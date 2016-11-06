package de.alaoli.games.minecraft.mods.yadm.interceptor;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.interceptor.worldprovider.DimensionFieldAccessor;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;

public abstract class WorldProviderInterceptor 
{
	protected static Dimension getDimension( DimensionFieldAccessor worldProvider, int dimId )
	{
		if( worldProvider.getDimensionYADM() == null )
		{
			Dimension dimension = YADimensionManager.INSTANCE.get( dimId );
			
			worldProvider.setDimensionYADM( dimension );
		}
		return worldProvider.getDimensionYADM();
	}
}
