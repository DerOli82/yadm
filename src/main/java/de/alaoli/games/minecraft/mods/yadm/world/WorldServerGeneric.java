package de.alaoli.games.minecraft.mods.yadm.world;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveHandler;

public class WorldServerGeneric extends WorldServer
{
	Dimension dimension;
	
	public WorldServerGeneric( MinecraftServer mcServer, ISaveHandler saveHandler, String worldName, Dimension dimension, WorldSettings settings, Profiler profile )
	{
		super( mcServer, saveHandler, worldName, dimension.getId(), settings, profile );

		this.dimension = dimension;
	}
}
