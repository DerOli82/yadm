package de.alaoli.games.minecraft.mods.yadm.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import de.alaoli.games.minecraft.mods.yadm.data.DimensionPattern;

public class DimensionPatternAdapter implements JsonSerializer<DimensionPattern>, JsonDeserializer<DimensionPattern>
{
	@Override
	public JsonElement serialize( DimensionPattern src, Type typeOfSrc, JsonSerializationContext context )
	{
		JsonObject result = new JsonObject();
		
		result.add( "name", new JsonPrimitive( src.getName() ) );
		result.add( "provider", new JsonPrimitive( src.getProvider() ) );
		result.add( "type", new JsonPrimitive( src.getType() ) );
		/*
		if( src.getSeed() == null )
		{
			result.add( "seed", new JsonNull() );
		}
		else
		{
			result.add( "seed", new JsonPrimitive( src.getSeed() ) );
		}*/
		
		return result;
	}

	@Override
	public DimensionPattern deserialize( JsonElement json, Type typeOfT, JsonDeserializationContext context ) throws JsonParseException 
	{
		JsonObject jsonObject = json.getAsJsonObject();

		String name		= jsonObject.get( "name" ).getAsString();
		String provider	= jsonObject.get( "provider" ).getAsString();
		String type		= jsonObject.get( "type" ).getAsString();
		//Long seed = ( !jsonObject.get( "seed" ).isJsonNull() ) ? jsonObject.get( "seed" ).getAsLong() : null;
		
		return new DimensionPattern( name, provider, type, null );
	}
}
