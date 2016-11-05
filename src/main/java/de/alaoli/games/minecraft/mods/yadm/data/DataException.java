package de.alaoli.games.minecraft.mods.yadm.data;

public class DataException extends RuntimeException 
{
	public DataException( String message )
	{
		super( message );
	}
	
	public DataException( String message, DataException e )
	{
		super(message, e );
	}
}
