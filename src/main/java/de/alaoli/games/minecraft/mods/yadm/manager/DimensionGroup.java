package de.alaoli.games.minecraft.mods.yadm.manager;

import de.alaoli.games.minecraft.mods.yadm.data.Dimension;

public class DimensionGroup extends ManageableGroup 
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public DimensionGroup( String name )
	{
		super(name);
	}

	/********************************************************************************
	 * Methods - Implement ManageableGroup
	 ********************************************************************************/
	
	@Override
	public Manageable create() 
	{
		return new Dimension();
	}
	
}
