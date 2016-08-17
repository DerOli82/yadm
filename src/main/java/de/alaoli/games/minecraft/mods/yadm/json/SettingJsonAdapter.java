package de.alaoli.games.minecraft.mods.yadm.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import de.alaoli.games.minecraft.mods.yadm.data.settings.Setting;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingFactory;
import de.alaoli.games.minecraft.mods.yadm.data.settings.SettingType;

public class SettingJsonAdapter implements JsonSerializer<Setting>, JsonDeserializer<Setting>  
{
	@Override
	public JsonElement serialize( Setting src, Type typeOfSrc, JsonSerializationContext context ) 
	{
		JsonObject result = context.serialize( src, src.getClass() ).getAsJsonObject();
		result.addProperty( "type", src.getSettingType().toString() );
		
		return result;
	}

	@Override
	public Setting deserialize( JsonElement json, Type typeOfT, JsonDeserializationContext context ) throws JsonParseException 
	{
		JsonObject settingJson = json.getAsJsonObject();
		
		if( !settingJson.has( "type" ) ) {
			throw new JsonParseException( "Setting type required." ); 
		}
		Class<Setting> settingType = (Class<Setting>) SettingFactory.createNewInstance( settingJson.get( "type" ).getAsString() ).getClass();  
		settingJson.remove( "type" );
		 
		return context.deserialize( settingJson, settingType );
	}
}
