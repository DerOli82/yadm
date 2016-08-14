package de.alaoli.games.minecraft.mods.yadm.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingGroup;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;
import de.alaoli.games.minecraft.mods.yadm.manager.Manageable;
import de.alaoli.games.minecraft.mods.yadm.network.Packageable;
import net.minecraft.nbt.NBTTagCompound;

public class Dimension extends SettingGroup implements Manageable, JsonSerializable, Packageable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	private String group; 
	
	private int id;
	
	private String name;
	
	private boolean isRegistered;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public Dimension( int id, String name )
	{
		this.id = id;
		this.name = name;
		this.isRegistered = false;
	}
	
	public Dimension( int id, String name, String group )
	{
		this( id, name );
		
		this.group = group;
	}
	
	public Dimension( NBTTagCompound tagCompound ) 
	{
		this.readFromNBT( tagCompound );
		this.isRegistered = false;
	
	}
	
	public int getId() 
	{
		return this.id;
	}

	public String getName() 
	{
		return this.name;
	}

	public void setRegistered( boolean isRegistered )
	{
		this.isRegistered = isRegistered;
	}
	
	public boolean isRegistered()
	{
		return this.isRegistered;
	}
	
	/********************************************************************************
	 * Methods - Implement Manageable
	 ********************************************************************************/

	@Override
	public String getManageableGroupName() 
	{
		return this.group;
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
	public JsonElement serialize( JsonSerializationContext context ) 
	{
		JsonObject result = super.serialize( context ).getAsJsonObject();
		
		result.addProperty( "id", this.id );
		result.addProperty( "name", this.name );
		
		return result;
	}

	@Override
	public void deserialize( JsonElement json, JsonDeserializationContext context )
	{
		JsonObject data = json.getAsJsonObject();

		if( !data.has( "id" ) ) { 
			throw new JsonParseException( "Dimension requires a id." ); 
		}		
		if( !data.has( "name" ) ) { 
			throw new JsonParseException( "Dimension requires a name." ); 
		}
		this.id = data.get( "id" ).getAsInt();
		this.name = data.get( "name" ).getAsString();
		
		super.deserialize( json, context );
	}
	
	/********************************************************************************
	 * Methods - Implement Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType() 
	{
		return null;
	}

	@Override
	public boolean isRequired() 
	{
		return true;
	}	

	/********************************************************************************
	 * Methods - Implement Packageable
	 ********************************************************************************/
	
	@Override
	public void writeToNBT( NBTTagCompound tagCompound ) 
	{	

	}

	@Override
	public void readFromNBT( NBTTagCompound tagCompound ) 
	{

	}
}
