package de.alaoli.games.minecraft.mods.yadm.interceptor.worldprovider;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;

public interface DimensionFieldAccessor 
{
	public Dimension getDimensionYADM();
	public void setDimensionYADM( Dimension dimension );
}
