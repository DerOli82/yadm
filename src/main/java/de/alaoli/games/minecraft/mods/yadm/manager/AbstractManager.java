package de.alaoli.games.minecraft.mods.yadm.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.alaoli.games.minecraft.mods.yadm.data.DataObject;

public abstract class AbstractManager 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private String savePath;
	
	private Map<String, DataObject> data;
	
	protected boolean dirty;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public AbstractManager()
	{
		this.data = new HashMap<String, DataObject>();
		this.dirty = false;
	}
	
	public abstract void load();
	
	public abstract void save();
	
	public void reload()
	{
		//safety first
		if( this.dirty )
		{
			this.save();
		}
		this.data.clear();
		this.load();
	}
	
	public void add( DataObject dataObject )
	{
		this.data.put( dataObject.getName(), dataObject );
		this.markDirty();
	}
	
	public void remove( DataObject dataObject )
	{
		this.data.remove( dataObject.getName() );
		this.markDirty();
	}
	
	public boolean exists( DataObject dataObject )
	{
		return this.data.containsKey( dataObject.getName() );
	}
	
	public boolean exists( String name )
	{
		return this.data.containsKey( name );
	}
	
	public void markDirty()
	{
		this.dirty = true;
	}
	
	public boolean isDirty()
	{
		return this.dirty;
	}
	
	public boolean isEmpty()
	{
		return this.data.isEmpty();
	}
	
	protected void clear()
	{
		this.data.clear();
	}
	
	/********************************************************************************
	 * Methods - Getter/Setter
	 ********************************************************************************/
	
	public String getSavePath() 
	{
		return this.savePath;
	}

	public void setSavePath( String savePath )
	{
		this.savePath = savePath;
	}	
	
	public DataObject get( String name )
	{
		return this.data.get( name );
	}
	
	public Set<Entry<String, DataObject>> getAll()
	{
		return this.data.entrySet();
	}
}
