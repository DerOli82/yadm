package de.alaoli.games.minecraft.mods.yadm.data.settings;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.yadm.data.DataException;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;
import de.alaoli.games.minecraft.mods.yadm.network.Packageable;
import net.minecraft.nbt.NBTTagCompound;

public class WorldProviderSetting implements Setting, JsonSerializable, Packageable
{
	/********************************************************************************
	 * Constants
	 ********************************************************************************/
	
	public static final String OVERWORLD = "overworld";
	public static final String NETHER = "nether";
	public static final String END = "end";
		
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
				
	private int id;
	
	private String name;

	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public WorldProviderSetting() {}
	
	public WorldProviderSetting( String name )
	{
		this.name = name;
	}
	
	public int getId() 
	{
		return this.id;
	}

	public String getName() 
	{
		return this.name;
	}
	
	public void setId( int Id ) 
	{
		this.id = Id;
	}

	/********************************************************************************
	 * Methods - Implements Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType()
	{
		return SettingType.WORLDPROVIDER;
	}

	@Override
	public boolean isRequired() 
	{
		return true;
	}
	
	/********************************************************************************
	 * Methods - Implement JsonSerializable
	 ********************************************************************************/

	@Override
	public JsonValue serialize() 
	{
		JsonObject json = new JsonObject();

		json.add( "type", this.getSettingType().toString() );
		json.add( "name", this.name );

		return json;
	}

	@Override
	public void deserialize( JsonValue json ) throws DataException
	{
		if( !json.isObject() ) { throw new DataException( "WorldProviderSetting isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();				
		
		if( obj.get( "name" ) == null ) { throw new DataException( "WorldProviderSetting 'name' is missing." ); }
		if( !obj.get( "name" ).isString() ) { throw new DataException( "WorldProviderSetting 'name' isn't a string." ); }
		
		this.name = obj.get( "name" ).asString();
	}	
	
	/********************************************************************************
	 * Methods - Implement Packageable
	 ********************************************************************************/
	
	@Override
	public void writeToNBT( NBTTagCompound tagCompound ) 
	{	
		tagCompound.setString( "name", this.name );
	}

	@Override
	public void readFromNBT( NBTTagCompound tagCompound ) 
	{
		this.name = tagCompound.getString( "name" );
	}
}
