package de.alaoli.games.minecraft.mods.yadm.event;

import java.util.Set;

import de.alaoli.games.minecraft.mods.yadm.data.settings.BorderSide;

public interface WorldBorderAction 
{
	public Set<BorderSide> allowedBorderSides();
	public void performAction( WorldBorderEvent event );
}
