package de.alaoli.games.minecraft.mods.lib.common.manager;

public interface Manageable
{
	public void setManageableGroupName( String name );
	public String getManageableGroupName();
	public boolean hasManageableGroupName();
	
	public void setManageableName( String name );
	public String getManageableName();
	public boolean hasManageableName();
}
