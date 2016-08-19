package de.alaoli.games.minecraft.mods.yadm.manager;

import de.alaoli.games.minecraft.mods.yadm.data.Template;

public class TemplateGroup extends ManageableGroup 
{
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	public TemplateGroup( String name )
	{
		super(name);
	}

	/********************************************************************************
	 * Methods - Implement ManageableGroup
	 ********************************************************************************/
	
	@Override
	public Manageable create() 
	{
		return new Template();
	}
	
}
