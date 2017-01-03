package de.alaoli.games.minecraft.mods.yadm.network;

import java.io.IOException;
import java.util.Set;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.manager.YADimensionManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class SyncDimensionsMessage implements IMessage 
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	protected static final YADimensionManager dimensions = YADimensionManager.INSTANCE;

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
				
				if( !dimensions.existsDimension( dimension.getId() ) )
				{	 
					dimensions.addDimension( dimension );
				}			
				dimensions.registerDimension( dimension );
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
		
		Set<NBTTagCompound> nbtDimensions = dimensions.getAsNBT();
		
		buffer.writeInt( nbtDimensions.size() );
		
		for( NBTTagCompound compound : nbtDimensions )
		{
			try 
			{
				packet = new PacketBuffer( buffer );
				packet.writeNBTTagCompoundToBuffer( compound );
			} 
			catch ( IOException e )
			{
				e.printStackTrace();
			}
		}
	}
}
