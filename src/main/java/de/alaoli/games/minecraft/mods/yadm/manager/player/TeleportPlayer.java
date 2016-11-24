package de.alaoli.games.minecraft.mods.yadm.manager.player;

import net.minecraft.entity.player.EntityPlayer;

public interface TeleportPlayer 
{
	public void emergencyTeleport( EntityPlayer player );
	public void teleport( TeleportSettings settings );
}
