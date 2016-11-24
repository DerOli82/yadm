package de.alaoli.games.minecraft.mods.yadm.world;

import java.util.Collection;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;

public interface ListOptions 
{
	public Collection<Class<? extends WorldProvider>> listWorldProvider();
	public Collection<WorldType> listWorldType();
}
