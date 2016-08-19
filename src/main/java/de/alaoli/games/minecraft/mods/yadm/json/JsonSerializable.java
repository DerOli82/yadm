package de.alaoli.games.minecraft.mods.yadm.json;

import com.eclipsesource.json.JsonValue;

public interface JsonSerializable
{
	public JsonValue serialize();

	public void deserialize( JsonValue json );
}
