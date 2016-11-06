package de.alaoli.games.minecraft.mods.yadm.json;

public interface JsonFileAdapter 
{
	public void setSavePath( String savePath );
	public String getSavePath();
	
	public void setDirty( boolean flag );
	public boolean isDirty();
	
	public void save();
	public void load();
}
