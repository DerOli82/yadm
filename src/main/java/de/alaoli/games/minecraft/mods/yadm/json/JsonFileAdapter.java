package de.alaoli.games.minecraft.mods.yadm.json;

import java.io.IOException;

import de.alaoli.games.minecraft.mods.yadm.data.DataException;

public interface JsonFileAdapter 
{
	public void setSavePath( String savePath );
	public String getSavePath();
	
	public void setDirty( boolean flag );
	public boolean isDirty();
	
	public void save() throws IOException, DataException;
	public void load() throws IOException, DataException;
	
	public void cleanup();
}
