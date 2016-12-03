package de.alaoli.games.minecraft.mods.yadm.event;

import java.util.Set;

import de.alaoli.games.minecraft.mods.yadm.data.settings.BorderSide;

public interface PerformWorldBorderEvent 
{
	public int priority();
	public Set<BorderSide> allowedBorderSides();
	public void performWorldBorderEvent( WorldBorderEvent event );
}
