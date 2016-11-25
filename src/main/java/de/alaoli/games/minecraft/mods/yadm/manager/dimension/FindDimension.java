package de.alaoli.games.minecraft.mods.yadm.manager.dimension;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;

public interface FindDimension 
{
	public Dimension findDimension( int dimensionId ) throws DimensionException;
	public Dimension findDimension( String name ) throws DimensionException;
	public Dimension findDimension( String group, String name ) throws DimensionException;
}
