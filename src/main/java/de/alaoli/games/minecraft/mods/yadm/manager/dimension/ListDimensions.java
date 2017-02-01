package de.alaoli.games.minecraft.mods.yadm.manager.dimension;

import java.util.Map.Entry;

import de.alaoli.games.minecraft.mods.lib.common.manager.Manageable;

import java.util.Set;

public interface ListDimensions 
{
	public Set<Entry<String, Manageable>> listDimensions();
}
