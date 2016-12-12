package de.alaoli.games.minecraft.mods.yadm.manager.player;

import de.alaoli.games.minecraft.mods.yadm.event.TeleportEvent;

public interface TeleportModifier 
{
	public void applyTeleportModifier( TeleportEvent event );
}
