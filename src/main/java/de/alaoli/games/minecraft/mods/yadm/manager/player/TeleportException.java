package de.alaoli.games.minecraft.mods.yadm.manager.player;

import de.alaoli.games.minecraft.mods.lib.common.ModException;

public class TeleportException extends ModException 
{
	public TeleportException( String msg )
	{
		super( msg );
	}
	
	public TeleportException( String msg, Exception e )
	{
		super( msg, e );
	}
}
