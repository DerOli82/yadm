package de.alaoli.games.minecraft.mods.yadm.json;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;
import de.alaoli.games.minecraft.mods.yadm.data.Template;

public class DimensionJsonAdapter extends AbstractJsonAdapter<Dimension>
{
	@Override
	public String getName() 
	{
		return "dimension";
	}

	@Override
	public Class<Dimension> getType() 
	{
		return Dimension.class;
	}
}
