package de.alaoli.games.minecraft.mods.yadm.data;

import java.util.UUID;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;
import de.alaoli.games.minecraft.mods.yadm.manager.Manageable;
import net.minecraft.entity.player.EntityPlayer;

public class Player implements Manageable, JsonSerializable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private UUID id;
	
	private String name;
	
	private Dimension dimension;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public Player() {}
	
	public Player( EntityPlayer player )
	{
		this.id = player.getUniqueID();
		this.name = player.getDisplayName();
	}
	
	public Player( UUID id, String name )
	{
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() 
	{
		return this.name + " (" + this.id.toString() + ")";
	}

	@Override
	public boolean equals( Object obj )
	{
		if( this == obj ) { return true; }
		if( !(obj instanceof Player) ) { return false; }
		
		return this.id.equals( ((Player)obj).getId() );
	}

	@Override
	public int hashCode() 
	{
		return this.id.hashCode();
	}

	public UUID getId()
	{
		return this.id;
	}
	
	public void setDimension( Dimension dimension )
	{
		this.dimension = dimension;
	}
	
	public Dimension getDimension()
	{
		return this.dimension;
	}
	
	public boolean ownsDimension()
	{
		return this.dimension != null;
	}

	/********************************************************************************
	 * Methods - Implement Manageable
	 ********************************************************************************/

	@Override
	public void setManageableGroupName( String name ) {}
	
	@Override
	public String getManageableGroupName() 
	{
		return "players";
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
	
	/********************************************************************************
	 * Methods - Implement JsonSerializable
	 ********************************************************************************/

	@Override
	public JsonValue serialize() throws DataException
	{
		JsonObject json = new JsonObject();
		
		json.add( "id", this.id.toString() );
		json.add( "name", this.name );
		
		return json;
	}

	@Override
	public void deserialize( JsonValue json ) throws DataException
	{
		if( !json.isObject() ) { throw new DataException( "Player isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();
		
		if( obj.get( "id" ) == null ) { throw new DataException( "Player 'id' is missing." ); }
		if( !obj.get( "id" ).isString() ) { throw new DataException( "Player 'id' isn't a string." ); }
		
		if( obj.get( "name" ) == null ) { throw new DataException( "Player 'name' is missing." ); }
		if( !obj.get( "name" ).isString() ) { throw new DataException( "Player 'name' isn't a string." ); }
		
		this.id = UUID.fromString( obj.get( "id" ).asString() );
		this.name = obj.get( "name" ).asString();
	}	
}
