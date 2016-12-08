package de.alaoli.games.minecraft.mods.yadm.manager.player;

import de.alaoli.games.minecraft.mods.yadm.YADMException;

public class PlayerException extends YADMException 
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
