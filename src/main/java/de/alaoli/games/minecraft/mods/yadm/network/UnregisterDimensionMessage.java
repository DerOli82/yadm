package de.alaoli.games.minecraft.mods.yadm.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class UnregisterDimensionMessage implements IMessage 
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	private int dimensionId;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public UnregisterDimensionMessage() {}
	
	public UnregisterDimensionMessage( int dimensionId )
	{
		this.dimensionId = dimensionId;
	}

	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/
	 
	 public int getDimensionId()
	 {
		 return this.dimensionId;
	 }
	
	/********************************************************************************
	 * Implements - IMessage
	 ********************************************************************************/
	
	@Override
	public void fromBytes( ByteBuf buffer ) 
	{	
		this.dimensionId = buffer.readInt();
	}

	@Override
	public void toBytes( ByteBuf buffer) 
	{						
		buffer.writeInt( this.dimensionId );	
	}
}
