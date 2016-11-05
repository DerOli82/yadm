package de.alaoli.games.minecraft.mods.yadm.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import cpw.mods.fml.relauncher.ReflectionHelper;
import de.alaoli.games.minecraft.mods.yadm.data.settings.Setting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingFactory;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingGroup;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;
import de.alaoli.games.minecraft.mods.yadm.interceptor.Injectable;
import de.alaoli.games.minecraft.mods.yadm.json.JsonSerializable;
import de.alaoli.games.minecraft.mods.yadm.manager.Manageable;
import de.alaoli.games.minecraft.mods.yadm.network.Packageable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.WorldInfo;

public class Dimension extends SettingGroup implements Manageable, JsonSerializable, Packageable, Injectable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/
	
	/**
	 * Required
	 */
	private int id;
	
	/**
	 * Required
	 */
	private String group;
	
	/**
	 * Required
	 */
	private String name;
	
	/**
	 * Optional
	 */
	private UUID owner;
	
	private boolean isRegistered;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public Dimension() {}
	
	public Dimension( int id, String group, String name )
	{
		this.id = id;
		this.group = group;
		this.name = name;
		this.isRegistered = false;
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

	public String getGroup() 
	{
		return this.group;
	}
	
	public String getName() 
	{
		return this.name;
	}

	public UUID getOwner()
	{
		return this.owner;
	}
	
	public void setOwner( EntityPlayer owner )
	{
		this.setOwner( owner.getUniqueID() );
	}
	
	public void setOwner( UUID owner )
	{
		this.owner = owner;
	}
	
	public void setRegistered( boolean isRegistered )
	{
		this.isRegistered = isRegistered;
	}
	
	public boolean isRegistered()
	{
		return this.isRegistered;
	}
	
	public boolean hasOwner()
	{
		return this.owner != null;
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
	 * Methods - Implement Manageable
	 ********************************************************************************/

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
		
		json.add( "id", this.id );
		json.add( "group", this.group );
		json.add( "name", this.name );
		if( this.owner != null )
		{
			json.add( "owner", this.owner.toString() );
		}
		json.add( "settings", super.serialize().asArray() );
		
		return json;
	}

	@Override
	public void deserialize( JsonValue json ) throws DataException
	{
		try
		{
			if( !json.isObject() ) { throw new DataException( "Dimension isn't a JsonObject." ); }
			
			JsonObject obj = json.asObject();
			
			if( obj.get( "id" ) == null ) { throw new DataException( "Dimension 'id' is missing." ); }
			if( !obj.get( "id" ).isNumber() ) { throw new DataException( "Dimension 'id' isn't a number." ); }
			
			if( obj.get( "group" ) == null ) { throw new DataException( "Dimension 'group' is missing." ); }
			if( !obj.get( "group" ).isString() ) { throw new DataException( "Dimension 'group' isn't a string." ); }
			
			if( obj.get( "name" ) == null ) { throw new DataException( "Dimension 'name' is missing." ); }
			if( !obj.get( "name" ).isString() ) { throw new DataException( "Dimension 'name' isn't a string." ); }
			
			this.id = obj.get( "id" ).asInt();
			this.group = obj.get( "group" ).asString();
			this.name = obj.get( "name" ).asString();
			
			if( ( json.asObject().get("owner") != null ) &&
				( json.asObject().get( "owner" ).isString() ) )
			{
				this.owner = UUID.fromString( json.asObject().get( "owner" ).asString() );
			}
			super.deserialize( json );
		}
		catch( DataException e )
		{
			throw new DataException( "Deserialization Exception in: " + this.group + ":" + this.name, e );
		}
	}

	/********************************************************************************
	 * Methods - Implement Packageable
	 ********************************************************************************/
	
	@Override
	public void writeToNBT( NBTTagCompound tagCompound ) 
	{	
		NBTTagCompound settingCompound;
		NBTTagList list = new NBTTagList();
		
		tagCompound.setInteger( "id", this.id );
		tagCompound.setString( "group", this.group );
		tagCompound.setString( "name", this.name );
		
		for( Setting setting : this.getAll().values() )
		{
			if( setting instanceof Packageable )
			{
				settingCompound = new NBTTagCompound();
				
				settingCompound.setString( "type", setting.getSettingType().toString() );
				((Packageable)setting).writeToNBT( settingCompound );
				list.appendTag( settingCompound );
			}
		}
		tagCompound.setTag( "settings", list );
	}

	@Override
	public void readFromNBT( NBTTagCompound tagCompound ) 
	{
		Setting setting;
		NBTTagCompound settingCompound;
		
		this.id = tagCompound.getInteger( "id" );
		this.group = tagCompound.getString( "group" );
		this.name = tagCompound.getString( "name" );
		
		NBTTagList list =(NBTTagList)tagCompound.getTag( "settings" );
		
		for( int i = 0; i< list.tagCount(); i++ )
		{
			settingCompound = list.getCompoundTagAt( i );
			setting = SettingFactory.createNewInstance( settingCompound.getString( "type" ) ); 
			
			((Packageable)setting).readFromNBT(settingCompound);		
			this.add(setting );
		}
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
