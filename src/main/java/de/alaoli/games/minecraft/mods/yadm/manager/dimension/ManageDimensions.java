package de.alaoli.games.minecraft.mods.yadm.manager.dimension;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.Template;

public interface ManageDimensions 
{
	public void registerDimensions() throws DimensionException;
	public void registerDimension( Dimension dimension ) throws DimensionException;
	
	public void unregisterDimensions() throws DimensionException;
	public void unregisterDimension( Dimension dimension ) throws DimensionException;
	
	public boolean existsDimension( int id );
	public boolean existsDimension( String name );
	public boolean existsDimension( String group, String name );
	
	public Dimension createDimension( String name, Template template ) throws DimensionException;
	public Dimension createDimension( String group, String name, Template template ) throws DimensionException;
	
	public void deleteDimension( int id ) throws DimensionException;
	public void deleteDimension( String name ) throws DimensionException;
	public void deleteDimension( String group, String name ) throws DimensionException;
	public void deleteDimension( Dimension dimension ) throws DimensionException;
}
