package de.alaoli.games.minecraft.mods.yadm.interceptor;

import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.interceptor.worldprovider.DimensionFieldAccessor;

public abstract class WorldProviderInterceptor 
{
	protected static Dimension getDimension( DimensionFieldAccessor worldProvider, int dimId )
	{
		if( worldProvider.getDimensionYADM() == null )
		{
			Dimension dimension = YADM.proxy.getDimensionManager().getById( dimId );
			
			worldProvider.setDimensionYADM( dimension );
		}
		return worldProvider.getDimensionYADM();
	}
}
