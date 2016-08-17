package de.alaoli.games.minecraft.mods.yadm.json;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;

public class DimensionJsonAdapter extends AbstractJsonAdapter<Dimension>
{
	@Override
	public String getName() 
	{
		return "dimensions";
	}

	@Override
	public Dimension createInstance() 
	{
		return new Dimension();
	}
}
