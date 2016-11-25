package de.alaoli.games.minecraft.mods.yadm.world;

import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;

public interface ListOptions 
{
	public Set<Entry<Integer, Class<? extends WorldProvider>>>  listWorldProvider();
	public Set<Entry<String, WorldType>> listWorldType();
}
