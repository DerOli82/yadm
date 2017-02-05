package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.util.List;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import cpw.mods.fml.relauncher.ReflectionHelper;
import de.alaoli.games.minecraft.mods.lib.common.data.DataException;
import de.alaoli.games.minecraft.mods.lib.common.json.JsonSerializable;
import de.alaoli.games.minecraft.mods.lib.common.network.Packageable;
import de.alaoli.games.minecraft.mods.yadm.world.Injectable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public class GeneratorOptionsSetting implements Setting, JsonSerializable, Packageable, Injectable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/

	private String value;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public String getValue()
	{
		return this.value;
	}
	
	/********************************************************************************
	 * Methods - Implement Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType()
	{
		return SettingType.GENERATOROPTIONS;
	}

	@Override
	public boolean isSettingRequired() 
	{
		return false;
	}
	
	/********************************************************************************
	 * Methods - Implement JsonSerializable
	 ********************************************************************************/

	@Override
	public JsonValue serialize() 
	{
		JsonObject json = new JsonObject();

		json.add( "type", this.getSettingType().toString() );
		json.add( "value", this.value );
		
		return json;
	}

	@Override
	public void deserialize( JsonValue json )
	{
		if( !json.isObject() ) { throw new DataException( "GeneratorOptionsSetting isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();
		
		if( obj.get( "value" ) == null ) { throw new DataException( "GeneratorOptionsSetting 'value' is missing." ); }
		if( !obj.get( "value" ).isString() ) { throw new DataException( "GeneratorOptionsSetting 'value' isn't a string." ); }
		
		this.value = obj.get( "value" ).asString();
	}
	
	/********************************************************************************
	 * Methods - Implement Packageable
	 ********************************************************************************/
	
	@Override
	public void writeToNBT( NBTTagCompound tagCompound ) 
	{	
		tagCompound.setString( "value", this.value );
	}

	@Override
	public void readFromNBT( NBTTagCompound tagCompound ) 
	{
		this.value = tagCompound.getString( "value" );
	}	
	
	/********************************************************************************
	 * Methods - Implements Injectable
	 ********************************************************************************/

	@Override
	public void injectInto( Object target )
	{
		if( target instanceof World )
		{
			World world = (World)target;
			world.provider.field_82913_c = this.value;
		}
		else if( target instanceof WorldInfo )
		{
			WorldInfo worldInfo = (WorldInfo)target;
			
			ReflectionHelper.setPrivateValue( 
				WorldInfo.class, worldInfo, 
				this.value,
				new String[] { "field_82576_c", "generatorOptions" } 
			);				
		}
	}		
	
	@Override
	public void injectInto( List targets )
	{
		for( Object target : targets )
		{
			this.injectInto( target );
		}
	}			
}
