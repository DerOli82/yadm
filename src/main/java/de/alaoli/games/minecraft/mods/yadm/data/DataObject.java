package de.alaoli.games.minecraft.mods.yadm.data;

import net.minecraft.nbt.NBTTagCompound;

public interface DataObject 
{
	public String getName();
	
	public void writeToNBT( NBTTagCompound tagCompound );
	
	public void readFromNBT( NBTTagCompound tagCompound );
}
