package de.alaoli.games.minecraft.mods.yadm.json;

import com.eclipsesource.json.JsonValue;

import de.alaoli.games.minecraft.mods.yadm.data.DataException;

public interface JsonSerializable
{
	public JsonValue serialize() throws DataException;

	public void deserialize( JsonValue json ) throws DataException;
}
