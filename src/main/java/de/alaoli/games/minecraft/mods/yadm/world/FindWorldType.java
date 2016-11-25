package de.alaoli.games.minecraft.mods.yadm.world;

import net.minecraft.world.WorldType;

public interface FindWorldType 
{
	public WorldType findWorldType( String name ) throws WorldException;
}
