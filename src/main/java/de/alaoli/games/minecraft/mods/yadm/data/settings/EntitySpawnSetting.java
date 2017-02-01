package de.alaoli.games.minecraft.mods.yadm.data.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.lib.common.data.DataException;
import de.alaoli.games.minecraft.mods.lib.common.json.JsonSerializable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;

public class EntitySpawnSetting implements Setting, JsonSerializable
{
	/********************************************************************************
	 * Attributes
	 ********************************************************************************/

	Map<EnumCreatureType, Integer> typeSpawnLimit = new HashMap<>();
	Map<EnumCreatureType, Integer> typeSpawn = new HashMap<>();
	
	Map<String, Integer> entitySpawnLimit = new HashMap<>();
	Map<String, Integer> entitySpawn = new HashMap<>();
	
	/********************************************************************************
	 * Methods
	 ********************************************************************************/

	protected EnumCreatureType getCreatureType( Entity entity )
	{
		EnumCreatureType type = null;
	
		if( entity.isCreatureType( EnumCreatureType.monster, false ) )
		{
			type = EnumCreatureType.monster;
		}
		else if( entity.isCreatureType( EnumCreatureType.creature, false ) )
		{
			type = EnumCreatureType.creature;
		}
		else if( entity.isCreatureType( EnumCreatureType.ambient, false ) )
		{
			type = EnumCreatureType.ambient;
		}
		else if( entity.isCreatureType( EnumCreatureType.waterCreature, false ) )
		{
			type = EnumCreatureType.waterCreature;
		}
		return type;
	}
	
	protected boolean ignoreEntityLimit( String name )
	{
		if( !this.entitySpawnLimit.containsKey( name ) ) { return false; } 
		
		return this.entitySpawnLimit.get( name ) < 0;
	}
	
	public boolean isLimitReached( Entity entity )
	{
		EnumCreatureType type = this.getCreatureType( entity );
		String name = (String)EntityList.classToStringMapping.get( entity.getClass() );
		
		//Ignore all limits
		if( this.ignoreEntityLimit( name ) ) { return false; }
		
		//Entity limit over type limit
		if( ( name != null ) && 
			( this.entitySpawnLimit.containsKey( name ) ) && 
			( this.entitySpawn.containsKey( name ) ) ) 
		{ 
			return this.entitySpawn.get( name ) >= this.entitySpawnLimit.get( name ); 
		}
		else if( ( type != null ) &&
				 ( this.typeSpawnLimit.containsKey( type ) ) )
		{
			return this.typeSpawn.get( type ) >= this.typeSpawnLimit.get( type );
		}
		else
		{
			return false;
		}
	}
	
	public void increase( Entity entity )
	{
		EnumCreatureType type = this.getCreatureType( entity );
		String name = (String)EntityList.classToStringMapping.get( entity.getClass() );
		
		//Ignore all limits
		if( this.ignoreEntityLimit( name ) ) { return; }
		
		//Entity limit over type limit
		if( ( name != null ) && 
			( this.entitySpawnLimit.containsKey( name ) ) && 
			( this.entitySpawn.containsKey( name ) ) ) 
		{ 
			this.entitySpawn.put( name, this.entitySpawn.get( name ) + 1 );
		}
		else if( ( type != null ) &&
				 ( this.typeSpawnLimit.containsKey( type ) ) )
		{
			this.typeSpawn.put( type, this.typeSpawn.get( type ) + 1 );
		}
	}
	
	public void decrease( Entity entity )
	{
		EnumCreatureType type = this.getCreatureType( entity );
		String name = (String)EntityList.classToStringMapping.get( entity.getClass() );
		
		//Ignore all limits
		if( this.ignoreEntityLimit( name ) ) { return; }
		
		int spawned;
		
		//Entity limit over type limit
		if( ( name != null ) && 
			( this.entitySpawnLimit.containsKey( name ) ) && 
			( this.entitySpawn.containsKey( name ) ) ) 
		{ 
			spawned = this.entitySpawn.get( name );
			
			if( spawned > 0 )
			{
				this.entitySpawn.put( name, spawned - 1 );
			}
		}
		else if( ( type != null ) &&
				 ( this.typeSpawnLimit.containsKey( type ) ) )
		{
			spawned = this.typeSpawn.get( type );
			
			if( spawned > 0 )
			{
				this.typeSpawn.put( type, spawned - 1 );
			}
		}
	}
	
	/********************************************************************************
	 * Methods - Implement Setting
	 ********************************************************************************/
	
	@Override
	public SettingType getSettingType()
	{
		return SettingType.ENTITYSPAWN;
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
		JsonObject typeLimit = new JsonObject();
		JsonObject entityLimit = new JsonObject();
		
		json.add( "type", this.getSettingType().toString() );

		for( Entry<EnumCreatureType, Integer> entry : this.typeSpawnLimit.entrySet() )
		{
			switch( entry.getKey() )
			{
				case monster :
					typeLimit.add( "monster", entry.getValue().intValue() );
					break;
					
				case creature :
					typeLimit.add( "creature", entry.getValue().intValue() );
					break;
					
				case ambient :
					typeLimit.add( "ambient", entry.getValue().intValue() );
					break;
					
				case waterCreature :
					typeLimit.add( "waterCreature", entry.getValue().intValue() );
					break;
			}
		}
		json.add( "typeLimit", typeLimit );
		
		for( Entry<String, Integer> entry : this.entitySpawnLimit.entrySet() )
		{
			entityLimit.add( entry.getKey(), entry.getValue().intValue() );
		}
		json.add( "entityLimit", entityLimit );
		
		return json;
	}

	@Override
	public void deserialize( JsonValue json )
	{
		if( !json.isObject() ) { throw new DataException( "EntitySpawnSetting isn't a JsonObject." ); }
		
		JsonObject obj = json.asObject();		
		
		if( obj.get( "typeLimit" ) == null ) { throw new DataException( "EntitySpawnSetting 'typeLimit' is missing." ); }
		if( !obj.get( "typeLimit" ).isObject() ) { throw new DataException( "EntitySpawnSetting 'typeLimit' isn't a JsonObject." ); }
		
		JsonObject typelimit = obj.get( "typeLimit" ).asObject();
		
		EnumCreatureType type;
		
		for( String name : typelimit.names() )
		{
			switch( name )
			{
				case "monster" :
					type = EnumCreatureType.monster;
					break;
					
				case "creature" :
					type = EnumCreatureType.creature;
					break;
					
				case "ambient" :
					type = EnumCreatureType.ambient;
					break;
					
				case "waterCreature" :
					type = EnumCreatureType.waterCreature;
					break;
					
				default :
					throw new DataException( "EntitySpawnSetting '" + name + "' unknown creature type." );
			}
			if( !typelimit.get( name ).isNumber() ) { throw new DataException( "EntitySpawnSetting '" + name + "' limit isn't a number." ); }
			
			this.typeSpawnLimit.put( type, typelimit.get( name ).asInt() );
			this.typeSpawn.put( type, 0 );
		}
		
		if( obj.get( "entityLimit" ) == null ) { throw new DataException( "EntitySpawnSetting 'entityLimit' is missing." ); }
		if( !obj.get( "entityLimit" ).isObject() ) { throw new DataException( "EntitySpawnSetting 'entityLimit' isn't a JsonObject." ); }
		
		JsonObject entitylimit = obj.get( "entityLimit" ).asObject();
				
		for( String name : entitylimit.names() )
		{
			if( !EntityList.stringToClassMapping.containsKey( name ) ) { throw new DataException( "EntitySpawnSetting '" + name + "' unknown entity." ); }
			if( !entitylimit.get( name ).isNumber() ) { throw new DataException( "EntitySpawnSetting '" + name + "' limit isn't a number." ); }
			
			this.entitySpawnLimit.put( name, entitylimit.get( name ).asInt() );
			this.entitySpawn.put( name, 0 );
		}
	}
}
