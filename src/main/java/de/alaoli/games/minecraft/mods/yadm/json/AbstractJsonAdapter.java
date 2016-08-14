package de.alaoli.games.minecraft.mods.yadm.json;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public abstract class AbstractJsonAdapter<T extends JsonSerializable> implements JsonSerializer< Set<JsonSerializable>>, JsonDeserializer< Set<JsonSerializable>>
{	
	public abstract String getName();
	
	public abstract T createInstance();
	
	@Override
	public JsonElement serialize(  Set<JsonSerializable> src, Type typeOfSrc, JsonSerializationContext context ) 
	{
		JsonObject result = new JsonObject();
		JsonArray array = new JsonArray();
		
		for( JsonSerializable item : src )
		{
			array.add( item.serialize( context ) );
		}
		result.add( this.getName(), array );
		
		return result;
	}
	
	@Override
	public  Set<JsonSerializable> deserialize( JsonElement json, Type typeOfT, JsonDeserializationContext context ) throws JsonParseException 
	{
		JsonSerializable instance;
		Set<JsonSerializable> result = new HashSet<JsonSerializable>();
		JsonArray array = json.getAsJsonObject().get( this.getName() ).getAsJsonArray();
				
		for( JsonElement item : array )
		{
			instance = this.createInstance();
			instance.deserialize(item, context);
			result.add( instance );
		}
		return result;
	}
}
