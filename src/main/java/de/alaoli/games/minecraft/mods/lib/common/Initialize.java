package de.alaoli.games.minecraft.mods.lib.common;

import java.io.IOException;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import de.alaoli.games.minecraft.mods.lib.common.data.DataException;

public interface Initialize 
{
	public void preInit( FMLPreInitializationEvent event ) throws IOException, DataException;
	public void init( FMLInitializationEvent event ) throws IOException, DataException;
	public void postInit( FMLPostInitializationEvent event ) throws IOException, DataException;
}
