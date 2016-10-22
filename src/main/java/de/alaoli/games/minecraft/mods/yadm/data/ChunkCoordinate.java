package de.alaoli.games.minecraft.mods.yadm.data;

import javax.vecmath.Vector2d;

public class ChunkCoordinate 
{
	public final int x;
	
	public final int z;
	
	public ChunkCoordinate( int x, int z )
	{
		this.x = x;
		this.z = z;
	}

	/**
	 * Get vector
	 * 
	 * @param chunkCoordinate
	 * @return
	 */
	public Vector2d vector( ChunkCoordinate chunkCoordinate )
	{	
		return new Vector2d( this.x - chunkCoordinate.x, this.z - chunkCoordinate.z );
	}
}
