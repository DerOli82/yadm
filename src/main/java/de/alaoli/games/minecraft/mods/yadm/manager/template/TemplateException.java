package de.alaoli.games.minecraft.mods.yadm.manager.template;

import de.alaoli.games.minecraft.mods.yadm.YADMException;

public class TemplateException extends YADMException 
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
