package de.alaoli.games.minecraft.mods.lib.common;

public class ModException extends RuntimeException
{
	public ModException( String msg )
	{
		super( msg );
	}
	
	public ModException( String msg, Exception e )
	{
		super( msg, e );
	}
}
