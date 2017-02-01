package de.alaoli.games.minecraft.mods.yadm.world;

import de.alaoli.games.minecraft.mods.lib.common.ModException;

public class WorldException extends ModException 
{
	public WorldException( String msg )
	{
		super( msg ); 
	}
	
	public WorldException( String msg, Exception e )
	{
		super( msg, e ); 
	}	
}
