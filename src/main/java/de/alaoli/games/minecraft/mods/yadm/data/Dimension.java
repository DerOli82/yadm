package de.alaoli.games.minecraft.mods.yadm.data;

import com.google.gson.annotations.Expose;

import net.minecraft.nbt.NBTTagCompound;

public class Dimension implements DataObject, NBTData
{
	/**********************************************************************
	 * Attributes 
	 **********************************************************************/

	@Expose
	protected int id;
	
	@Expose
	protected String name;
	
	@Expose
	protected DimensionSettings settings;
	
	private boolean deleted;
	
	private boolean isRegistered;
	
	/**********************************************************************
	 * Methods 
	 **********************************************************************/

	public Dimension( NBTTagCompound tagCompound ) 
	{
		this.readFromNBT( tagCompound );
		this.isRegistered = false;
		this.deleted = false;
	}
	
	public Dimension( int id, String name, DimensionSettings settings )
	{
		this.id = id;
		this.name = name;
		this.settings = settings;
		this.isRegistered = false;
		this.deleted = false;
	}
	
	public boolean isRegistered()
	{
		return this.isRegistered;
	}
	

	public void markDeleted() 
	{
		StringBuilder name = new StringBuilder()
			.append( "DELETED Dimension '" )
			.append( this.name )
			.append( "'" );
		this.name = name.toString();
		this.deleted = true;
	}
	
	public boolean canTeleport()
	{
		return this.isRegistered && !this.deleted;
	}
	/**********************************************************************
	 * Methods - Getter/Setter
	 **********************************************************************/
		
	public int getId() 
	{
		return this.id;
	}

	public DimensionSettings getSettings()
	{
		return this.settings;
	}
	
	public void setRegistered( boolean isRegistered )
	{
		this.isRegistered = isRegistered;
	}

	/********************************************************************************
	 * Methods - Implements DataObject
	 ********************************************************************************/	

	@Override
	public String getName() 
	{
		return this.name;
	}
	
	/********************************************************************************
	 * Methods - Implements NBTData
	 ********************************************************************************/	
	
	@Override
	public void writeToNBT( NBTTagCompound tagCompound ) 
	{	
		NBTTagCompound settingsTagCompound = new NBTTagCompound();
		
		this.settings.writeToNBT(settingsTagCompound);
		
		tagCompound.setInteger( "id", this.id );
		tagCompound.setString( "name", this.name );
		tagCompound.setTag( "settings", settingsTagCompound );
	}

	@Override
	public void readFromNBT( NBTTagCompound tagCompound ) 
	{
		this.id = tagCompound.getInteger( "id" );
		this.name = tagCompound.getString( "name" );
		this.settings = new DimensionSettings( tagCompound.getCompoundTag( "settings" ) );
	}


}
