package de.alaoli.games.minecraft.mods.yadm.data;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class Dimension 
{
	private int id;
	
	private String name;
	
	private WorldServer world;
	
	public Dimension( int id, String name )
	{
		this.id = id;
		this.name = name;
	}

	public int getId() 
	{
		return this.id;
	}

	public String getName() 
	{
		return this.name;
	}

	public WorldServer getWorld() 
	{
		if( this.world == null ) 
		{
			this.world = DimensionManager.getWorld( this.id );
		}
		return this.world;
	}
	
	
}
