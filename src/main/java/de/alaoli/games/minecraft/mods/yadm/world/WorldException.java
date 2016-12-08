package de.alaoli.games.minecraft.mods.yadm.world;

import de.alaoli.games.minecraft.mods.yadm.YADMException;

public class WorldException extends YADMException 
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
