package de.alaoli.games.minecraft.mods.lib.common.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.lib.common.data.DataException;
import de.alaoli.games.minecraft.mods.lib.common.json.JsonSerializable;

public abstract class ManageableGroup implements Manageable, JsonSerializable
{
	/********************************************************************************
	 * Attribute
	 ********************************************************************************/
	
	private String name;
	private Map<String, Manageable> data = new HashMap<>();;

	/********************************************************************************
	 * Method
	 ********************************************************************************/
	
	public ManageableGroup( String name )
	{
		this.name = name;
	}
	
	public abstract Manageable createManageable();
	
	public void addManageable( Manageable data )
	{
		this.data.put( data.getManageableName(), data );
	}
	
	public void removeManageable( Manageable data )
	{
		this.data.remove( data.getManageableName() );
	}
	
	public boolean existsManageable( String name )
	{
		return this.data.containsKey( name );
	}
	
	public boolean isManageableEmpty()
	{
		return this.data.isEmpty();
	}
	
	protected void clearManageable()
	{
		this.data.clear();
	}
	
	public Manageable getManageable( String name )
	{
		return this.data.get( name );
	}
	
	public Set<Entry<String, Manageable>> getManageable()
	{
		return this.data.entrySet();
	}
	
	/********************************************************************************
	 * Method - Implement Manageable
	 ********************************************************************************/

	@Override
	public void setManageableGroupName( String name ) 
	{
		this.setManageableName( name );
	}

	@Override
	public String getManageableGroupName() 
	{
		return this.getManageableName();
	}
		
	@Override
	public boolean hasManageableGroupName() 
	{
		return this.hasManageableName();
	}

	@Override
	public void setManageableName( String name ) 
	{
		this.name = name;
	}
	
	@Override
	public String getManageableName() 
	{
		return this.name;
	}	
	
	@Override
	public boolean hasManageableName() 
	{
		return this.name != null;
	}
	
	/********************************************************************************
	 * Method - Implement JsonSerializable
	 ********************************************************************************/

	@Override
	public JsonValue serialize() throws DataException
	{
		Manageable data;
		JsonObject json = new JsonObject();
		JsonArray array = new JsonArray();
		
		for( Entry<String, Manageable> entry : this.data.entrySet() )
		{
			data = entry.getValue();

			if( data instanceof JsonSerializable )
			{
				array.add( ((JsonSerializable)data).serialize() );
			}
		}
		json.add( this.getManageableName(), array ); 
		
		return json;
	}

	@Override
	public void deserialize( JsonValue json ) throws DataException
	{
		if( !json.isObject() ) { throw new DataException( "ManageableGroup isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();
		
		if( obj.get( this.getManageableGroupName() ) == null ) { throw new DataException( "ManageableGroup is missing." ); }
		if( !obj.get( this.getManageableGroupName() ).isArray() ) { throw new DataException( "ManageableGroup isn't an array." ); }
		
		Manageable data;
		JsonArray array = obj.get( this.getManageableName() ).asArray();
		
		for( JsonValue value : array )
		{
			data = this.createManageable();
			
			data.setManageableGroupName( this.getManageableGroupName() );
			((JsonSerializable)data).deserialize( value );
			
			this.addManageable( data );
		}
	}		
}
