package de.alaoli.games.minecraft.mods.yadm;

public class YADMException extends RuntimeException
{
	public YADMException( String msg )
	{
		super( msg );
	}
	
	public YADMException( String msg, Exception e )
	{
		super( msg, e );
	}
}
