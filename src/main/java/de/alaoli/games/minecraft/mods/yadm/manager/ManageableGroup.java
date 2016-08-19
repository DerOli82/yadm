package de.alaoli.games.minecraft.mods.yadm.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;

public class ManageableGroup implements Manageable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private String name;
	
	private Map<String, Manageable> data;

	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public ManageableGroup( String name )
	{
		this.name = name;
	}
	
	public void add( Manageable data )
	{
		this.data.put( data.getManageableName(), data );
	}
	
	public void remove( Manageable data )
	{
		this.data.remove( data.getManageableName() );
	}
	
	public boolean exists( String name )
	{
		return this.data.containsKey( name );
	}
	
	public boolean isEmpty()
	{
		return this.data.isEmpty();
	}
	
	protected void clear()
	{
		this.data.clear();
	}
	
	public Manageable get( String name )
	{
		return this.data.get( name );
	}
	
	public Set<Entry<String, Manageable>> getAll()
	{
		return this.data.entrySet();
	}
	
	/********************************************************************************
	 * Methods - Implement Manageable
	 ********************************************************************************/

	@Override
	public String getManageableName() 
	{
		return this.name;
	}
}
