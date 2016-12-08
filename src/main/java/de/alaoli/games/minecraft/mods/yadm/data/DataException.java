package de.alaoli.games.minecraft.mods.yadm.data;

import de.alaoli.games.minecraft.mods.yadm.YADMException;

public class DataException extends YADMException 
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
