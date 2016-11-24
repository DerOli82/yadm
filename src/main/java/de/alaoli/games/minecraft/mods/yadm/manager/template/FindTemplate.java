package de.alaoli.games.minecraft.mods.yadm.manager.template;

import de.alaoli.games.minecraft.mods.yadm.data.Template;

public interface FindTemplate 
{
	public Template findTemplate( String name ) throws TemplateException;
	public Template findTemplate( String group, String name ) throws TemplateException;
}
