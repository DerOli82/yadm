package de.alaoli.games.minecraft.mods.lib.common.comparator;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class BlockComparator 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	protected Block block;
	
	protected int blockMetaId = 0;
	
	private boolean ignoreMeta = false;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public BlockComparator( Block block )
	{
		this.block = block;
	}
	
	public BlockComparator( Block block, int metaId )
	{
		this.block = block;
		this.blockMetaId = metaId;
	}

	
	public BlockComparator( Block block, boolean ignoreMeta )
	{
		this.block = block;
		this.ignoreMeta = ignoreMeta;
	}

	@Override
	public int hashCode() 
	{
		if( this.ignoreMeta )
		{
			return this.block.hashCode();
		}
		else
		{
			return this.block.hashCode() + this.blockMetaId;
		}
	}

	@Override
	public boolean equals( Object obj ) 
	{
		if( !(obj instanceof BlockComparator) ) { return false;}
		
		if( this.ignoreMeta )
		{
			return this.block.equals( ((BlockComparator)obj).block );
		}
		else
		{
			return this.block.equals( ((BlockComparator)obj).block ) &&
					( this.blockMetaId == ((BlockComparator)obj).blockMetaId );
		}
	}

	@Override
	public String toString() 
	{
		StringBuilder result = new StringBuilder()
			.append( GameRegistry.findUniqueIdentifierFor( this.block ) );
			
		if( this.ignoreMeta )
		{
			result.append( ":*" );
		}
		else
		{
			if( this.blockMetaId == 0 )
			{
				result.append( ":" );
				result.append( this.blockMetaId );
			}
		}		
		return result.toString();
	}
}
