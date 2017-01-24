package de.alaoli.games.minecraft.mods.lib.common.data;

import de.alaoli.games.minecraft.mods.lib.common.ModException;

public class DataException extends ModException 
{
	public DataException( String msg )
	{
		super( msg );
	}
	
	public DataException( String msg, Exception e )
	{
		super( msg, e );
	}
}
