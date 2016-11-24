package de.alaoli.games.minecraft.mods.yadm.manager.player;

import java.util.UUID;

import de.alaoli.games.minecraft.mods.yadm.data.Player;
import net.minecraft.entity.player.EntityPlayer;

public interface ManagePlayers
{
	public boolean existsPlayer( UUID id );
	public boolean existsPlayer( String name );
	public boolean existsPlayer( EntityPlayer entityPlayer );
	
	public void addPlayer( Player player );
	public void addPlayer( EntityPlayer entityPlayer );
	
	public void removePlayer( UUID id );
	public void removePlayer( String name );
	public void removePlayer( Player player );
	public void removePlayer( EntityPlayer entityPlayer );
}
