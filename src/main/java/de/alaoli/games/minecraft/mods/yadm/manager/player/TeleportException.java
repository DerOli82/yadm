package de.alaoli.games.minecraft.mods.yadm.manager.player;

import de.alaoli.games.minecraft.mods.yadm.YADMException;

public class TeleportException extends YADMException 
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
