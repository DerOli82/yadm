package de.alaoli.games.minecraft.mods.yadm.manager.dimension;

import java.util.Map.Entry;
import java.util.Set;

import de.alaoli.games.minecraft.mods.yadm.manager.Manageable;

public interface ListDimensions 
{
	public Set<Entry<String, Manageable>> listDimensions();
}
