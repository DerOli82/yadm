package de.alaoli.games.minecraft.mods.yadm.data;

import com.google.gson.annotations.Expose;

public abstract class DataObject 
{
	@Expose
	private String name;
	
	public DataObject( String name )
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
}
