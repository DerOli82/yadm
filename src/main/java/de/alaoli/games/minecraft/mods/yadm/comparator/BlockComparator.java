package de.alaoli.games.minecraft.mods.yadm.comparator;

import cpw.mods.fml.common.registry.GameRegistry;
import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import net.minecraft.block.Block;

public class BlockComparator 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	protected Block block;
	
	protected int blockMetaId = 0;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public BlockComparator( String name )
	{
		String splitted[] = name.split( ":" );
		
		if( splitted.length < 2 ) { throw new DataException("Minecraft unique identifier requires <modid>:<itemid>."); }
		
		this.block = GameRegistry.findBlock( splitted[0], splitted[1] );
		
		if( block == null ) { throw new DataException("Block not found." ); }
			
		if( splitted.length > 2 )
		{
			this.blockMetaId = Integer.valueOf( splitted[2] );
		}
	}

	public BlockComparator( Block block )
	{
		this.block = block;
	}
	
	public BlockComparator( Block block, int metaId )
	{
		this.block = block;
		this.blockMetaId = metaId;
	}
	
	
	@Override
	public int hashCode() 
	{
		return this.block.hashCode() + this.blockMetaId;
	}

	@Override
	public boolean equals( Object obj ) 
	{
		if( !(obj instanceof BlockComparator) ) { return false;}
		
		return this.block.equals( ((BlockComparator)obj).block ) &&
				( this.blockMetaId == ((BlockComparator)obj).blockMetaId );
	}

	@Override
	public String toString() 
	{
		return GameRegistry.findUniqueIdentifierFor( this.block ) + ":" + this.blockMetaId;
	}	
}
