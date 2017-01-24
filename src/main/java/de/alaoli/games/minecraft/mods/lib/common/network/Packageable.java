package de.alaoli.games.minecraft.mods.lib.common.network;

import net.minecraft.nbt.NBTTagCompound;

public interface Packageable 
{
	public void writeToNBT( NBTTagCompound tagCompound );
	public void readFromNBT( NBTTagCompound tagCompound );
}
