package de.alaoli.games.minecraft.mods.yadm.json;

import de.alaoli.games.minecraft.mods.yadm.data.Template;

public class TemplateJsonAdapter extends AbstractJsonAdapter<Template>
{
	@Override
	public String getName() 
	{
		return "templates";
	}

	@Override
	public Template createInstance() 
	{
		return new Template();
	}


}
