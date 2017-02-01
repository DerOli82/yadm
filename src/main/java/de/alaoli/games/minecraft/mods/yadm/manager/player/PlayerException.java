package de.alaoli.games.minecraft.mods.yadm.manager.player;

import de.alaoli.games.minecraft.mods.lib.common.ModException;

public class PlayerException extends ModException 
{
	public PlayerException( String msg ) 
	{
		super( msg );
	}
	
	public PlayerException( String msg, Exception e )
	{
		super( msg, e );
	}

}
