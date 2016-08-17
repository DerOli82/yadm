package de.alaoli.games.minecraft.mods.yadm.network;

import net.minecraft.nbt.NBTTagCompound;

public interface Packageable 
{
	public void writeToNBT( NBTTagCompound tagCompound );
	
	public void readFromNBT( NBTTagCompound tagCompound );
}
