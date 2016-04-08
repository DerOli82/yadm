package de.alaoli.games.minecraft.mods.yadm.world;

import de.alaoli.games.minecraft.mods.yadm.YADM;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderGeneric extends WorldProvider
{	
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private Dimension dimension;

	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/
	
	public Dimension getDimension()
	{
		if( this.dimension == null )
		{
			this.dimension = YADM.proxy.getDimensionManager().getById( this.dimensionId );
		}
		return this.dimension;
	}
	
	/********************************************************************************
	 * Methods - Override WorldProvider
	 ********************************************************************************/	
	
	@Override
	protected void registerWorldChunkManager() 
	{
		this.worldChunkMgr = this.getDimension().getType().getChunkManager( this.worldObj );
	}

	@Override
	public IChunkProvider createChunkGenerator() 
	{
		return this.getDimension().getType().getChunkGenerator( this.worldObj, null );
	}

	@Override
	public String getDimensionName() 
	{
		StringBuilder name = new StringBuilder();
		name.append( "YADM-Dimension: " );
		name.append( this.getDimension().getName() );
		
		return name.toString();
	}
}
