package de.alaoli.games.minecraft.mods.yadm.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

import cpw.mods.fml.relauncher.ReflectionHelper;
import de.alaoli.games.minecraft.mods.yadm.data.settings.Setting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingGroup;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.interceptor.Injectable;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;
import de.alaoli.games.minecraft.mods.yadm.manager.Manageable;
import de.alaoli.games.minecraft.mods.yadm.network.Packageable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.WorldInfo;

public class Dimension extends SettingGroup implements Manageable, JsonSerializable, Packageable, Injectable
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

	/********************************************************************************
	 * Methods - Implement Injectable
	 ********************************************************************************/

	@Override
	public void injectInto( Object target )
	{
		if( target instanceof WorldProvider )
		{
			WorldProvider worldProvider = (WorldProvider)target;
			WorldInfo worldInfo = worldProvider.worldObj.getWorldInfo();
			Class clazz	= worldInfo.getClass();

			//Get and reflect theWorldInfo
			if( clazz == DerivedWorldInfo.class )
			{
				worldInfo = (WorldInfo) ReflectionHelper.getPrivateValue( 
					DerivedWorldInfo.class, 
					(DerivedWorldInfo)worldInfo, 
					new String[] { "field_76115_a", "theWorldInfo" } 
				);
			}
			List targets = new ArrayList();;
			targets.add( worldProvider );
			targets.add( worldInfo );
			
			for( Setting setting : this.getAll().values() )
			{
				if( setting instanceof Injectable )
				{
					((Injectable)setting).injectInto( targets );
				}
			}
		}
	}

	@Override
	public void injectInto( List targets ) 
	{
		//Nothing to do
	}
}
