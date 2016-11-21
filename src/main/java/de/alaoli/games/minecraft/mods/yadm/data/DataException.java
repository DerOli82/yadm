package de.alaoli.games.minecraft.mods.yadm.data;

public class DataException extends RuntimeException 
{
	public DataException( String message )
	{
		super( message );
	}
	
	public DataException( String message, Exception e )
	{
		super(message, e );
	}
}
