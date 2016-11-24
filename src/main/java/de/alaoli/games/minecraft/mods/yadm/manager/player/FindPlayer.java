package de.alaoli.games.minecraft.mods.yadm.manager.player;

import java.util.UUID;

import de.alaoli.games.minecraft.mods.yadm.data.Player;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public interface FindPlayer 
{
	public Player findPlayer( UUID id ) throws PlayerException;
	public Player findPlayer( String name ) throws PlayerException;
	public Player findPlayer( ICommandSender sender ) throws PlayerException;
	public Player findPlayer( EntityPlayer entityPlayer ) throws PlayerException;
}
