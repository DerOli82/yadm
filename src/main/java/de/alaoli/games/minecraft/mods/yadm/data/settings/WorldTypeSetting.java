package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.util.List;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import cpw.mods.fml.relauncher.ReflectionHelper;
import de.alaoli.games.minecraft.mods.lib.common.data.DataException;
import de.alaoli.games.minecraft.mods.lib.common.json.JsonSerializable;
import de.alaoli.games.minecraft.mods.lib.common.network.Packageable;
import de.alaoli.games.minecraft.mods.yadm.Log;
import de.alaoli.games.minecraft.mods.yadm.world.FindWorldType;
import de.alaoli.games.minecraft.mods.yadm.world.Injectable;
import de.alaoli.games.minecraft.mods.yadm.world.WorldBuilder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;

public class WorldTypeSetting implements Setting, JsonSerializable, Packageable, Injectable 
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/

	protected static final FindWorldType worldType = WorldBuilder.INSTANCE;
	
	private String name;
	private WorldType type;
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/
	
	public WorldTypeSetting() {}
	
	public WorldTypeSetting( String name )
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}

	/********************************************************************************
	 * Methods - Implements Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType()
	{
		return SettingType.WORLDTYPE;
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
		json.add( "name", this.name );

		return json;
	}

	@Override
	public void deserialize( JsonValue json )
	{
		if( !json.isObject() ) { throw new DataException( "WorldTypeSetting isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();				
		
		if( obj.get( "name" ) == null ) { throw new DataException( "WorldTypeSetting 'name' is missing." ); }
		if( !obj.get( "name" ).isString() ) { throw new DataException( "WorldTypeSetting 'name' isn't a string." ); }
		
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
	
	/********************************************************************************
	 * Methods - Implements Injectable
	 ********************************************************************************/

	@Override
	public void injectInto( Object target )
	{
		if( target instanceof WorldInfo )
		{
			WorldInfo worldInfo = (WorldInfo)target;
			
			ReflectionHelper.setPrivateValue( 
				WorldInfo.class, worldInfo, 
				worldType.findWorldType( this.name ), 
				new String[] { "field_76098_b", "terrainType" } 
			);			
			Log.debug( "  - WorldType: " + this.name +" into WorldInfo" );
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
