package de.alaoli.games.minecraft.mods.yadm.manager;

public abstract class AbstractManager extends ManageableGroup
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private String savePath;
		
	protected boolean dirty;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public AbstractManager( String name )
	{
		super( name );
		
		this.dirty = false;
	}
	
	public String getSavePath() 
	{
		return this.savePath;
	}

	public void setSavePath( String savePath )
	{
		this.savePath = savePath;
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
		this.clear();
		this.load();
	}
	
	/********************************************************************************
	 * Methods - Override ManageableGroup
	 ********************************************************************************/

	@Override
	public void add( Manageable data ) 
	{
		super.add( data );
		this.dirty = true;
	}

	@Override
	public void remove( Manageable data ) 
	{
		super.remove( data );
		this.dirty = true;
	}
}
