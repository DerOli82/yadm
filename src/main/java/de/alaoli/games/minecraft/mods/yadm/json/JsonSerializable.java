package de.alaoli.games.minecraft.mods.yadm.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;

public interface JsonSerializable
{
	public JsonElement serialize( JsonSerializationContext context );

	public void deserialize( JsonElement json, JsonDeserializationContext context );
}
