package de.alaoli.games.minecraft.mods.yadm.network;

import java.io.IOException;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class UpdateDimensionMessage implements IMessage 
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	private Dimension dimension;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public UpdateDimensionMessage() {}
	
	public UpdateDimensionMessage( Dimension dimension )
	{
		this.dimension = dimension;
	}

	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/
	 
	 public Dimension getDimension()
	 {
		 return this.dimension;
	 }
	 
	/********************************************************************************
	 * Implements - IMessage
	 ********************************************************************************/
	
	@Override
	public void fromBytes( ByteBuf buffer ) 
	{	
		try 
		{
			PacketBuffer packet = new PacketBuffer( buffer );
			this.dimension = new Dimension( packet.readNBTTagCompoundFromBuffer() );
		} 
		catch ( IOException e ) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes( ByteBuf buffer) 
	{						
		try 
		{
			PacketBuffer packet	= new PacketBuffer( buffer );
			NBTTagCompound tagCompound = new NBTTagCompound();
			
			this.dimension.writeToNBT( tagCompound );
			packet.writeNBTTagCompoundToBuffer( tagCompound );
		} 
		catch ( IOException e )
		{
			e.printStackTrace();
		}		
	}
}
