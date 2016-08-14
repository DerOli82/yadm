package de.alaoli.games.minecraft.mods.yadm.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class AbstractManager
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private String savePath;
	
	private Map<String, Manageable> data;
	
	private Map<String, Set<Manageable>> groupedData;
	
	protected boolean dirty;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public AbstractManager()
	{
		this.data = new HashMap<String, Manageable>();
		this.groupedData = new HashMap<String, Set<Manageable>>();
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
	
	public void add( Manageable data )
	{
		if( !this.groupedData.containsKey( data.getManageableGroupName() ) )
		{
			this.groupedData.put( data.getManageableGroupName(), new HashSet<Manageable>() );
		}
		this.groupedData.get( data.getManageableGroupName() ).add( data );
		this.data.put( data.getManageableName(), data );
		this.markDirty();
	}
	
	public void addGroup( Set<Manageable> group )
	{
		for( Manageable data : group )
		{
			this.add( data );
		}
	}
	
	public void remove( Manageable data )
	{
		if( this.groupedData.containsKey( data.getManageableGroupName() ) )
		{
			this.groupedData.get( data.getManageableGroupName() ).remove( data );
		}
		this.data.remove( data.getManageableName() );
		this.markDirty();
	}
	
	public void removeGroup( Set<Manageable> group )
	{
		for( Manageable data : group )
		{
			this.remove( data );
		}
	}
	
	public boolean exists( Manageable data )
	{
		return this.data.containsKey( data.getManageableName() );
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
	
	public Manageable get( String name )
	{
		return this.data.get( name );
	}
	
	public Set<Entry<String, Manageable>> getAll()
	{
		return this.data.entrySet();
	}
	
	public Map<String, Set<Manageable>> getAllGroups()
	{
		return this.groupedData;
	}
}
