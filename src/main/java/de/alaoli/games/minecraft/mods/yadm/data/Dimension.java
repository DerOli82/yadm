package de.alaoli.games.minecraft.mods.yadm.data;

import java.util.ArrayList;
import java.util.List;

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
	
	private int id;
	
	private String name;
	
	private boolean isRegistered;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public Dimension() {}
	
	public Dimension( int id, String name )
	{
		this.id = id;
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
	public JsonValue serialize() 
	{
		JsonObject json = new JsonObject();
		
		json.add( "id", this.id );
		json.add( "name", this.name );
		json.add( "settings", super.serialize().asObject() );
		
		return json;
	}

	@Override
	public void deserialize( JsonValue json )
	{
		this.id = json.asObject().get( "id" ).asInt();
		this.name = json.asObject().get( "name" ).asString();
		
		super.deserialize( json.asObject().get( "settings" ).asArray() );
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
		this.name = tagCompound.getString( "name" );
		
		NBTTagList list =(NBTTagList)tagCompound.getTag( "settings" );
		
		for( int i = 0; i< list.tagCount(); i++ )
		{
			settingCompound = list.getCompoundTagAt( i );
			setting = SettingFactory.createNewInstance( settingCompound.getString( "type" ) ); 
					
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
