package de.alaoli.games.minecraft.mods.yadm.network;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class SyncDimensionsMessage implements IMessage 
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	private Set<Dimension> dimensions;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public SyncDimensionsMessage()
	{
		this.dimensions = new HashSet<Dimension>();
	}
	
	public SyncDimensionsMessage( Dimension dimension )
	{
		this();
		this.dimensions.add( dimension );
	}
	
	public SyncDimensionsMessage( Set<Dimension> dimensions )
	{
		this.dimensions = dimensions;
	}
	
	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/
	 
	 public Set<Dimension> getDimensions()
	 {
		 return this.dimensions;
	 }
	 
	/********************************************************************************
	 * Implements - IMessage
	 ********************************************************************************/
	
	@Override
	public void fromBytes( ByteBuf buffer ) 
	{	
		PacketBuffer packet;
		NBTTagCompound tagCompound;
		Dimension dimension;
		
		int size = buffer.readInt();
		
		for( int i = 0; i < size; i++ )
		{
			try 
			{
				packet = new PacketBuffer( buffer );
				dimension = new Dimension( packet.readNBTTagCompoundFromBuffer() );
				
				this.dimensions.add( dimension );
			} 
			catch ( IOException e ) 
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void toBytes( ByteBuf buffer) 
	{
		PacketBuffer packet;
		NBTTagCompound tagCompound;
		
		buffer.writeInt( this.dimensions.size() );
		
		for( Dimension dimension : this.dimensions )
		{						
			try 
			{
				packet		= new PacketBuffer( buffer );
				tagCompound	= new NBTTagCompound();
				
				dimension.writeToNBT( tagCompound );
				packet.writeNBTTagCompoundToBuffer(tagCompound);
			} 
			catch ( IOException e )
			{
				e.printStackTrace();
			}
			
		}
	}
}
