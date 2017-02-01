package de.alaoli.games.minecraft.mods.yadm.manager.template;

import de.alaoli.games.minecraft.mods.lib.common.ModException;

public class TemplateException extends ModException 
{
	public TemplateException( String msg )
	{
		super( msg );
	}
	
	public TemplateException( String msg, Exception e )
	{
		super( msg, e );
	}
}
