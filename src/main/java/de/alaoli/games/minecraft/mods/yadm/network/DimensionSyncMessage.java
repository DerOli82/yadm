package de.alaoli.games.minecraft.mods.yadm.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import gnu.trove.list.array.TIntArrayList;
import io.netty.buffer.ByteBuf;

public class DimensionSyncMessage implements IMessage 
{
	/********************************************************************************
	 * Constant
	 ********************************************************************************/
	
	public static final int MAX_BYTES = 4;
	
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	private TIntArrayList dimensionIds;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public DimensionSyncMessage()
	{
		this.dimensionIds = new TIntArrayList();
	}
	
	public DimensionSyncMessage( int dimensionId )
	{
		this.dimensionIds = new TIntArrayList();
		this.dimensionIds.add( dimensionId );
	}
	
	public DimensionSyncMessage( TIntArrayList dimensionIds )
	{
		this.dimensionIds = dimensionIds;
	}
	
	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/
	 
	 public TIntArrayList getDimensionIds()
	 {
		 return this.dimensionIds;
	 }
	 
	/********************************************************************************
	 * Implements - IMessage
	 ********************************************************************************/
	
	@Override
	public void fromBytes(ByteBuf buffer ) 
	{	
		int size = ByteBufUtils.readVarInt( buffer, MAX_BYTES );
		
		for( int i = 0; i < size; i++ )
		{
			this.dimensionIds.add( ByteBufUtils.readVarInt( buffer, MAX_BYTES ) );
		}
		
	}

	@Override
	public void toBytes(ByteBuf buffer) 
	{
		ByteBufUtils.writeVarInt( buffer, this.dimensionIds.size(), MAX_BYTES );
		
		for( int i = 0; i < this.dimensionIds.size(); i++ )
		{
			ByteBufUtils.writeVarInt( buffer, this.dimensionIds.get( i ), MAX_BYTES );
		}
	}
}
